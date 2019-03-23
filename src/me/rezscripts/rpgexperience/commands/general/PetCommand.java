package me.rezscripts.rpgexperience.commands.general;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class PetCommand extends AbstractCommand {

    public PetCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
        //        plugin.getInstance(PetManager.class).showMenu(p, pd);
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
