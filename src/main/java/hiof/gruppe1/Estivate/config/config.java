package hiof.gruppe1.Estivate.config;

import java.io.File;
import java.util.Queue;

/**
 * The namespace corresponding to changing some default behavior of the ORM. Supports changing table- to-class association, Java attribute to SQL attribute. Additionally, addressing the database directly can be done using functions in this namespace.
 */
public class config {
    File filePersistentStorage;
    Queue<ConfigurationObject> storedConfigurations;
    assocConfiguration currentConfiguration;

    public config() {
    }

    public config(File filePersistentStorage) {
        this.filePersistentStorage = filePersistentStorage;
    }

    /**
     *
     Provides an object that governs the behavior of a given Java class. Options to change a given SQL column relation can be set on this object, and additionally, changes to Java-->SQL type associations can be overwritten for individual classes.
     * @param objectName
     * @return assocConfiguration assoc
     */
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

    /**
     * Allows setting the assciation of a single java attribute in a single method call.
     * @param workingClass
     * @param javaAssoc
     * @param SQLAssoc
     * @param <T>
     */
    public <T> void setDefault(Class<T> workingClass, String javaAssoc, String SQLAssoc) {
        storedConfigurations.add(new assocConfiguration(workingClass.getSimpleName(), javaAssoc, SQLAssoc));
    }
}


