package hiof.gruppe1.Estivate.objectBuilders;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.Objects.SQLConnection;

public class EstivateBuilder {
    SQLConnection connection = new SQLConnection(this);
    public EstivateBuilder() {
    }
    public SQLConnection initializeSQLConn() {
        return connection;
    }
    public EstivatePersist build() {
        return null;
    }
}
