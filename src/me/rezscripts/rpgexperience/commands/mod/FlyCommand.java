package me.rezscripts.rpgexperience.commands.mod;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class FlyCommand extends AbstractCommand {

    public FlyCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
        p.setAllowFlight(!p.getAllowFlight());
        p.sendMessage(ChatColor.AQUA + "Flying " + (p.getAllowFlight() ? "en" : "dis") + "abled!");
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
