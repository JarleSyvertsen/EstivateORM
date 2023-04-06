package hiof.gruppe1.Estivate.SQLParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

import java.sql.Connection;

public class SQLParserTextConcatenation implements ISQLParser {
    Connection connection;

    public Boolean writeToDatabase(SQLMultiCommand multiCommand) {
        return false;
    }

    public Boolean writeToDatabase(SQLWriteObject writeObject) {
        parseWriteObjectToDB(writeObject);
        return true;
    }
    // Should be private, just for debugging
    public String parseWriteObjectToDB(SQLWriteObject writeObject) {
        if(writeObject.getAttributeList().get("id").getData().toString().equals("-1")) {
         writeObject.getAttributeList().remove("id");
        }

        String insertInto = "INSERT INTO ";
        String insertTable = writeObject.getAttributeList().remove("class").getInnerClass();
        String values = " VALUES ";

        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();

        finalString.append(insertInto);
        finalString.append(insertTable);

        writeObject.getAttributeList().forEach((k,v) -> {
            keyString.append(k);
            keyString.append(",");
            valuesString.append(createWritableValue(v));
            valuesString.append(",");
        });

        createValuesInParenthesis(finalString, keyString);
        finalString.append(values);
        createValuesInParenthesis(finalString, valuesString);
        return finalString.toString();
    }

    private static void createValuesInParenthesis(StringBuilder finalString, StringBuilder keyString) {
        finalString.append("(");
        finalString.append(keyString);
        finalString.deleteCharAt(finalString.length() - 1);
        finalString.append(")");
    }

    public String createWritableValue(SQLAttribute sqlAttr) {
        if(sqlAttr.getData().getClass().getSimpleName().equals("String")) {
            return String.format("\"%s\"", sqlAttr.getData().toString());
        }
        return sqlAttr.getData().toString();
    }

}
