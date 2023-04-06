package hiof.gruppe1.Estivate.SQLAdapters;

import hiof.gruppe1.Estivate.Objects.SQLMultiCommand;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;

public interface ISQLParser {
    public Boolean writeToDatabase(SQLMultiCommand multiCommand);

    public Boolean writeToDatabase(SQLWriteObject writeObject);
    public <T> T readFromDatabase(Class<T> castTo, int id);
}
