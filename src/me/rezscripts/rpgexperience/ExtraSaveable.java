package me.rezscripts.rpgexperience;

import me.rezscripts.rpgexperience.players.PlayerDataFile;
import org.bukkit.entity.Player;


public interface ExtraSaveable {

    public void preLoad(Player p);

    public void postLoad(Player p);

    public PlayerDataFile extraSave(PlayerDataFile pdf);
    
    public void setExtraLoadedData(boolean isNew, PlayerDataFile pdf);
}
