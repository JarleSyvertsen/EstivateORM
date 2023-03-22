package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.HashMap;

public class EstivateMultiTransaction {
    SQLMultiCommand sqlMultiCommand;
    HashMap<String, SQLAttribute> results = new HashMap<>();

    public EstivateMultiTransaction getAggreagate() {
        sqlMultiCommand = new SQLMultiCommand<>();
        return this;
    }

    public EstivateMultiTransaction retrieveFormat(String format) {
        sqlMultiCommand.retrieveFormat(format);
        return this;
    }

    public <T> EstivateMultiTransaction count(Class<T> workingClass, String condition, String resultName) {
        // Create temporary SQLCommand, execute, return result to the result queue.
        return this;
    }

    public double result(String command) {
        // Not sure if its gonna work, buth mathX is a library for parsing strings and create
        // Arguments out of the SQLAttributes here
        Expression exp = new Expression(command);
        results.forEach((k, v) -> exp.addArguments(new Argument(k + " = " + v.getData())));
        return exp.calculate();
    }
}
