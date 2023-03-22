package hiof.gruppe1.Estivate.EstivateCore;

import hiof.gruppe1.Estivate.config.config;

public class Estivate {
    config WorkingConfiguration = new config();
    public config getConfig() {
        return WorkingConfiguration;
    }

    public EstivateTransaction startTransaction() {
        return new EstivateTransaction().startTransaction();
    }


}
