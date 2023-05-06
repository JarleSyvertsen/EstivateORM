package hiof.gruppe1.Estivate.SQLParsers;

import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

import java.util.ArrayList;

public interface ISQLParser {
    public Boolean writeToDatabase(SQLWriteObject writeObject);
    public <T> T readFromDatabase(Class<T> castTo, int id);
    public <T> ArrayList<T> readFromDatabase(Class<T> castTo);
    public <T> ArrayList<T> readFromDatabase(Class<T> castTo, String conditional);
}
