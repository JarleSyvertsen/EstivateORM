package hiof.gruppe1.Estivate.Objects;

import java.util.HashMap;

public class SQLWriteObject {
    HashMap<String, SQLAttribute> attributes;

    public SQLWriteObject() {
    }

    public SQLWriteObject(HashMap<String, SQLAttribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, SQLAttribute attribute) {
        attributes.put(name, attribute);
    }

    public void setAttributes(HashMap<String, SQLAttribute> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, SQLAttribute> getAttributeList() {
        return attributes;
    }

}

