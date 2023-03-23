package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLSearchQuery;
import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;
import hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation;
import hiof.gruppe1.Estivate.config.config;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import java.sql.Connection;
import java.util.ArrayList;

public class EstivatePersist {
    // Default, could be defined by config if multiple parsers are present.
    private IObjectParser objectParser = new ReflectionParser();
    private ISQLParser SQLParser = new SQLParserTextConcatenation();
    private config WorkingConfiguration = new config();
    private Connection connection;

    public Boolean persist(Object object) {
        return true;
    }

    public <T> T getOne(int id, Class<T> output) {
        return null;
    }

    public <T> ArrayList<T> getAll(Class<T> output) {
        return null;
    }

    // Meant to be used as a builderPattern, where a SQLMulticommand is used to create a more complex SQL query.
    // The format is in this case defined via .retrieveObject
    public <T> SQLSearchQuery getMany() {
        return null;
    }

    public EstivateTransaction startTransaction() {
        return new EstivateTransaction().startTransaction();
    }
}

