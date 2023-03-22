package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;
import hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

public class EstivatePersist {
    // Default, could be defined by config if multiple parsers are present.
    IObjectParser objectParser = new ReflectionParser();
    ISQLParser SQLParser = new SQLParserTextConcatenation();

    public Boolean persist(Object object) {
        return true;
    }
    public <T> T getOne(int id, Class<T> output) {return null; }
    public <T> T getAll(Class<T> output) {return null; }
    // Meant to be used as a builderPattern, where a SQLMulticommand is used to create a more complex SQL query.
    // The format is in this case defined via .retrieveObject
    public <T> T getMany() {return null;}

}

