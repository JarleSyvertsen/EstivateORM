package hiof.gruppe1.Estivate.config;

public class assocConfiguration implements ConfigurationObject {
    String javaAssoc;
    String SQLAssoc;
    String affectedClass;
    config callingConfig;

    public assocConfiguration(String affectedClass, config callingConfig) {
        this.affectedClass = affectedClass;
    }

    /**
     * An object intended to store a java-SQL attribute binding.
     * Can be set with a single class/table to limit the scope of the modification.
     * @param affectedClass The class/table which would be affected by the settings.
     * @param javaAssoc The fully qualified name of the Java class in the Java-to-SQL binding.
     * @param SQLAssoc The name of the SQL attribute in the Java-to-SQL binding.
     */
    public assocConfiguration(String affectedClass, String javaAssoc, String SQLAssoc) {
        this.javaAssoc = javaAssoc;
        this.SQLAssoc = SQLAssoc;
        this.affectedClass = affectedClass;
    }

    /**
     * Returns the Java attribute of the Java-SQL binding represented by the object.
     * @return String
     */
    public String getJavaAssoc() {
        return javaAssoc;
    }
    /**
     * Sets the Java attribute of the Java-SQL binding represented by the object.
     */
    public void setJavaAssociation(String javaAssoc) {
        this.javaAssoc = javaAssoc;
    }
    /**
     * Returns the SQL attribute of the Java-SQL binding represented by the object.
     * @return String
     */
    public String getSQLAssociation() {
        return SQLAssoc;
    }
    /**
     * Sets the SQL attribute of the Java-SQL binding represented by the object.
     */
    public void setSQLAssoc(String SQLAssoc) {
        this.SQLAssoc = SQLAssoc;
    }
    /**
     * Returns the currently affected class/table which the Java-SQL binding applies to, if applicable.
     * @return String
     */
    public String getAffectedClass() {
        return affectedClass;
    }
    /**
     * Sets the currently affected class/table which the Java-SQL binding applies to.
     * If unset, the binding is overwritten for all classes.
     */
    public void setAffectedClass(String affectedClass) {
        this.affectedClass = affectedClass;
    }

    /**
     * Saves the current changes and sets the configuration as active.
     */
    public void SaveConfiguration() {
        callingConfig.saveConfiguration();
    }
}
