package me.rezscripts.rpgexperience.commands.general;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TestCommand extends AbstractCommand {

    public TestCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Test command.");
    }

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }
}
