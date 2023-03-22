package hiof.gruppe1.Estivate.Objects;

public class SQLAttribute<T> {
    private final Class<T> data;
    private final T t;
    public SQLAttribute(Class<T> data, T t) {
        this.data = data;
        this.t = t;
    }

    public Class<T> getData() {
        return data;
    }

    public T getInnerClass() {
        return t;
    }
}
