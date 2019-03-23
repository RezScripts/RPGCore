package me.rezscripts.rpgexperience.shield;

import java.util.HashMap;
import java.util.List;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.utils.RMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;



public class RPGShieldCore extends RPGShield {

    public enum Activity {
        FAST_CHAT("Message throttled - you are talking too quickly!", 1);
        ;
        public String message = "ERROR";
        public int amount = 1;

        Activity(String message, int amount) {
            this.message = message;
            this.amount = amount;
        }
    }
    private static final int KICK_THRESHOLD = 20;

    private static HashMap<String, Integer> warnLevels = new HashMap<String, Integer>();

    private static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8[&3RPGShield&8] &c");

    public static void clear(String name) {
        warnLevels.remove(name);
    }

    public static void warn(Player p, Activity a) {
        p.sendMessage(PREFIX + a.message);
        warnLevels.compute(p.getName(), (k, v) -> v == 0 ? 1 : a.amount + a.amount);
        check(p);
    }

    public static void warn(PlayerData pd, Activity a) {
        pd.sendMessage(PREFIX + a.message);
        warnLevels.compute(pd.getName(), (k, v) -> v == 0 ? 1 : a.amount + a.amount);
        check(pd);
    }

    public static void check(Player p) {
        if (warnLevels.getOrDefault(p.getName(), 0) > KICK_THRESHOLD) {
            p.kickPlayer(PREFIX + "Kicked for reaching RPGShield suspicion threshold.");
            RMessages.announce(PREFIX + p.getName() + " was kicked for reaching the suspicion threshold.");
        }
    }

    public static void check(PlayerData pd) {
        if (pd.getPlayer() != null) {
            if (warnLevels.getOrDefault(pd.getName(), 0) > KICK_THRESHOLD) {
                pd.getPlayer().kickPlayer(PREFIX + "Kicked for reaching RPGShield suspicion threshold.");
                RMessages.announce(PREFIX + pd.getName() + " was kicked for reaching the suspicion threshold.");
            }
        }
    }

    @Override
    public List<ShieldCheck> getChecks() {
        return null;
    }

    @Override
    public void halt() {
    }

    @Override
    public RPGCore getPlugin() {
        return RPGCore.plugin;
    }

}