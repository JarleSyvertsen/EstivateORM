package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import java.sql.ResultSet;
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

    public SQLParserTextConcatenation(IDriverHandler sqlDriver) {
        this.sqlDriver = sqlDriver;
        this.objectParser = new ReflectionParser();
    }

    public Boolean writeToDatabase(SQLMultiCommand multiCommand) {
        return false;
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        String writeableString = createWritableSQLString(writeObject);
        sqlDriver.executeInsert(writeableString);
        return true;
    }

    public <T> T readFromDatabase(Class<T> castTo, int id) {
        String SQLQuery = createRadableSQLString(castTo, id);
        HashMap<String, String> describedTable = sqlDriver.describeTable(castTo);
        ResultSet querySet = sqlDriver.executeQuery(SQLQuery);

        HashMap<String,SQLAttribute> readAttributes = new HashMap<>();
        try {
            for (Map.Entry<String, String> entry : describedTable.entrySet()) {
                String attributeName = entry.getKey();
                String attributeValue = entry.getValue();
                switch (attributeValue) {
                    case "INTEGER" -> readAttributes.put(attributeName, new SQLAttribute(Integer.class, querySet.getInt(attributeName)));
                    case "TEXT" -> readAttributes.put(attributeName, new SQLAttribute(String.class, querySet.getString(attributeName)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return objectParser.parseAttributeListToObject(castTo, readAttributes);
    }

    private String createRadableSQLString(Class queryClass, int id) {
        StringBuilder limiter = new StringBuilder();
        limiter.append(WHERE);
        limiter.append(ID_EQUALS);
        limiter.append(id);
        return createRadableSQLString(queryClass, limiter.toString());
    }
    private String createRadableSQLString(Class queryClass, String limiter) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        reader.append(limiter != null ? limiter : "");
        return reader.toString();
    }

    private String createWritableSQLString(SQLWriteObject writeObject) {
        if(writeObject.getAttributeList().get("id").getData().toString().equals("-1")) {
         writeObject.getAttributeList().remove("id");
        }

        String insertTable = writeObject.getAttributeList().remove("class").getInnerClass();

        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();

        finalString.append(INSERT_INTO);
        finalString.append(insertTable);

        writeObject.getAttributeList().forEach((k,v) -> {
            keyString.append(k);
            keyString.append(",");
            valuesString.append(createWritableValue(v));
            valuesString.append(",");
        });

        createValuesInParenthesis(finalString, keyString);
        finalString.append(VALUES);
        createValuesInParenthesis(finalString, valuesString);
        finalString.append(" RETURNING id");
        return finalString.toString();
    }

    private static void createValuesInParenthesis(StringBuilder finalString, StringBuilder keyString) {
        finalString.append("(");
        finalString.append(keyString);
        finalString.deleteCharAt(finalString.length() - 1);
        finalString.append(")");
    }

    private String createWritableValue(SQLAttribute sqlAttr) {
        if(sqlAttr.getData().getClass().getSimpleName().equals("String")) {
            return String.format("\"%s\"", sqlAttr.getData().toString());
        }
        return sqlAttr.getData().toString();
    }

}
