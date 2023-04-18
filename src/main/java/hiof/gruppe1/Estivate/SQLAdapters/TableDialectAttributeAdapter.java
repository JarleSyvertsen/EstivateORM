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

    private static HashMap<String, SQLJavaAttr> SQLAttrToGeneric = populateEnumsAsValues();
    private static HashMap<Class, String> JavaClassToSQLite = populateClassToSQLite();

    private static HashMap<Class, String> populateClassToSQLite() {
        HashMap<Class, String> SQLiteKeys = new HashMap<>();
        SQLiteKeys.put(String.class, "TEXT");
        SQLiteKeys.put(Integer.class, "INTEGER");
        SQLiteKeys.put(Double.class, "DOUBLE");
        SQLiteKeys.put(Float.class, "FLOAT");
        SQLiteKeys.put(Boolean.class, "BOOLEAN");
        return SQLiteKeys;
    }

    private static HashMap<String, SQLJavaAttr> populateEnumsAsValues() {
        HashMap<String, SQLJavaAttr> SQLAttrAsKey = new HashMap<>();
        SQLAttrAsKey.put("TEXT", SQLJavaAttr.STRING_COMPAT);
        SQLAttrAsKey.put("INTEGER", SQLJavaAttr.INT_COMPAT);
        SQLAttrAsKey.put("DOUBLE", SQLJavaAttr.DOUBLE_COMPAT);
        SQLAttrAsKey.put("FLOAT", SQLJavaAttr.FLOAT_COMPAT);
        SQLAttrAsKey.put("BOOLEAN", SQLJavaAttr.BOOLEAN_COMPAT);

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
