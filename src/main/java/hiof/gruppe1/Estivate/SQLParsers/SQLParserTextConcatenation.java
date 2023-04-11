package hiof.gruppe1.Estivate.SQLParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLAdapters.SQLTableCalculations;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import static hiof.gruppe1.Estivate.SQLAdapters.TableDialectAttributeAdapter.getCompatAttr;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SQLParserTextConcatenation implements ISQLParser {
    private final String SELECT = "SELECT ";
    private final String SELECT_ALL_FROM = "SELECT * FROM ";
    private final String INSERT_INTO = "INSERT INTO ";
    private final String VALUES = " VALUES ";
    private final String WHERE = " WHERE ";
    private final String ID_EQUALS = "id = ";

    IDriverHandler sqlDriver;
    IObjectParser objectParser;
    SQLTableCalculations tableManagement;

    public SQLParserTextConcatenation(IDriverHandler sqlDriver) {
        this.sqlDriver = sqlDriver;
        this.objectParser = new ReflectionParser();
        this.tableManagement = new SQLTableCalculations(sqlDriver);
    }

    public Boolean writeToDatabase(SQLMultiCommand multiCommand) {
        return false;
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        if (!tableManagement.insertIsTableCorrect(writeObject)) {
            tableManagement.createTable(writeObject);
        }
        String writeableString = createWritableSQLString(writeObject);
        sqlDriver.executeInsert(writeableString);
        return true;
    }

    public <T> T readFromDatabase(Class<T> castTo, int id) {
        String SQLQuery = createReadableSQLString(castTo, id);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        HashMap<String, SQLAttribute> readAttributes = getAttributeMap(describedTable, querySet);
        return objectParser.parseAttributeListToObject(castTo, readAttributes);
    }

    public <T> ArrayList<T> readFromDatabase(Class<T> castTo) {
        String SQLQuery = createReadableSQLString(castTo);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        ArrayList<T> arrayOfObjects = new ArrayList<>();
        try {
            while (querySet.next()) {
                T object = objectParser.parseAttributeListToObject(castTo, getAttributeMap(describedTable, querySet));
                arrayOfObjects.add(object);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return arrayOfObjects;
    }

    private String createReadableSQLString(Class queryClass, int id) {
        StringBuilder limiter = new StringBuilder();
        limiter.append(WHERE);
        limiter.append(ID_EQUALS);
        limiter.append(id);
        return createReadableSQLString(queryClass, limiter.toString());
    }

    private String createReadableSQLString(Class queryClass, String limiter) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        reader.append(limiter != null ? limiter : "");
        return reader.toString();
    }

    private String createReadableSQLString(Class queryClass) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        return reader.toString();
    }

    private String createWritableSQLString(SQLWriteObject writeObject) {
        if (writeObject.getAttributeList().get("id").getData().toString().equals("0")) {
            writeObject.getAttributeList().remove("id");
        }
        String insertTable = writeObject.getAttributeList().remove("class").getInnerClass();

        // PartBuilders to allow building the String in a non-linear way.
        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();
        StringBuilder recursiveAdds = traverseNonPrimitives(writeObject);
        // Remove the complex objects after parsing.
        writeObject.getAttributeList().entrySet().removeIf(entry -> !isSimple(entry.getValue().getData().getClass()));

        // Appends
        finalString.append(INSERT_INTO);
        finalString.append(insertTable);
        writeObject.getAttributeList().forEach((k, v) -> {
            keyString.append(k);
            keyString.append(",");
            valuesString.append(createWritableValue(v));
            valuesString.append(",");
        });

        createValuesInParenthesis(finalString, keyString);
        finalString.append(VALUES);
        createValuesInParenthesis(finalString, valuesString);
        finalString.append(" RETURNING id");
        //   finalString.append(recursiveAdds);

        return finalString.toString();
    }

    private StringBuilder traverseNonPrimitives(SQLWriteObject writeObject) {
        StringBuilder recursiveAdds = new StringBuilder();
        writeObject.getAttributeList().forEach((k, v) -> {
            if (!isSimple(v.getData().getClass())) {
                HashMap<String, SQLAttribute> parsedAttributes = objectParser.parseObjectToAttributeList(v.getDataRaw());
                SQLWriteObject recursiveObject = new SQLWriteObject(parsedAttributes);
                recursiveAdds.append(" ");
                recursiveAdds.append(createWritableSQLString(recursiveObject));
            }
        });
        return recursiveAdds;
    }

    private static void createValuesInParenthesis(StringBuilder finalString, StringBuilder keyString) {
        finalString.append("(");
        finalString.append(keyString);
        finalString.deleteCharAt(finalString.length() - 1);
        finalString.append(")");
    }

    private String createWritableValue(SQLAttribute sqlAttr) {
        if (sqlAttr.getData().getClass().getSimpleName().equals("String")) {
            return String.format("\"%s\"", sqlAttr.getData().toString());
        }
        return sqlAttr.getData().toString();
    }

    private static HashMap<String, SQLAttribute> getAttributeMap(HashMap<String, String> describedTable, ResultSet querySet) {
        HashMap<String, SQLAttribute> readAttributes = new HashMap<>();
        try {
            for (Map.Entry<String, String> entry : describedTable.entrySet()) {
                String attributeName = entry.getKey();
                String attributeValue = entry.getValue();
                switch (getCompatAttr(attributeValue)) {
                    case INT_COMPAT -> readAttributes.put(attributeName, new SQLAttribute(Integer.class, querySet.getInt(attributeName)));
                    case STRING_COMPAT -> readAttributes.put(attributeName, new SQLAttribute(String.class, querySet.getString(attributeName)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readAttributes;
    }


}
