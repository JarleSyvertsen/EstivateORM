package hiof.gruppe1.Estivate.config;

import java.io.File;
import java.util.Queue;

public class config {
    File filePersistentStorage;
    Queue<ConfigurationObject> storedConfigurations;
    assocConfiguration currentConfiguration;

    public config() {
    }

    public config(File filePersistentStorage) {
        this.filePersistentStorage = filePersistentStorage;
    }

    public assocConfiguration configObject(String objectName) {
        if (currentConfiguration.getAffectedClass().compareTo(objectName) != 0) {
            currentConfiguration = new assocConfiguration(objectName, this);
        }
        return currentConfiguration;
    }

    public void saveConfiguration() {
        storedConfigurations.add(currentConfiguration);
        currentConfiguration = null;
    }

    public <T> void setDefault(Class<T> workingClass, String javaAssoc, String SQLAssoc) {
        storedConfigurations.add(new assocConfiguration(workingClass.getSimpleName(), javaAssoc, SQLAssoc));
    }
}


