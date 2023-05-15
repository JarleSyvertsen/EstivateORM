package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;

public class ReflectionParser implements IObjectParser {
    private final HashMap<Integer, Queue> collectionWriteQueue = new HashMap<>();

    public HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object) {
        HashMap<String, SQLAttribute> attributes = new HashMap<>();
        for (Method getter : object.getClass().getMethods()) {
            if (getter.getName().startsWith("get") && getter.getParameterTypes().length == 0) {
                try {
                    final Object returnValue = getter.invoke(object);
                    if (returnValue != null) {
                        attributes.put(getter.getName().substring(3).toLowerCase(), new SQLAttribute(returnValue.getClass(), returnValue));
                    }

                } catch (InvocationTargetException | IllegalAccessException e) {
                    System.out.println(e);
                }
            }
        }
        return attributes;
    }

    public <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute> attributeList) {
        T creationObject = createClassOfType(castTo);
        callSetter(attributeList, creationObject);
        return creationObject;
    }

    private static <T> void callSetter(HashMap<String, SQLAttribute> attributeList, T object) {
        for (Method setter : object.getClass().getMethods()) {
            if (!setter.getName().startsWith("set")) {
                continue;
            }
            String setName = setter.getName().substring(3).toLowerCase();
            try {
                if (attributeList.get(setName) != null) {
                    setter.invoke(object, attributeList.get(setName).getDataRaw());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Boolean hasSubElements(String castingClass) {
        try {
            HashMap<String, Class<?>> subElements = getSubElementList(createClassOfType(Class.forName(castingClass)));
            return !subElements.isEmpty();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T, S> void addElementsToCollectionQueue(T baseElement, S appendingElement, String setter) {
        int collectionHash = getCollectionHash(baseElement, setter);

        if (!collectionWriteQueue.containsKey(collectionHash)) {
            Queue<S> elementQueue = new LinkedList<>();
            collectionWriteQueue.put(collectionHash, elementQueue);
            collectionWriteQueue.get(collectionHash).add(appendingElement);
            return;
        }
        collectionWriteQueue.get(collectionHash).add(appendingElement);
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T, S> void writeToCollection(T baseElement, S appendedElement, String variableName) {
        int collectionHash = getCollectionHash(baseElement, variableName);
        String firstUpper = variableName.toLowerCase().substring(0, 1).toUpperCase();
        String getter = "get" + firstUpper + variableName.substring(1);
        String setter = "set" + firstUpper + variableName.substring(1);

        Collection<?> writeCollection;
        try {
            Queue valueQueue = collectionWriteQueue.get(collectionHash);
            if(valueQueue != null) {
                writeCollection = (Collection<?>) baseElement.getClass().getMethod(getter).invoke(baseElement);
                writeCollection.addAll(valueQueue);
                addElementToObject(baseElement, writeCollection, setter);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    // Used to manually create a collection, getting the collection directly proved better.
    // Stored in case it might be useful in some context.
    private static <T> String getCollectionType(T baseElement, String setter) {
        for(Method method : baseElement.getClass().getMethods()) {
            if(method.getName().equals(setter)) {
                  return method.getParameterTypes()[0].getName();
            }
        }
        return null;
    }

    public <T> int getCollectionHash(T object, String setter) {
        return Objects.hash(object.hashCode(), setter.hashCode());
    }

    public <T> HashMap<String, Class<?>> getSubElementList(T castingClass) {
        HashMap<String, Class<?>> subElementList = new HashMap<>();
        for (Method setter : castingClass.getClass().getMethods()) {
            if (!setter.getName().startsWith("set")) {
                continue;
            }
            String setName = setter.getName().substring(3).toLowerCase();
            if (!isSimple(setter.getParameterTypes()[0])) {
                subElementList.put(setName, setter.getParameterTypes()[0]);
            }
        }
        return subElementList;
    }


    public <T> Class<?> getCollectionInnerClass(T castingClass, String setName) {
        Class<?> typeClass;
        try {
            Field typeField = castingClass.getClass().getDeclaredField(setName);
            ParameterizedType paraType = (ParameterizedType) typeField.getGenericType();
            typeClass = (Class<?>) paraType.getActualTypeArguments()[0];
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return typeClass;
    }

    public <S, T> void addElementToObject(T baseElement, S elementToAppend, String setter) {
        for (Method setMethod : baseElement.getClass().getMethods()) {
            if (!setMethod.getName().startsWith("set")) {
                continue;
            }
            String setterName = setMethod.getName().substring(3).toLowerCase();
            if (setterName.equals(setter)) {
                try {
                    setMethod.invoke(baseElement, elementToAppend);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static <T> T createClassOfType(Class<T> castTo) throws RuntimeException {
        T creationObject;
        try {
            creationObject = castTo.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return creationObject;
    }

}
