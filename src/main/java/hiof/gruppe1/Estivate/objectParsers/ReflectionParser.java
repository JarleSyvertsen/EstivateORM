package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import static hiof.gruppe1.Estivate.utils.simpleTypeCheck.isSimple;
import static java.util.Arrays.stream;


public class ReflectionParser implements IObjectParser {
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
        for (Method setter : creationObject.getClass().getMethods())
        {
            if (!setter.getName().startsWith("set")) { continue; }
            String setName = setter.getName().substring(3).toLowerCase();
            try {
                if (attributeList.get(setName) != null) {
                    setter.invoke(creationObject, attributeList.get(setName).getDataRaw());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        return creationObject;
    }

    public Boolean hasSubElements(String castingClass) {
        try {
            HashMap<String, Class<?>> subElements = getSubElementList(createClassOfType(Class.forName(castingClass)));
            return !subElements.isEmpty();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> HashMap<String, Class<?>> getSubElementList(T castingClass) {
        HashMap<String, Class<?>> subElementList = new HashMap<>();
        for (Method setter : castingClass.getClass().getMethods())
        {
            if (!setter.getName().startsWith("set")) { continue; }
            String setName = setter.getName().substring(3).toLowerCase();
                if (!isSimple(setter.getParameterTypes()[0])) {
                    subElementList.put(setName, setter.getParameterTypes()[0]);
                }
        }
        return subElementList;
    }

    public  <S, T> void addElementToObject(T baseElement, S elementToAppend, String setter) {
        for (Method setMethod : baseElement.getClass().getMethods()) {
            if (!setMethod.getName().startsWith("set")) { continue; }
            String setterName = setMethod.getName().substring(3).toLowerCase();
            if(setterName.equals(setter)) {
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
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return creationObject;
    }

}
