package hiof.gruppe1.Estivate.objectBuilders;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.Objects.SQLConnection;

public class EstivateBuilder {
    private String relativeURL = "EstivateDB.db";
    private Boolean debug = false;
    private final SQLConnection connection = new SQLConnection(this);

    /**
     * Object used to initialize EstivatePersist elements.
     * Provides a default EstivateDB.db (SQLLite based) backend when called with no parameters.
     */
    public EstivateBuilder() {
    }

    /**
     * Allows for setting a custom URL for the connection, in the case of a file-backed database
     * This is an URI/Filepath to the file itself.
     * @param relativeURL URL if using a preconfigured database, Filepath in the case of file-based database (default).
     * @return EstivateBuilder
     */
    public EstivateBuilder setDBUrl(String relativeURL) {
        this.relativeURL = relativeURL;
        return this;
    }

    /**
     * Allows for setting debug mode. Debug mode outputs the SQL strings as they are executed.
     * @param debug true if enabled, false by default.
     * @return EstivateBuilder
     */
    public EstivateBuilder setDebug(Boolean debug) {
        this.debug = debug;
        return this;
    }
    /**
     * Creates a connection and returns it with current values. Usually build is preferred for most
     * use-cases.
     * @return SQLConnection
     */
    public SQLConnection initializeSQLConn() {
        return connection;
    }

    /**
     * Creates a connection and returns a EstivatePersist object which allows
     * utilizing the main functions of the software.
     * @return EstivatePersist
     */
    public EstivatePersist build() {
       return new EstivatePersist(relativeURL, debug);
    }
}
