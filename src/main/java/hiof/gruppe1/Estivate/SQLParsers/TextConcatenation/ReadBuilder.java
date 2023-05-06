package hiof.gruppe1.Estivate.SQLParsers.TextConcatenation;

public class ReadBuilder {
    private final String SELECT = "SELECT ";
    private final String FROM = "FROM ";
    private final String SELECT_ALL_FROM = "SELECT * FROM ";
    private final String WHERE = " WHERE ";
    private final String ID_EQUALS = "id = ";
    private final String EQUALS = " = ";
    private final String AND = " AND ";
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
        return reader.toString();
    }

    public String getIdOfSubElement(String setter, String childName, String parentName, int parentID) {
         StringBuilder selector = new StringBuilder();
         selector.append(SELECT);
         selector.append(childName);
         selector.append(" ");
         selector.append(FROM);
         selector.append(parentName);
         selector.append("_has_");
         selector.append(childName);
         selector.append("\n");
         selector.append(WHERE);
         selector.append(parentName);
         selector.append(EQUALS);
         selector.append(parentID);
         selector.append(AND);
         selector.append("setter");
         selector.append(EQUALS);
         selector.append(String.format("'%s'", setter));
         return selector.toString();
    }
}
