package me.rezscripts.rpgexperience.commands.general;

import me.rezscripts.rpgexperience.ManagerInstances;
import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.commands.AbstractCommand;
import me.rezscripts.rpgexperience.quests.QuestManager;
import me.rezscripts.rpgexperience.quests.questLine.QuestLineManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class QuestCommand extends AbstractCommand {

    public QuestCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        QuestLineManager qm = (QuestLineManager) ManagerInstances.getInstance(QuestLineManager.class);
        if (qm == null){
            return;
        }
        qm.showQuestLineMenu(p, RPGCore.plugin.getPD(p));
    }

    @Override
    public void executePlayer(Player p, PlayerData pd, String[] args) {
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}
