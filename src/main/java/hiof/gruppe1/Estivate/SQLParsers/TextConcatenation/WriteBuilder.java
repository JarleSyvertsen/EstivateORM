package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static hiof.gruppe1.Estivate.SQLParsers.TextConcatenation.SQLParserTextConcatenation.getObjectClass;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class WriteBuilder {
    private final String INSERT_INTO = "INSERT OR REPLACE INTO ";
    private final String VALUES = " VALUES ";
    private final TextConcatTableManagement tableManagement;
    private final IObjectParser objectParser;
    IDriverHandler driver;

    public WriteBuilder(IDriverHandler driver, IObjectParser objectParser) {
        this.driver = driver;
        this.tableManagement = new TextConcatTableManagement(driver);
        this.objectParser = objectParser;
    }

    String createWritableSQLString(SQLWriteObject writeObject) {
        tableManagement.createOrResizeTableIfNeeded(writeObject);

        if (writeObject.getAttributeList().get("id").getData().toString().equals("0")) {
            writeObject.getAttributeList().remove("id");
        }

        String tableName = writeObject.getAttributeList().get("class").getInnerName();

        String insertTable = getObjectClass(writeObject);
        writeObject.getAttributeList().remove("class");

        // PartBuilders to allow building the String in a non-linear way.
        // Remove the complex objects after parsing.

        SQLWriteObject writeObjectSimple = new SQLWriteObject();
        writeObjectSimple.setAttributes((HashMap<String, SQLAttribute>) writeObject.getAttributeList().clone());
        writeObjectSimple.getAttributeList().entrySet().removeIf(entry -> !isSimple(entry.getValue().getData().getClass()));

        String finalString = createInsertStatement(tableName, insertTable, writeObjectSimple);

        int parentId = executeGetId(tableName, finalString);
        traverseNonPrimitives(writeObject, tableName, parentId);

        return String.valueOf(parentId);
    }

    private void traverseNonPrimitives(SQLWriteObject writeObject, String parentNameSimple, int parentId) {
        writeObject.getAttributeList().forEach((k, v) -> {
            if (!isSimple(v.getData().getClass())) {
                HashMap<String, SQLAttribute> parsedAttributes = objectParser.parseObjectToAttributeList(v.getDataRaw());
                SQLWriteObject recursiveObject = new SQLWriteObject(parsedAttributes);
                String objectClass = getObjectClass(recursiveObject);

                tableManagement.createAppendingTableIfMissing(parentNameSimple, objectClass, true);
                String childId = createWritableSQLString(recursiveObject);
                String recursiveRelationship = createRelationshipInsert(k, parentNameSimple, objectClass, String.valueOf(parentId), childId);
                driver.executeNoReturnSplit(recursiveRelationship);
            }
        });
    }

    private String createInsertStatement(String tableName, String insertTable, SQLWriteObject writeObjectSimple) {
        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();

        // Appends
        finalString.append(INSERT_INTO);
        finalString.append(insertTable);

        writeObjectSimple.getAttributeList().forEach((k, v) -> {
            keyString.append("\"");
            keyString.append(tableName);
            keyString.append("_");
            keyString.append(k);
            keyString.append("\"");
            keyString.append(",");
            valuesString.append(StringUtils.createWritableValue(v));
            valuesString.append(",");
        });

        finalString.append(StringUtils.createValuesInParenthesis(keyString));
        finalString.append(VALUES);
        finalString.append(StringUtils.createValuesInParenthesis(valuesString));
        finalString.append(" RETURNING ");
        finalString.append(tableName);
        finalString.append("_");
        finalString.append("id");
        finalString.append(";");

        return finalString.toString();
    }

    private int executeGetId(String tableName, String executingString) {
        int id;
        try {
            ResultSet rs = driver.executeQuery(executingString);
            id = rs.getInt(tableName + "_" + "id");
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }


    private String createRelationshipInsert(String setter, String parentName, String childName, String parentId, String childId) {
        StringBuilder relationshipInsert = new StringBuilder();
        ArrayList<String> keyValues = new ArrayList<>();

        keyValues.add(parentName);
        keyValues.add(childName);
        keyValues.add("setter");
        StringBuilder keys = StringUtils.createCommaValues(keyValues);

        ArrayList<String> values = new ArrayList<>();
        values.add(parentId);
        values.add(childId);
        values.add(setter);
        StringBuilder csvValues = StringUtils.createCommaValues(values);

        relationshipInsert.append("\n");
        relationshipInsert.append(INSERT_INTO);
        relationshipInsert.append(parentName);
        relationshipInsert.append("_has_");
        relationshipInsert.append(childName);
        relationshipInsert.append(StringUtils.createValuesInParenthesis(keys));
        relationshipInsert.append(" ");
        relationshipInsert.append(VALUES);
        relationshipInsert.append(StringUtils.createValuesInParenthesis(csvValues));

        return relationshipInsert.toString();
    }
}
