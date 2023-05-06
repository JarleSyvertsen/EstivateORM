package hiof.gruppe1.Estivate.Objects;

import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;

import java.util.ArrayList;

// Backend command object to be parsed. Built via the "frontend" EstivateMultiTransaction.
public class SQLSearchQuery extends SQLQueryBase {
    public SQLSearchQuery(ISQLParser parser) {
        this.parser = parser;
    }
    public SQLSearchQuery addCondition(String condition) {
        whereStatements.add(condition);
        return this;
    }
    public <T> ArrayList<T> asArrayList(Class<T> castTo) {
        return parser.readFromDatabase(castTo, createFullQuery());
    }

}
