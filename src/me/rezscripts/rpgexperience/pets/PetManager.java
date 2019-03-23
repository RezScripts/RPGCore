package me.rezscripts.rpgexperience.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.rezscripts.rpgexperience.AbstractManagerCore;
import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.menus.MenuItem;
import me.rezscripts.rpgexperience.menus.MenuManager;
import me.rezscripts.rpgexperience.utils.RHead;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;



public class PetManager extends AbstractManagerCore {

    protected static HashMap<UUID, UUID> spawnedPets = new HashMap<UUID, UUID>();

    private static HashMap<String, String> replacers = new HashMap<String, String>();
    static {
        replacers.put("Peter", "Xerty");
        replacers.put("robot", "pet");
        replacers.put("botmaster", "master");
    }

    public PetManager(RPGCore plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
    }

    public boolean isPetOwner(Player p, LivingEntity le) {
        return spawnedPets.containsKey(le.getUniqueId()) && spawnedPets.get(le.getUniqueId()).equals(p.getUniqueId());
    }

    public void showMenu(Player p, PlayerData pd) {
        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        int row = 1;
        int col = 0;
        for (PetsType pt : PetsType.values()) {
            list.add(new MenuItem(row, col, pt == PetsType.LIL_ME ? RHead.getPlayerSkull(p.getName()) : pt.item, ChatColor.YELLOW + pt.display, new String[] {
                    ChatColor.GRAY + "Click to switch to " + pt.display
            }, () -> {
                System.out.println("triggered runnable " + pt);
                pd.spawnNewPet(pt);
            }));
            col++;
            if (col > 8) {
                row++;
                col = 0;
            }
        }
        p.openInventory(MenuManager.createMenu(p, "RPGExperience Pets", 5, list));
    }

    public static void givePet(PlayerData pd, PetsType type) {
        if (pd.ownedPets.containsKey(type)) {
            pd.sendMessage(ChatColor.RED + "You received a " + type.display + " pet, but you already have one so nothing happened!");
        } else {
            pd.ownedPets.put(type, 0l);
            pd.sendMessage(ChatColor.AQUA + " " + ChatColor.BOLD + "Congratulations!");
            pd.sendMessage(ChatColor.GREEN + " You have a new " + ChatColor.LIGHT_PURPLE + type.display + ChatColor.GREEN + " pet!");
            pd.sendMessage(ChatColor.GRAY + " Use " + ChatColor.YELLOW + "/pet" + ChatColor.GRAY + " to check out your new pet.");
        }
        pd.save();
    }

}