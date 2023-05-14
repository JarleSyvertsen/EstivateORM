package hiof.gruppe1.Estivate.Objects;

import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SQLQueryBase {

    protected ISQLParser parser;
    protected String primaryTable;
    protected ArrayList<String> selectAttributes = new ArrayList<>();
    protected Queue<String> whereStatements = new LinkedList<>();
    protected Class<?> outputClass;
    protected String outputFormat;

    protected String createFullQuery() {
        final String SELECT = "SELECT ";
        final String FROM = "FROM ";
        final String WHERE = "WHERE ";
        final String AND = " AND ";


        StringBuilder fullQuery = new StringBuilder();
        if(!selectAttributes.isEmpty()) {
            fullQuery.append(SELECT);
            selectAttributes.forEach((selector) -> fullQuery.append(selector).append(","));
            fullQuery.deleteCharAt(fullQuery.length() - 1).append(" ");
            fullQuery.append(FROM).append(primaryTable);
        }

        if(!whereStatements.isEmpty()) {
            fullQuery.append(WHERE);
            whereStatements.forEach((statement) -> fullQuery.append(statement).append(AND));
            for(int i = 0; i < AND.length(); i++) {
                fullQuery.deleteCharAt(fullQuery.length() - 1);
            }
        }

        return fullQuery.toString();
    }

}
