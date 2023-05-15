package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.HashMap;

/**
 * Multitransaction is a secondary namespace where transactions that spans across tables is supported. In addition, functions pertaining to aggregate data is located here.
 */
public class EstivateTransaction {
    SQLMultiCommand sqlMultiCommand;
    IDriverHandler driverHandler;

    public EstivateTransaction(IDriverHandler driverHandler) {
        this.driverHandler = driverHandler;
    }

    HashMap<String, SQLAttribute> results = new HashMap<>();

    /**
     * getAggregate serves as a way to initialize a new transaction, and is thus preferred when starting a transaction of this kind.
     *
     * @return EstivateAggregateTransaction
     */

    /**
     * If the function generates more than one result, users can define which data structure to return. If none is defined, an Array is used as default.
     *
     * @param format (LinkedList, ArrayList, HashMap, Array, HashSet)
     * @return EstivateMultiTransaction
     */
    public EstivateTransaction retrieveFormat(String format) {
        sqlMultiCommand.retrieveFormat(format);
        return this;
    }

    /**
     * Counts the given rows of a query, and stores is into an internal result with the name given in resultName. This name can then be refered to via the result function later. The class parameter is used to find which table to search (can be changed via config), and the string condition allows users to append conditions to use when fetching in the given table.
     *
     * @param tableClass
     * @param condition
     * @param resultName
     * @param <T>
     * @return EstivateMultiTransaction
     */
    public <T> EstivateTransaction count(Class<T> tableClass, String condition, String resultName) {
        SQLMultiCommand temp = new SQLMultiCommand(driverHandler);
        temp.addSelect("count(*)");
        temp.retrieveClass(tableClass);
        temp.addCondition(condition);
        results.put(resultName, new SQLAttribute(int.class, temp.getIntValue()));
        return this;
    }

    /**
     * Sums the value of a given table using the sumField parameter to define which column to search. The parameter tableClass is used to find which table to query, additionally using condition to restrict the amount of rows used in the result. The sum is stored into resultName, for later use in the result function.
     *
     * @param tableClass
     * @param condition
     * @param sumColumn
     * @param resultName
     * @param <T>
     * @return EstivateMultiTransaction
     */
    public <T> EstivateTransaction sum(Class<T> tableClass, String condition, String sumColumn, String resultName) {
        SQLMultiCommand temp = new SQLMultiCommand(driverHandler);
        temp.addSelect(String.format("sum(%s)", sumColumn));
        temp.retrieveClass(tableClass);
        temp.addCondition(condition);
        results.put(resultName, new SQLAttribute(int.class, temp.getIntValue()));
        return this;
    }

    /**
     * Takes a string in the form of mathematical equation. Here, the user refer to the resultNames previously defined. The result of the operation is returned as a single value.
     *
     * @param command
     * @return Double result
     */
    public double result(String command) {
        // Not sure if its gonna work, buth mathX is a library for parsing strings and create
        // Arguments out of the SQLAttributes here
        Expression exp = new Expression(command);

        results.forEach((k, v) -> exp.addArguments(new Argument(k + " = " + v.getDataRaw())));
        return exp.calculate();
    }
}
