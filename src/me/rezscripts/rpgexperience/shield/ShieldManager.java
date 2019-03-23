package me.rezscripts.rpgexperience.shield;


import me.rezscripts.rpgexperience.AbstractManagerCore;
import me.rezscripts.rpgexperience.RPGCore;

public class ShieldManager extends AbstractManagerCore {

    private RPGShield activeShield = null;

    public ShieldManager(RPGCore plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        register(new RPGShieldCore());
    }

    public void register(RPGShield shield) {
        if (this.activeShield != null) {
            activeShield.halt();
        }
        activeShield = shield;
        shield.start();
    }

}
