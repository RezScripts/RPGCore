package me.rezscripts.rpgexperience.menus;

import me.rezscripts.rpgexperience.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public abstract class MenuGeneralRunnable<T extends PlayerData> {

    @SuppressWarnings("unchecked")
    public void onClick(Player p, ItemStack item, int slot) {
        PlayerData pd = MenuManager.plugin.getPD(p);
        if (p != null && pd != null && item != null && item.getType() != Material.AIR)
            execute(p, (T) pd, item, slot);
    }

    public abstract void execute(final Player p, T pd, ItemStack item, int slot);

}