package hiof.gruppe1.Estivate.SQLAdapters;

import java.util.HashMap;

public class TableDialectAttributeAdapter {
    // Definitions of all known SQL Attributes to a generic known Enum
    public enum SQLJavaAttr {
        STRING_COMPAT,
        INT_COMPAT,
        BOOLEAN_COMPAT,
        DOUBLE_COMPAT,
        FLOAT_COMPAT
    }

    private static HashMap<String, SQLJavaAttr> SQLAttrToGeneric = populateEnumsAsKeys();
    private static HashMap<Class, String> JavaClassToSQLite = populateSQLiteAsKeys();

    private static HashMap<Class, String> populateSQLiteAsKeys() {
        HashMap<Class, String> SQLiteKeys = new HashMap<>();
        SQLiteKeys.put(String.class, "TEXT");
        SQLiteKeys.put(Integer.class, "INTEGER");
        return SQLiteKeys;
    }

    private static HashMap<String, SQLJavaAttr> populateEnumsAsKeys() {
        HashMap<String, SQLJavaAttr> SQLAttrAsKey = new HashMap<>();
        SQLAttrAsKey.put("TEXT", SQLJavaAttr.STRING_COMPAT);
        SQLAttrAsKey.put("INTEGER", SQLJavaAttr.INT_COMPAT);
        return SQLAttrAsKey;
    }
    public static SQLJavaAttr getCompatAttr(String sqlAttr) {
        return SQLAttrToGeneric.get(sqlAttr);
    }
    public static String convertToSQLDialect(Class javaClass, String dialect) {
        switch (dialect) {
            case "sqlite":
                return JavaClassToSQLite.get(javaClass);
        }
        return null;
    }

}
