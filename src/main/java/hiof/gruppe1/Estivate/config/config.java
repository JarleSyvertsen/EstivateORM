package hiof.gruppe1.Estivate.config;

import java.util.Queue;

/**
 * The namespace corresponding to changing some default behavior of the ORM.
 * Supports changing table-to-class association or Java-to-SQL attribute association.
 * Additionally, addressing the database directly can be done using functions in this namespace.
 */
public class config {
    Queue<ConfigurationObject> storedConfigurations;
    assocConfiguration currentConfiguration;
    /**
     * Creates an object that allows for retrieving and storing single assocConfiguration objects.
     * assocConfiguration represents single settings that can be changed and persisted.
     */
    public config() {
    }

    /**
     *
     Provides an object that governs the behavior of a given Java class. Options to change a given SQL column relation can be set on this object, and additionally, changes to Java-->SQL type associations can be overwritten for individual classes.
     * @param objectName Name of table/class which configurations would affect.
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
     * Allows setting the association of a single java attribute in a single method call.
     * @param workingClass The class/table which would be affected by the settings.
     * @param javaAssoc The fully qualified name of the Java class in the Java-to-SQL binding.
     * @param SQLAssoc The name of the SQL attribute in the Java-to-SQL binding
     */
    public <T> void setDefault(Class<T> workingClass, String javaAssoc, String SQLAssoc) {
        storedConfigurations.add(new assocConfiguration(workingClass.getSimpleName(), javaAssoc, SQLAssoc));
    }
}


