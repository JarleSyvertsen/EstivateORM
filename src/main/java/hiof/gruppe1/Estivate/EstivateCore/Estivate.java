package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.SQLParsers.ISQLParser;
import hiof.gruppe1.Estivate.SQLParsers.SQLParserTextConcatenation;
import hiof.gruppe1.Estivate.objectParsers.IObjectParser;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import java.util.HashMap;

public class Estivate {
    // Default, could be defined by config if multiple parsers are present.
    IObjectParser objectParser = new ReflectionParser();
    ISQLParser SQLParser = new SQLParserTextConcatenation();

    public Boolean persist(Object object) {
        return true;
    }
}
