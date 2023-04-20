package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

public class ReadBuilder {
    private final String SELECT_ALL_FROM = "SELECT * FROM ";
    private final String WHERE = " WHERE ";
    private final String ID_EQUALS = "id = ";
    protected String createReadableSQLString(Class queryClass, int id) {
        StringBuilder limiter = new StringBuilder();
        limiter.append(WHERE);
        limiter.append(ID_EQUALS);
        limiter.append(id);
        return createReadableSQLString(queryClass, limiter.toString());
    }

    protected String createReadableSQLString(Class queryClass, String limiter) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        reader.append(limiter != null ? limiter : "");
        return reader.toString();
    }

    protected String createReadableSQLString(Class queryClass) {
        StringBuilder reader = new StringBuilder();
        reader.append(SELECT_ALL_FROM);
        reader.append(queryClass.getSimpleName());
        reader.append(" ");
        //  reader.append(tableManagement.createJoiningTables(queryClass.getSimpleName()));
        return reader.toString();
    }
}
