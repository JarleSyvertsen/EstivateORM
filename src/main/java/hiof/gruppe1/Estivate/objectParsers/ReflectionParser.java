package hiof.gruppe1.Estivate.objectParsers;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionParser implements IObjectParser {
    public<T> HashMap<String, SQLAttribute> parseObjectToAttributeList(Object object) {
        HashMap<String, SQLAttribute> attributes = new HashMap<>();
        for (Method getter : object.getClass().getMethods()) {
            if(getter.getName().startsWith("get") && getter.getParameterTypes().length == 0) {
                try {
                    final Object returnValue = getter.invoke(object);
                  attributes.put(getter.getName().substring(3), new SQLAttribute(returnValue.getClass(), returnValue));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    System.out.println(e);
                }

            }
        }
        return attributes;
    }

    public <T> T parseAttributeListToObject(Class<T> castTo, HashMap<String, SQLAttribute> attributeList) {
        return null;
    }

}
