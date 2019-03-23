package me.rezscripts.rpgexperience.shield;


import me.rezscripts.rpgexperience.RPGCore;

import java.util.List;

public abstract class RPGShield {

    public void start() {
        int count = 0;
        List<ShieldCheck> checks = getChecks();
        if (checks != null) {
            for (ShieldCheck sc : checks) {
                getPlugin().getServer().getPluginManager().registerEvents(sc, getPlugin());
                count++;
            }
        }
        System.out.println("Registered " + count + " checks for RPGShield.");
    }

    public abstract RPGCore getPlugin();

    public abstract List<ShieldCheck> getChecks();

    public abstract void halt();
}
