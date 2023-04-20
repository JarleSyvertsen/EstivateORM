package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.util.HashMap;

public interface IObjectParser {
    public <T> HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object);
    public <T> HashMap<String, Class<?>> getSubElementList(T castingClass);
    public <S, T> T addElementToObject(T baseElement, S elementToAppend, String setter);
    public <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute> attributeList);
}
