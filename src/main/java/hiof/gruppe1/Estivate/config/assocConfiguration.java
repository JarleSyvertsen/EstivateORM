package hiof.gruppe1.Estivate.config;

public class assocConfiguration implements ConfigurationObject {
    String javaAssoc;
    String SQLAssoc;
    String affectedClass;
    Configuration callingConfig;

    public assocConfiguration(String affectedClass, Configuration callingConfig) {
        this.affectedClass = affectedClass;
    }


    public assocConfiguration(String affectedClass, String javaAssoc, String SQLAssoc) {
        this.javaAssoc = javaAssoc;
        this.SQLAssoc = SQLAssoc;
        this.affectedClass = affectedClass;
    }


    public String getJavaAssoc() {
        return javaAssoc;
    }

    public void setJavaAssociation(String javaAssoc) {
        this.javaAssoc = javaAssoc;
    }

    public String getSQLAssociation() {
        return SQLAssoc;
    }

    public void setSQLAssoc(String SQLAssoc) {
        this.SQLAssoc = SQLAssoc;
    }

    public String getAffectedClass() {
        return affectedClass;
    }

    public void setAffectedClass(String affectedClass) {
        this.affectedClass = affectedClass;
    }
    public void SaveConfiguration() {
        callingConfig.saveConfiguration();
    }
}
