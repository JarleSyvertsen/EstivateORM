package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionParser implements IObjectParser {
    public <T> HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object) {
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
