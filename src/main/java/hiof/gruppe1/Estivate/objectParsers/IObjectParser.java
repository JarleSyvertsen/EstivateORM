package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.util.HashMap;

public interface IObjectParser {
    HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object);

     <T> HashMap<String, Class<?>> getSubElementList(T castingClass);

     <S, T> void addElementToObject(T baseElement, S elementToAppend, String setter);

     <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute> attributeList);

     <T, S> void addElementsToCollectionQueue(T baseElement, S appendingElement, String setter);

     <T> Class<?> getCollectionInnerClass(T castingClass, String setName);

     <T, S> void writeToCollection(T baseElement, S appendedElement, String variableName);
}
