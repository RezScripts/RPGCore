package me.rezscripts.rpgexperience.commands;


import java.lang.reflect.Field;

import me.rezscripts.rpgexperience.AbstractManagerCore;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.commands.admin.BanIPCommand;
import me.rezscripts.rpgexperience.commands.general.PetCommand;
import me.rezscripts.rpgexperience.commands.general.QuestCommand;
import me.rezscripts.rpgexperience.commands.general.TestCommand;
import me.rezscripts.rpgexperience.commands.helper.KickCommand;
import me.rezscripts.rpgexperience.commands.helper.MuteCommand;
import me.rezscripts.rpgexperience.commands.mod.*;
import me.rezscripts.rpgexperience.commands.owner.*;
import me.rezscripts.rpgexperience.players.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


import org.bukkit.craftbukkit.libs.jline.internal.Log;


public class CommandManager extends AbstractManagerCore {

    public static CommandMap cmap = null;

    public CommandManager(RPGCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().split(" ")[0].contains(":")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Hidden syntax is disabled.");
        }
    }

    @Override
    public void initialize() {
        try {
            Field f = CraftServer.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            cmap = (CommandMap) f.get(plugin.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cmap == null) {
            Log.error("FATAL ERROR: COULD NOT RETRIEVE COMMAND MAP.");
            plugin.getServer().shutdown();
            return;
        }
        AbstractCommand.plugin = plugin;

        // Member
        register(Rank.MEMBER, new TestCommand("testcommand"));
        register(Rank.MEMBER, new QuestCommand("q","quest","quests"));
        register(Rank.MEMBER, new PetCommand("pet", "pets"));
        // Mod
        register(RPGCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new ChangeWorldCommand("changeworld", "cw"));
        register(RPGCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new TeleportCommand("teleport", "tp"));
        register(Rank.MOD, new TeleportHereCommand("tphere", "teleporthere"));
        register(Rank.MOD, new BanCommand("ban"));
        register(Rank.MOD, new GetIPCommand("getip"));
        register(Rank.MOD, new PardonCommand("pardon"));
        register(Rank.HELPER, new KickCommand("kick")); // manually disable for Builder rank
        register(Rank.HELPER, new MuteCommand("mute")); // manually disable for Builder rank
        register(RPGCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new BackCommand("back"));
        register(RPGCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new FlyCommand("fly"));
        register(RPGCore.TEST_REALM ? Rank.MEMBER : Rank.MOD, new FlySpeedCommand("flyspeed"));

        // Admin
        register(Rank.ADMIN, new BanIPCommand("banip", "ipban"));
        // Owner
        register(Rank.OWNER, new SetRankCommand("setrank", "setrankalias"));
        register(Rank.OWNER, new GiveBadgeCommand("givebadge"));
        register(Rank.OWNER, new RemoveBadgeCommand("removebadge"));
        register(Rank.OWNER, new SetInventoryCommand("setinv", "setinventory"));
        register(Rank.OWNER, new ViewInventoryCommand("viewinventory", "checkinventory", "seeinv", "seeinventory"));
        register(Rank.OWNER, new GiveUnlockCommand("giveunlock"));
        register(Rank.OWNER, new RemoveUnlockCommand("removeunlock"));

    }

    protected void register(Rank rank, AbstractCommand command) {
        command.requiredRank = rank;
        cmap.register("", command);
        plugin.getServer().getPluginManager().registerEvents(command, plugin);
    }
}