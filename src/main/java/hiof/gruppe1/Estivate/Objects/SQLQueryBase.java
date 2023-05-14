package hiof.gruppe1.Estivate.Objects;

import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SQLQueryBase {

    private final String SELECT = "SELECT ";
    private final String FROM = "FROM ";
    protected ISQLParser parser;
    protected String primaryTable;
    protected ArrayList<String> selectAttributes = new ArrayList<>();
    protected Queue<String> whereStatements = new LinkedList<>();
    protected Class<?> outputClass;
    protected String outputFormat;

    protected String createFullQuery() {
        StringBuilder fullQuery = new StringBuilder();
        if(!selectAttributes.isEmpty()) {
            fullQuery.append(SELECT);
            selectAttributes.forEach((selector) -> fullQuery.append(selector).append(","));
            fullQuery.deleteCharAt(fullQuery.length() - 1).append(" ");
            fullQuery.append(FROM).append(primaryTable);
        }

        if(!whereStatements.isEmpty()) {
            fullQuery.append("WHERE ");
            whereStatements.forEach((statement) -> fullQuery.append(statement).append(" AND "));
            int lengthOfLast = 5;

            for(int i = 0; i < lengthOfLast; i++) {
                fullQuery.deleteCharAt(fullQuery.length() - 1);
            }
        }

        return fullQuery.toString();
    }

}
