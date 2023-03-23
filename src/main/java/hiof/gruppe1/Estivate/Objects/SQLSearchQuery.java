package hiof.gruppe1.Estivate.Objects;

import java.util.ArrayList;
import java.util.Queue;

// Backend command object to be parsed. Built via the "frontend" EstivateMultiTransaction.
public class SQLSearchQuery<T> {
    private ArrayList<String> selectAttributes;
    private Queue<String> whereStatements;
    private Class<T> outputClass;
    private String outputFormat;

    public SQLSearchQuery() {

    }
    public SQLSearchQuery(Class<T> outputClass) {
        this.outputClass = outputClass;
    }

    public void retrieveClass(Class<T> outputClass) {
        this.outputClass = outputClass;
    }

    public void retrieveFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void addSelect(String selector) {
        selectAttributes.add(selector);
    }
    public void addCondition(String condition) {
        whereStatements.add(condition);
    }

    public <T> T execute() {
        return null;
    }
}
