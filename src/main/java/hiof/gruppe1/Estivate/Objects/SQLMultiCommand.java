package hiof.gruppe1.Estivate.Objects;

import java.util.ArrayList;
import java.util.Queue;
// Backend command object to be parsed. Built via the "frontend" EstivateMultiTransaction.
public class SQLMultiCommand<T> {
    private ArrayList<String> selectAttributes;
    private String primaryTable;
    private Queue<String> whereStatements;
    private Class<T> outputClass;
    private String outputFormat;

    public SQLMultiCommand() {

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
    public void setPrimaryTable(String primaryTable) {
        this.primaryTable = primaryTable;
    }
    public void addCondition(String condition) {
        whereStatements.add(condition);
    }

}
