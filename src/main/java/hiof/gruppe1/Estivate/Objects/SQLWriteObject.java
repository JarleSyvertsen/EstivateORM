package hiof.gruppe1.Estivate.Objects;

import java.util.HashMap;

public class SQLWriteObject {
    HashMap<String, SQLAttribute> attributes;

    public SQLWriteObject() {
    }

    public void addAttribute(String name, SQLAttribute<Object> attribute) {
        attributes.put(name, attribute);
    }

    public HashMap<String, SQLAttribute> getAttributeList() {
        return attributes;
    }
}

