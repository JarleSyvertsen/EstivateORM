package hiof.gruppe1.Estivate.Objects;

public class SQLAttribute<T> {
    private Object object;
    private Class objectClass;
    public SQLAttribute(Class objectClass, Object object) {
        this.object = object;
        this.objectClass = objectClass;
    }

    @SuppressWarnings("unchecked")
    public T getData() {
        return (T) objectClass.cast(object);
    }
}
