package hiof.gruppe1.Estivate.Objects;

import hiof.gruppe1.Estivate.drivers.IDriverHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
// Backend command object to be parsed. Built via the "frontend" EstivateMultiTransaction.
public class SQLMultiCommand {
    private IDriverHandler driver;
    private ArrayList<String> selectAttributes = new ArrayList<>();
    private String primaryTable;
    private Queue<String> whereStatements = new LinkedList<>();
    private Class<?> outputClass;
    private String outputFormat;

    private final String SELECT = "SELECT ";
    private final String FROM = "FROM ";

    public String createFullQuery() {
        StringBuilder fullQuery = new StringBuilder();
        fullQuery.append(SELECT);
        selectAttributes.forEach((selector) -> fullQuery.append(selector).append(","));
        fullQuery.deleteCharAt(fullQuery.length() - 1).append(" ");

        fullQuery.append(FROM).append(primaryTable);

        if(!whereStatements.isEmpty()) {
            whereStatements.forEach((statement) -> fullQuery.append(statement).append(","));
            fullQuery.deleteCharAt(fullQuery.length() - 1);
        }

        return fullQuery.toString();
    }

    public SQLMultiCommand(IDriverHandler driver) {
        this.driver = driver;
    }

    public void retrieveClass(Class<?> outputClass) {
        if(primaryTable == null) {
            primaryTable = outputClass.getSimpleName();
        }
        this.outputClass = outputClass;
    }

    public void retrieveFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void addSelect(String selector) {
        selectAttributes.add(selector);
    }
    public void setPrimaryTable(String primaryTable) {
        this.primaryTable = primaryTable;
    }
    public void addCondition(String condition) {
        if(condition == null || condition.equals("")) {
            return;
        }

        whereStatements.add(condition);
    }

    public int getIntValue() {
        ResultSet rs = driver.executeQueryIgnoreNoTable(createFullQuery());
        try {
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
