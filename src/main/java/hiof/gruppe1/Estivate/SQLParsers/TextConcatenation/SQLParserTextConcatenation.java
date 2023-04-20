package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.getCompatAttr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SQLParserTextConcatenation implements ISQLParser {
    private final IDriverHandler sqlDriver;
    private final IObjectParser objectParser;
    private final WriteBuilder writeBuilder;
    private final ReadBuilder readBuilder;

    public SQLParserTextConcatenation(IDriverHandler sqlDriver) {
        this.sqlDriver = sqlDriver;
        this.objectParser = new ReflectionParser();
        this.writeBuilder = new WriteBuilder(sqlDriver, objectParser);
        this.readBuilder = new ReadBuilder();
    }

    public static String getObjectClass(SQLWriteObject writeObject) {
        return writeObject.getAttributeList().get("class").getInnerClass();
    }

    public Boolean writeToDatabase(SQLMultiCommand multiCommand) {
        return false;
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        String writeableString = writeBuilder.createWritableSQLString(writeObject);
        sqlDriver.executeNoReturnSplit(writeableString);
        return true;
    }

    public <T> T readFromDatabase(Class<T> castTo, int id) {
        String SQLQuery = readBuilder.createReadableSQLString(castTo, id);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
   //     ArrayList<String> relatedTables = tableManagement.getRelatedTables(castTo.getSimpleName());
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        HashMap<String, SQLAttribute> readAttributes = getAttributeMap(describedTable, querySet);
        return objectParser.parseAttributeListToObject(castTo, readAttributes);
    }

    public <T> ArrayList<T> readFromDatabase(Class<T> castTo) {
        String SQLQuery = readBuilder.createReadableSQLString(castTo);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);

        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        ArrayList<T> arrayOfObjects = new ArrayList<>();
        try {
            while (querySet.next()) {
                T object = objectParser.parseAttributeListToObject(castTo, getAttributeMap(describedTable, querySet));
                objectParser.getSubElementList(object);
                arrayOfObjects.add(object);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayOfObjects;
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

    private HashMap<String, SQLAttribute> getAttributeMap(Class topLevelCast, ArrayList<String> RelatedTables, HashMap<String, String> describedTable, ResultSet querySet) {
        HashMap<String, SQLAttribute> readAttributes = getAttributeMap(describedTable, querySet);
        for(String relatedTable : RelatedTables) {
            HashMap<String, SQLAttribute> subElementMap = getAttributeMap(sqlDriver.describeTable(relatedTable), querySet);
            try {
                String topLevelCastingElement = topLevelCast.getName();
                String fullPath = topLevelCastingElement.substring(0, topLevelCastingElement.lastIndexOf(".") + 1);
                Class<?> castingClass = Class.forName(fullPath + relatedTable);
                readAttributes.put(StringUtils.getStringFromQuerySet("setter", querySet),  new SQLAttribute(castingClass,objectParser.parseAttributeListToObject(castingClass, subElementMap)));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        return readAttributes;
    }

}
