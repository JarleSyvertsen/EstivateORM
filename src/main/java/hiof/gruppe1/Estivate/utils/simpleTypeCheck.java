package hiof.gruppe1.Estivate.utils;

public class simpleTypeCheck {
    public static boolean isSimple(Class<?> type) {
        return (type.isPrimitive() && type != void.class)
                || type == Double.class
                || type == Float.class
                || type == Long.class
                || type == Integer.class
                || type == Short.class
                || type == Character.class
                || type == Byte.class
                || type == Boolean.class
                || type == String.class;
    }
}
