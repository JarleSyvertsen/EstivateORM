package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLAdapters.SQLTableCalculations;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;

import java.util.ArrayList;
import java.util.HashMap;

import static hiof.gruppe1.Estivate.SQLParsers.TextConcatenation.SQLParserTextConcatenation.getObjectClass;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class WriteBuilder {
    private final String SELECT = "SELECT ";
    private final String FROM = "FROM ";
    private final String INSERT_INTO = "INSERT OR REPLACE INTO ";
    private final String VALUES = " VALUES ";
    private final String SET_PARENT = "\nUPDATE tempRelations SET parent = last_insert_rowid();";
    private final String SET_CHILD = "\nUPDATE tempRelations SET child = last_insert_rowid();";
    private final SQLTableCalculations tableManagement;
    private final IObjectParser objectParser;

    public WriteBuilder(IDriverHandler driver, IObjectParser objectParser) {
        this.tableManagement = new SQLTableCalculations(driver);
        this.objectParser = objectParser;
    }

    String createWritableSQLString(SQLWriteObject writeObject) {
        tableManagement.createOrResizeTableIfNeeded(writeObject);
        String tableName = writeObject.getAttributeList().get("class").getInnerClass();
        if (writeObject.getAttributeList().get("id").getData().toString().equals("0")) {
            writeObject.getAttributeList().remove("id");
        }

        String insertTable = getObjectClass(writeObject);
        writeObject.getAttributeList().remove("class");

        // PartBuilders to allow building the String in a non-linear way.
        StringBuilder finalString = new StringBuilder();
        StringBuilder keyString = new StringBuilder();
        StringBuilder valuesString = new StringBuilder();
        StringBuilder recursiveAdds = traverseNonPrimitives(writeObject, insertTable);
        // Remove the complex objects after parsing.
        writeObject.getAttributeList().entrySet().removeIf(entry -> !isSimple(entry.getValue().getData().getClass()));

        if(!recursiveAdds.isEmpty()) {
            String TEMP_JOINING_TABLE = "CREATE TEMP TABLE IF NOT EXISTS tempRelations (Id PRIMARY KEY, parent INTEGER, child INTEGER); \n" +
                    "INSERT OR IGNORE INTO tempRelations VALUES (0,0,0); \n";
            finalString.append(TEMP_JOINING_TABLE);
        }

        // Appends
        finalString.append(INSERT_INTO);
        finalString.append(insertTable);

        writeObject.getAttributeList().forEach((k, v) -> {
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
        finalString.append(";");
        if(!recursiveAdds.isEmpty()) {
            finalString.append(SET_PARENT);
        }
        finalString.append(recursiveAdds);

        return finalString.toString();
    }

    private StringBuilder traverseNonPrimitives(SQLWriteObject writeObject, String parentClass) {
        StringBuilder recursiveAdds = new StringBuilder();

        writeObject.getAttributeList().forEach((k, v) -> {
            if (!isSimple(v.getData().getClass())) {
                HashMap<String, SQLAttribute> parsedAttributes = objectParser.parseObjectToAttributeList(v.getDataRaw());

                SQLWriteObject recursiveObject = new SQLWriteObject(parsedAttributes);
                String recursiveRelationship = createRelationshipInsert(k, parentClass, getObjectClass(recursiveObject));
                String appendingTable = tableManagement.createAppendingTableIfMissing(parentClass, recursiveObject);

                recursiveAdds.append("\n");
                recursiveAdds.append(appendingTable);
                recursiveAdds.append("\n");
                recursiveAdds.append(createWritableSQLString(recursiveObject));
                recursiveAdds.append(SET_CHILD);
                recursiveAdds.append(recursiveRelationship);
            }
        });
        return recursiveAdds;
    }

    private String createRelationshipInsert(String setter, String parentId, String childId) {
        StringBuilder relationshipInsert = new StringBuilder();
        ArrayList<String> keyValues = new ArrayList<>();
        keyValues.add(parentId);
        keyValues.add(childId);
        keyValues.add("setter");
        StringBuilder keys = StringUtils.createCommaValues(keyValues);

        relationshipInsert.append("\n");
        relationshipInsert.append(INSERT_INTO);
        relationshipInsert.append(parentId);
        relationshipInsert.append("_has_");
        relationshipInsert.append(childId);
        relationshipInsert.append(StringUtils.createValuesInParenthesis(keys));
        relationshipInsert.append(" ");
        relationshipInsert.append(SELECT);
        relationshipInsert.append("parent");
        relationshipInsert.append(",");
        relationshipInsert.append("child");
        relationshipInsert.append(",");
        relationshipInsert.append(String.format("\"%s\"", setter));
        relationshipInsert.append(" ");
        relationshipInsert.append(FROM);
        relationshipInsert.append("tempRelations");
        relationshipInsert.append(";");

        return relationshipInsert.toString();
    }
}
