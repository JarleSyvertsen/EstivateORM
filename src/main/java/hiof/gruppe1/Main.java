package hiof.gruppe1;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.Objects.SQLWriteObject;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;
import hiof.gruppe1.testData.Author;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        ReflectionParser rp = new ReflectionParser();
        Author perArne = new Author("Per Arne", "To tredjedel ved");
        HashMap<String, SQLAttribute> attributes = rp.parseObjectToAttributeList(perArne);
        SQLWriteObject writeObject = new SQLWriteObject();
        writeObject.setAttributes(attributes);
        writeObject.getAttributeList().forEach((k,v) -> System.out.println());
        SQLAttribute test = new SQLAttribute(Integer.class, 5);
        int i = test.getData();
    }
}