package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLSearchQuery;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;
import hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation;
import hiof.gruppe1.Estivate.config.config;
import hiof.gruppe1.Estivate.drivers.IDriverHandler;
import hiof.gruppe1.Estivate.drivers.SQLiteDriver;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Persist serves as the main namespace, where storing of Java objects and retrieval is supported.
 */
public class EstivatePersist {

    // Default, could be defined by config if multiple parsers are present.
    private IObjectParser objectParser;
    private ISQLParser SQLParser;
    private config WorkingConfiguration;
    private IDriverHandler sqlDriver;
   public EstivatePersist(String relativeURL) {
        this.objectParser  = new ReflectionParser();
        this.sqlDriver = new SQLiteDriver(relativeURL);
        this.SQLParser = new SQLParserTextConcatenation(sqlDriver);
        WorkingConfiguration  = new config();
    }

    /**
     * Persist serves as the primary function to store arbitary objects to database. The object will be divided into primitives (stored in a table with the same name, unless overwritten via config), and embedded objects will be stored in their respective table, with a many-to-many relationship by default.
     * @param object
     * @return Boolean
     */
    public Boolean persist(Object object) {
        HashMap<String, SQLAttribute> attributes = objectParser.parseObjectToAttributeList(object);
        SQLWriteObject writeObject = new SQLWriteObject();
        writeObject.setAttributes(attributes);
        SQLParser.writeToDatabase(writeObject);
        return true;
    }

    /**
     * getOne allows for fetching singular objects when the ID of the object is known. A class object is provided both to determine which class to cast to, but also which table to query. This class-table association can be overwritten in config if needed.
     * @param id
     * @param output
     * @return Object of type <T>
     * @param <T>
     *
     */
    public <T> T getOne(int id, Class<T> output) {
        return SQLParser.readFromDatabase(output, id);
    }

    /**
     * getAll allows one to fetch all elements of a given class. The given class is used to determine which table to query, if this is insufficent, this association can be overwritten in config.
     * @param output
     * @return ArrayList<Class>
     * @param <T>
     */
    public <T> ArrayList<T> getAll(Class<T> output) {
        return null;
    }

    /**
     *
     Get many serves as the primary function for more complex queries, built up via a builder-pattern of commands registered on the SQLSearchQuery object. Each object that is fetched through this function will be cast to the given class and stored in a new object of the given format.
     * @return SQLSearchQuery queries
     * @param <T>
     */
    public <T> SQLSearchQuery getMany() {
        return null;
    }

    /**
     * Used when the user want to make use of the SQL transaction to wrap multiple commands into a batch, and atomically resolve all operations together.
     * @return EstivateTransaction
     */
    public EstivateTransaction startTransaction() {
        return new EstivateTransaction().startTransaction();
    }
}

