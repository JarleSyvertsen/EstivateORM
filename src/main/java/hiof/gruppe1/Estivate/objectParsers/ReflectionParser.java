package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.util.HashMap;

public class ReflectionParser implements IObjectParser {

    public HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object) {
        return null;
    }

    public <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute<Object>> attributeList) {
        return null;
    }

}
