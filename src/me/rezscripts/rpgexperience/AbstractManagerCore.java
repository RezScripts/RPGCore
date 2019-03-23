package me.rezscripts.rpgexperience;

public abstract class AbstractManagerCore extends AbstractManager {

    public static RPGCore plugin;

    public AbstractManagerCore(RPGCore pl) {
        super(pl);
        plugin = pl;
    }

}
