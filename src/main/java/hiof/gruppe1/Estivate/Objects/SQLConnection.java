package hiof.gruppe1.Estivate.Objects;

import hiof.gruppe1.Estivate.EstivateCore.EstivatePersist;
import hiof.gruppe1.Estivate.objectBuilders.EstivateBuilder;

import java.net.URL;

public class SQLConnection {
    URL url;
    int port;
    String dbName;
    String userName;
    String password;
    EstivateBuilder callingBuilder;

    public SQLConnection() {
    }

    public SQLConnection(EstivateBuilder Builder) {
        this.callingBuilder = Builder;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public SQLConnection setPort(int port) {
        this.port = port;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public SQLConnection setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SQLConnection setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SQLConnection setPassword(String password) {
        this.password = password;
        return this;
    }

    public EstivatePersist build() {
        return callingBuilder.build();
    }
}
