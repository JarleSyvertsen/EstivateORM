package hiof.gruppe1;

import hiof.gruppe1.Estivate.Objects.SQLAttribute;
import hiof.gruppe1.Estivate.objectParsers.ReflectionParser;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        ReflectionParser rp = new ReflectionParser();
        HashMap<String, SQLAttribute> attributes =  rp.parseObjectToAttributeList(2);

    }
}