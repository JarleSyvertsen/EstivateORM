package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;

import java.util.Queue;

public class EstivateMultiTransaction {
    SQLMultiCommand sqlMultiCommand;
    Queue<SQLAttribute> results;
    int finalResult = 0;

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

    public void result(String command) {
        // Implement supporting 2 and 2 operations.
        // Could mean we simply use mathParser/A switch to calculate the last two results.
        // And require the user to daisy-chain these results via multiple calls to the result and to counts etc.
        // Probably the most likely solution.

        // Implement as is
        // Need to programmatically parse the string, might have to change this interface to something less useful, but easier to implement.
        // Might use math parser for the logic, but we still need to pull out the arguments, find them in result, and call the expression
        // function with a varying amount of variables.
        finalResult = 0;
    }
}
