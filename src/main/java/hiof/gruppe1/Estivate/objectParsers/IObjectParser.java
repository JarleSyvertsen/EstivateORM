package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.util.HashMap;

public interface IObjectParser {
    public HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object);

    public <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute> attributeList);
}
