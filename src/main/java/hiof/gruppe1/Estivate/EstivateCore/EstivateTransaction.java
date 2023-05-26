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
     * Counts the given rows of a query, and stores the result with the name as defined in the resultName parameter.
     * This result can then be referred to in the result function via the resultName.
     * The class parameter is used to find which table to search (can be changed via config), and the string condition allows users to append conditions to use when fetching in the given table.
     * @param queryTable The class object which relates to the table one which to query.
     * @param condition A SQL formatted condition to narrow down the queried rows, can be NULL. WHERE is automatically added.
     * @param resultName The name of the stored variable for later reference by .result
     * @return EstivateMultiTransaction
     */
    public <T> EstivateTransaction count(Class<T> queryTable, String condition, String resultName) {
        SQLMultiCommand temp = new SQLMultiCommand(driverHandler);
        temp.addSelect("count(*)");
        temp.retrieveClass(queryTable);
        temp.addCondition(condition);
        results.put(resultName, new SQLAttribute(int.class, temp.getIntValue()));
        return this;
    }

    /**
     * Sums the value of a given table using the sumField parameter to define which column to search.
     * The parameter queryTable is used to find which table to query, additionally using condition to restrict the amount of rows used in the result.
     * The sum is stored into resultName, for later use in the result function.
     * @param queryTable The class object which relates to the table one which to query.
     * @param condition A SQL formatted condition to narrow down the queried rows, can be NULL. WHERE is automatically added.
     * @param sumColumn The name of the column the sum operation is applied on, will  mirror the class attribute unless overwritten.
     * @param resultName The name of the stored variable for later reference by .result
     * @return EstivateMultiTransaction
     */
    public <T> EstivateTransaction sum(Class<T> queryTable, String condition, String sumColumn, String resultName) {
        SQLMultiCommand temp = new SQLMultiCommand(driverHandler);
        temp.addSelect(String.format("sum(%s)", sumColumn));
        temp.retrieveClass(queryTable);
        temp.addCondition(condition);
        results.put(resultName, new SQLAttribute(int.class, temp.getIntValue()));
        return this;
    }

    /**
     * Takes a string in the form of mathematical equation.
     * The variables as defined in sum/count can be employed here.
     * The result of the operation is returned as a single value.
     * @param equation A user defined equation using the variables produced by sum/count.
     * @return double
     */
    public double result(String equation) {
        Expression exp = new Expression(equation);
        results.forEach((k, v) -> exp.addArguments(new Argument(k + " = " + v.getDataRaw())));
        return exp.calculate();
    }
}
