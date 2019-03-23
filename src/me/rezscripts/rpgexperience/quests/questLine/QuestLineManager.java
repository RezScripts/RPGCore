package me.rezscripts.rpgexperience.quests.questLine;

import me.rezscripts.rpgexperience.AbstractManager;
import me.rezscripts.rpgexperience.ManagerInstances;
import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.menus.MenuItem;
import me.rezscripts.rpgexperience.menus.MenuManager;
import me.rezscripts.rpgexperience.quests.Quest;
import me.rezscripts.rpgexperience.quests.QuestManager;
import me.rezscripts.rpgexperience.utils.ItemBuilder;
import me.rezscripts.rpgexperience.utils.RUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestLineManager extends AbstractManager {

    private static ArrayList<QuestLine> questLines = new ArrayList<QuestLine>();

    public QuestLineManager(RPGCore plugin) {
        super( plugin );
    }

    public ArrayList<QuestLine> getQuestLines() {
        return questLines;
    }

    public void setQuestLines(ArrayList<QuestLine> questLines) {
        QuestLineManager.questLines = questLines;
    }

    public QuestLine getQuestLine(String s) {
        for (QuestLine ql : getQuestLines()) {
            if (ql.getName() == s) {
                return ql;
            }
        }
        return null;
    }

    public void showQuestLineMenu(Player p, PlayerData pd) {
        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        int row = 0;
        int col = 0;

        Comparator<Integer> comp = (Integer a, Integer b) -> {
            return a.compareTo( b );
        };
        ArrayList<Integer> ints = getQuestIDList();

        Collections.sort( ints, comp );

        for (QuestLine ql : getQuestLines()) {
            list.add( new MenuItem( row, col, getItem( ql, pd ), () ->
            {
                p.closeInventory();
                QuestManager qm = (QuestManager) ManagerInstances.getInstance( QuestManager.class );
                qm.showMenu( ql, p, pd );
            } ) );
            col++;
            if (col > 8) {
                row++;
                col = 0;
            }
        }
        p.openInventory( MenuManager.createMenu( p, "Quest Lines", 1, list ) );

    }

    public void reload(){
        questLines = new ArrayList<QuestLine>();
        setUpQuestLines();
    }

    public ItemStack getItem(QuestLine ql, PlayerData pd) {
        boolean completed = true;

        if (ql.getQuests() == null){
            return new ItemStack( Material.BARRIER );
        }

        for (Quest q : ql.getQuests()) {
            if (!pd.completedQuests.contains( q )) {
                completed = false;
                break;
            }
            continue;
        }

        int done = getQuestsDone(ql, pd );
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes( '&',"&eQuests: &7" + done + "/" + ql.getQuests().size()));
        lore.add(ChatColor.translateAlternateColorCodes( '&',"" ));
        lore.add(ChatColor.translateAlternateColorCodes( '&',"&e         &lProgress" ));
        lore.add( RUtils.getPercecentBar(ChatColor.GREEN, done, ql.getQuests().size()));
        lore.add("");

        if (completed){
            return new ItemBuilder(Material.BOOK).setName( ChatColor.YELLOW + "" + ChatColor.BOLD + ql.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " Quest Line").setLore(lore).addGlow().toItemStack();
        }else{
            return new ItemBuilder(Material.BOOK).setName( ChatColor.YELLOW + "" + ChatColor.BOLD + ql.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " Quest Line").setLore(lore).toItemStack();
        }
    }

    public int getQuestsDone(QuestLine ql, PlayerData pd){
        int i = 0;
        for (Quest q : ql.getQuests()) {
            if (pd.completedQuests.contains( q )) {
                i+=1;
            }
            continue;
        }
        return i;
    }

    public ArrayList<Integer> getQuestIDList() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (QuestLine q : getQuestLines()) {
            ints.add( q.getID() );
        }
        return ints;
    }

    @Override
    public void initialize() {
        setUpQuestLines();
    }

    public void setUpQuestLines() {
        File folder = new File( RPGCore.plugin.getDataFolder() + "/Quests" );

        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (File f : folder.listFiles()) {
            if (f.getName().startsWith( "-" )) {
                continue;
            }
            Bukkit.getConsoleSender().sendMessage( "QuestLine:"  + f.getName());
            String[] split = f.getName().split( "-" );
            getQuestLines().add( new QuestLine( split[1], Integer.parseInt( split[0] ) ) );
        }
    }
}
