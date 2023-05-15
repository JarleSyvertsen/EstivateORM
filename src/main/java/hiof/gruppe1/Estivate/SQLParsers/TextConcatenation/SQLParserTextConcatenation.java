package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.getCompatAttr;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
public class SQLParserTextConcatenation implements ISQLParser {
    private final IDriverHandler sqlDriver;
    private final IObjectParser objectParser;
    private final WriteBuilder writeBuilder;
    private final ReadBuilder readBuilder;
    private final TextConcatTableManagement tableManagement;

    public SQLParserTextConcatenation(IDriverHandler sqlDriver) {
        this.sqlDriver = sqlDriver;
        this.tableManagement = new TextConcatTableManagement(sqlDriver);
        this.objectParser = new ReflectionParser();
        this.writeBuilder = new WriteBuilder();
        this.readBuilder = new ReadBuilder();
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        writeObjectToDatabase(writeObject);
        return true;
    }

    private String writeObjectToDatabase(SQLWriteObject writeObject) {
        tableManagement.createOrResizeTableIfNeeded(writeObject);

        if (writeObject.getAttributeList().get("id").getData().toString().equals("0")) {
            writeObject.getAttributeList().remove("id");
        }

        String tableName = writeObject.getAttributeList().get("class").getInnerName();
        writeObject.getAttributeList().remove("class");

        SQLWriteObject writeObjectSimple = createCopyNonPrimitives(writeObject);
        String finalString = writeBuilder.createInsertStatement(tableName,writeObjectSimple);

        int parentId = executeGetId(finalString);
        traverseNonPrimitives(writeObject, tableName, parentId);

        return String.valueOf(parentId);
    }

    private void traverseNonPrimitives(SQLWriteObject writeObject, String parentNameSimple, int parentId) {
        writeObject.getAttributeList().forEach((k, v) -> {
           if (isCollection(v.getData().getClass())) {
                Collection<?> collection = v.getData();
                collection.forEach((obj) -> persistSubElement(parentNameSimple, parentId, k, new SQLAttribute(obj.getClass(), obj)) );
                return;
            }
            if (!isSimple(v.getData().getClass())) {
                persistSubElement(parentNameSimple, parentId, k, v);
            }
        });
    }

    private void persistSubElement(String parentNameSimple, int parentId, String key, SQLAttribute value) {
        HashMap<String, SQLAttribute> parsedAttributes = objectParser.parseObjectToAttributeList(value.getDataRaw());
        SQLWriteObject recursiveObject = new SQLWriteObject(parsedAttributes);
        String objectClass = getObjectClass(recursiveObject);

        tableManagement.createAppendingTableIfMissing(parentNameSimple, objectClass, true);
        String childId = writeObjectToDatabase(recursiveObject);
        String recursiveRelationship = writeBuilder.createRelationshipInsert(key, parentNameSimple, objectClass, String.valueOf(parentId), childId);
        sqlDriver.executeNoReturnSplit(recursiveRelationship);
    }

    public <T> T readFromDatabase(Class<T> castTo, int id) {
        String SQLQuery = readBuilder.createReadableSQLString(castTo, id);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        return objectFromQuerySet(castTo, describedTable, querySet);
    }

    public <T> ArrayList<T> readFromDatabase(Class<T> castTo) {
        String SQLQuery = readBuilder.createReadableSQLString(castTo);
        return objectsFromQuerySet(castTo, SQLQuery);
    }

    public <T> ArrayList<T> readFromDatabase(Class<T> castTo, String conditional) {
        if(!conditional.startsWith(" ")) {
            conditional = " " + conditional;
        }

        String SQLQuery = readBuilder.createReadableSQLString(castTo, conditional);
        return objectsFromQuerySet(castTo, SQLQuery);
    }

    private static HashMap<String, SQLAttribute> getAttributeMap(HashMap<String, String> describedTable, ResultSet querySet) {
        HashMap<String, SQLAttribute> readAttributes = new HashMap<>();
        try {
            for (Map.Entry<String, String> entry : describedTable.entrySet()) {
                String attributeName = entry.getKey();
                String attributeValue = entry.getValue();
                String cleanAttributeName = attributeName.substring(attributeName.indexOf("_") + 1);
                switch (getCompatAttr(attributeValue)) {
                    case INT_COMPAT -> readAttributes.put(cleanAttributeName, new SQLAttribute(Integer.class, querySet.getInt(attributeName)));
                    case STRING_COMPAT -> readAttributes.put(cleanAttributeName, new SQLAttribute(String.class, querySet.getString(attributeName)));
                    case BOOLEAN_COMPAT -> readAttributes.put(cleanAttributeName, new SQLAttribute(Boolean.class, querySet.getBoolean(attributeName)));
                    case DOUBLE_COMPAT -> readAttributes.put(cleanAttributeName, new SQLAttribute(Double.class, querySet.getDouble(attributeName)));
                    case FLOAT_COMPAT -> readAttributes.put(cleanAttributeName, new SQLAttribute(Float.class, querySet.getFloat(attributeName)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readAttributes;
    }

    private <T> T objectFromQuerySet(Class<T> castTo, HashMap<String, String> describedTable, ResultSet querySet) {
        HashMap<String, SQLAttribute> attributeHashMap = getAttributeMap(describedTable, querySet);
        int parentId = attributeHashMap.get("id").getData();

        T object = objectParser.parseAttributeListToObject(castTo, attributeHashMap);
        HashMap<String, Class<?>> subElementList = objectParser.getSubElementList(object);
        subElementList.forEach((k,v) -> {
            if(isCollection(v)) {
                Class<?> collectionClass = objectParser.getCollectionInnerClass(object, k);
                for (int childId : getChildIds(castTo, parentId, k, collectionClass)) {
                    objectParser.addElementsToCollectionQueue(object, readFromDatabase(collectionClass, childId), k);
                }
                objectParser.writeToCollection(object, v, k);
                return;
            }
            int childId = getChildId(castTo, parentId, k, v);
            if (childId > 0) {
                objectParser.addElementToObject(object, readFromDatabase(v, childId), k);
            }
        });
        return object;
    }


    private <T> ArrayList<T> objectsFromQuerySet(Class<T> castTo, String SQLQuery) {
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        if(querySet == null) {
            System.out.println("Malformed query, possibly related to a getMany or getAggregate function call.");
            System.out.println("The offending query was: " + SQLQuery);
            return null;
        }

        ArrayList<T> arrayOfObjects = new ArrayList<>();
        try {
            while (querySet.next()) {
                arrayOfObjects.add(objectFromQuerySet(castTo, describedTable, querySet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayOfObjects;
    }


    private <T> int getChildId(Class<T> parentClass, int parentId, String setter, Class<?> childClass) {
        try (ResultSet resultSet = sqlDriver.executeQueryIgnoreNoTable(readBuilder.getIdOfSubElement(setter, childClass.getSimpleName(), parentClass.getSimpleName(), parentId))) {
            if(resultSet != null) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            return -1;
        }
        return -1;
    }

    private <T> ArrayList<Integer> getChildIds(Class<T> parentClass, int parentId, String setter, Class<?> childClass) {
        ArrayList<Integer> ids = new ArrayList<>();
        try (ResultSet resultSet = sqlDriver.executeQueryIgnoreNoTable(readBuilder.getIdOfSubElement(setter, childClass.getSimpleName(), parentClass.getSimpleName(), parentId))) {
            if(resultSet != null) {
             while(resultSet.next()) {
                 ids.add(resultSet.getInt(1));
             }
             return ids;
            }
        } catch (SQLException e) {
            return ids;
        }
        return ids;
    }

    private int executeGetId(String executingString) {
        int id;
        try {
            ResultSet rs = sqlDriver.executeQuery(executingString);
            id = rs.getInt("id");
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    private SQLWriteObject createCopyNonPrimitives(SQLWriteObject completeObject) {
        SQLWriteObject simple = new SQLWriteObject(new HashMap<>(completeObject.getAttributeList()));
        simple.getAttributeList().entrySet().removeIf(entry -> !isSimple(entry.getValue().getData().getClass()));
        return simple;
    }

    public static String getObjectClass(SQLWriteObject writeObject) {
        return writeObject.getAttributeList().get("class").getInnerName();
    }

    public static boolean isCollection(Class c) {
        return Collection.class.isAssignableFrom(c) || Map.class.isAssignableFrom(c);
    }
}
