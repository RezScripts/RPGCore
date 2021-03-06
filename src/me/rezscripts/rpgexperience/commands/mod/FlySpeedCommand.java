package me.rezscripts.rpgexperience.commands.mod;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class FlySpeedCommand extends AbstractCommand {

    public FlySpeedCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
        try {
            p.setFlySpeed(Float.parseFloat(args[0]));
            p.sendMessage(ChatColor.AQUA + "Flyspeed set to " + p.getFlySpeed());
        } catch (Exception e) {
            p.sendMessage("Use as /flyspeed <0.0-1.0>");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
