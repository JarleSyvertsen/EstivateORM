package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import java.util.ArrayList;

public class WriteBuilder {
    private final String INSERT_INTO = "INSERT OR REPLACE INTO ";
    private final String VALUES = " VALUES ";

    String createInsertStatement(String insertTable, SQLWriteObject writeObjectSimple) {
        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();

        // Appends
        finalString.append(INSERT_INTO);
        finalString.append(insertTable);

        writeObjectSimple.getAttributeList().forEach((k, v) -> {
            keyString.append("\"");
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
        finalString.append("id");
        finalString.append(";");

        return finalString.toString();
    }


    String createRelationshipInsert(String setter, String parentName, String childName, String parentId, String childId) {
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
