package hiof.gruppe1.Estivate.config;

import java.io.File;

public class Configuration {
    File filePersistentStorage;

    public Configuration() {
    }

    public Configuration(File filePersistentStorage) {
        this.filePersistentStorage = filePersistentStorage;
    }

    public assocConfiguration configObject(String objectName) {
        return new assocConfiguration(objectName);
    }

    public <T> void setDefault(Class<T> workingClass, String javaAssoc, String SQLAssoc) {
        new assocConfiguration(workingClass.getSimpleName(), javaAssoc, SQLAssoc).saveConfiguration();
    }
}


