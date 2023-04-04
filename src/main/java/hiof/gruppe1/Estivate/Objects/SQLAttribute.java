package hiof.gruppe1.Estivate.Objects;

public class SQLAttribute {
    private Object object;
    private Class objectClass;
    public SQLAttribute(Class objectClass, Object object) {
        this.object = object;
        this.objectClass = objectClass;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) objectClass.cast(object);
    }
    public <T> T getDataType(Class<T> typeToCast) {
        return (T) typeToCast.cast(object);
    }
}
