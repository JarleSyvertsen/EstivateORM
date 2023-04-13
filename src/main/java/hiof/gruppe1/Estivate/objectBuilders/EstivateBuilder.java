package hiof.gruppe1.Estivate.objectBuilders;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.Objects.SQLConnection;

public class EstivateBuilder {
    private String relativeURL;
    private Boolean debug = false;
    private SQLConnection connection = new SQLConnection(this);
    public EstivateBuilder() {
    }
    public EstivateBuilder setDBUrl(String relativeURL) {
        this.relativeURL = relativeURL;
        return this;
    }

    public EstivateBuilder setDebug(Boolean debug) {
        this.debug = debug;
        return this;
    }

    public SQLConnection initializeSQLConn() {
        return connection;
    }
    public EstivatePersist build() {
       return new EstivatePersist(relativeURL, debug);
    }
}
