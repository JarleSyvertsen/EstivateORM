package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;

import java.util.ArrayList;

public class EstivateMultiTransaction {
    SQLMultiCommand sqlMultiCommand;
    ArrayList<SQLAttribute> results;

    public EstivateMultiTransaction getAggreagate() {
        sqlMultiCommand = new SQLMultiCommand<>();
        return this;
    }

    public EstivateMultiTransaction retrieveFormat(String format) {
        sqlMultiCommand.retrieveFormat(format);
        return this;
    }

    public <T> EstivateMultiTransaction count(Class<T> workingClass, String condition, String resultName) {
        // Create temporary SQLCommand, execute, return result to the result arraylist.
        return this;
    }

    public int result(String command) {
        // Need to programmatically parse the string, might have to change this interface to something less useful, but easier to implement.
        return 0;
    }
}
