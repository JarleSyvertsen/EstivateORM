package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.util.ArrayList;
import java.util.HashMap;

public interface IObjectParser {
    public <T> HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object);
    public <T> ArrayList<String> getSubElementList(T castingClass);
    public <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute> attributeList);
}
