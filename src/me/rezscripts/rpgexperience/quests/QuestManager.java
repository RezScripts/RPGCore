package me.rezscripts.rpgexperience.quests;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.rezscripts.rpgexperience.AbstractManager;
import me.rezscripts.rpgexperience.ManagerInstances;
import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.menus.MenuItem;
import me.rezscripts.rpgexperience.menus.MenuManager;
import me.rezscripts.rpgexperience.quests.questLine.QuestLine;
import me.rezscripts.rpgexperience.quests.questLine.QuestLineManager;
import me.rezscripts.rpgexperience.utils.ItemBuilder;
import me.rezscripts.rpgexperience.utils.RUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;



public class QuestManager extends AbstractManager {

    protected static ArrayList<Quest> quests = new ArrayList<Quest>();

    public QuestManager(RPGCore plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        readQuestFiles();
        setUpSteps();
    }


    private void setUpSteps() {
        for (Quest q : getQuestList()) {
            q.registerSteps();
        }
    }

    private void addQuests() {
        readQuestFiles();
    }

    private void readQuestFiles()
    {
        QuestLineManager qLm = (QuestLineManager) ManagerInstances.getInstance( QuestLineManager.class );
        for (QuestLine ql : qLm.getQuestLines()) {
            File folder = new File( RPGCore.plugin.getDataFolder() + "/Quests/" + ql.getID() + "-" + ql.getName());

            if (!folder.exists()) {
                return;
            }

            for (File f : folder.listFiles()) {
                if (f.getName().startsWith( "-" )) {
                    continue;
                }

                try {
                    YamlConfiguration questFile = new YamlConfiguration();
                    questFile.load( f );
                    String name = f.getName().replaceAll( ".YAML", "" );
                    int id = questFile.getInt( "ID" );

                    ArrayList<ItemStack> rewards = getRewards( questFile );

                    int level = 0;
                    ArrayList<Integer> reqQuests = new ArrayList<Integer>();

                    if (questFile.getConfigurationSection( "Requirements" ) != null) {
                        ConfigurationSection s = questFile.getConfigurationSection( "Requirements" );

                        if (s.getInt( "Level" ) != 0) {
                            level = s.getInt( "Level" );
                        }

                        if (s.getIntegerList( "Required Quests" ) != null) {
                            for (Integer in : s.getIntegerList( "Required Quests" )) {
                                reqQuests.add( in );
                            }
                        }
                    }

                    Quest q = new Quest( id, ql, name, rewards == null ? null : rewards,
                            reqQuests.isEmpty() ? null : RUtils.convertIntegers( reqQuests ), level );
                    addQuest(q);
                    ql.addQues(q);

                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

            }
        }

    }

    public void reload(){
        setQuestList(new ArrayList<Quest>());
        readQuestFiles();
        setUpSteps();
    }


    public void showMenu(QuestLine ql, Player p, PlayerData pd) {
        ArrayList<MenuItem> list = new ArrayList<MenuItem>();
        int row = 1;
        int col = 0;

        Comparator<Integer> comp = (Integer a, Integer b) -> {
            return a.compareTo(b);
        };
        ArrayList<Integer> ints = getQuestIDList(ql);
        Collections.sort(ints, comp);

        for (int id : ints) {
            Quest q = getQuest(id);
            list.add(new MenuItem(row, col, getItem(q, p, pd), () ->
            {
                p.closeInventory();
                q.startQuest(p);
            }));
            col++;
            if (col > 8) {
                row++;
                col = 0;
            }
        }
        p.openInventory(MenuManager.createMenu(p, ql.getName() + " - Quests", 5, list));

    }

    private ItemStack getItem(Quest q, Player p, PlayerData pd) {
        QuestManager qm = (QuestManager) ManagerInstances.getInstance(QuestManager.class);

        if (pd.inProgressQuests.containsKey(q)) {
            // IN PROG
            List<String> lore = new ArrayList<String>();
            lore.add("");
            for (Step s : qm.getCompletedSteps(p, q)) {
                lore.add(ChatColor.translateAlternateColorCodes('&', "  &a❖&r  &a&m" + s.getDesc()));
            }
            String[] splitStep = pd.inProgressQuests.get(q).split("-");
            Integer stepID = Integer.parseInt(splitStep[0]);
            Integer stepScore = Integer.parseInt(splitStep[1]);

            lore.add(ChatColor.translateAlternateColorCodes('&', "  &e❖  " + q.getSteps().get(stepID).getDesc()
                    + "  &7[" + stepScore + "/" + q.getSteps().get(stepID).getScore() + "]"));
            return new ItemBuilder(Material.BOOK_AND_QUILL)
                    .setName(ChatColor.translateAlternateColorCodes('&', "&e&l" + q.getName())).setLore(lore)
                    .addLoreLine("").addLoreLine("").addLoreLine(getQuestBar(q,pd)).addLoreLine( "" ).addLoreLine(ChatColor.translateAlternateColorCodes('&', "&eIn Progress"))
                    .toItemStack();
        }
        if (pd.completedQuests.contains(q)) {
            // COMPLETED
            List<String> lore = new ArrayList<String>();
            lore.add("");
            for (Step s : q.getSteps()) {
                lore.add(ChatColor.translateAlternateColorCodes('&', "  &a❖  &r&a&m" + s.getDesc()));
            }
            return new ItemBuilder(Material.BOOK).addGlow()
                    .setName(ChatColor.translateAlternateColorCodes('&', "&a&l" + q.getName())).setLore(lore)
                    .addLoreLine("").addLoreLine("").addLoreLine(RUtils.getPercecentBar(ChatColor.GREEN, 1,1)).addLoreLine( "" ).addLoreLine(ChatColor.translateAlternateColorCodes('&', "&aCompleted"))
                    .toItemStack();
        }

        else {
            // NOT STARTED
            List<String> lore = new ArrayList<String>();
            lore.add("");
            if (q.getReqQuestsArray() != null || q.getReqLevel() > 1) {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&eRequirements: "));
                lore.add("");

                if (q.getReqQuestsArray() != null) {
                    for (Quest qu : q.getReqQuestsArray()) {
                        lore.add(ChatColor.translateAlternateColorCodes('&',
                                pd.completedQuests.contains(qu) ? "  &a✓ " + qu.getName()
                                        : "  &c✗ " + qu.getName()));
                    }
                }


//				if (q.getReqLevel() >= 1) {
//					lore.add(ChatColor.translateAlternateColorCodes('&',
//							LevelUtils.getLevel(pd.getXp()) >= q.getReqLevel() ? "  &a✓ Level " + q.getReqLevel()
//									: "  &c✗ Level " + q.getReqLevel()));
//				}

                lore.add("");

            }
            lore.add(ChatColor.YELLOW
                    + "To start this quest:");
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "  &a❖  &r&a" + q.getSteps().get(0).getDesc()));
            return new ItemBuilder(Material.BOOK)
                    .setName(ChatColor.translateAlternateColorCodes('&', "&c&l" + q.getName())).setLore(lore)
                    .addLoreLine("").addLoreLine("").addLoreLine(ChatColor.translateAlternateColorCodes('&', "&cNot Started"))
                    .toItemStack();
        }
    }

    private String getQuestBar(Quest q, PlayerData pd)
    {
        int steps = q.getSteps().size();
        //1:1-0
        int completed = Integer.parseInt(pd.inProgressQuests.get(q)
                .split("-")[0]);

        return RUtils.getPercecentBar( ChatColor.GREEN, completed, steps );
    }

    @SuppressWarnings("deprecation")
    private ArrayList<ItemStack> getRewards(YamlConfiguration questFile)
    {
        ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
        ConfigurationSection rewardSection = questFile.getConfigurationSection("Rewards");
        int i = 1;
        while (rewardSection.getConfigurationSection("Reward " + i) != null)
        {
            ConfigurationSection s = rewardSection.getConfigurationSection("Reward " + i);
            if (s.getString("Placeholder") != null) {
                rewards.add(RUtils.getPlaceHolderItem(s.getString("Placeholder"), s.getInt("Amount")));
            } else {
                try {
                    ItemStack item = new ItemStack(Material.valueOf(s.getString("Material")));

                    if (s.getInt("Amount") != 0) {
                        item.setAmount(s.getInt("Amount"));
                    }

                    if (s.getInt("Data") != 0) {
                        item.setData(new MaterialData(s.getInt("Data")));
                    }

                    if (s.getInt("Durability") != 0) {
                        item.setDurability((short) s.getInt("Durability"));
                    }

                    ItemMeta im = item.getItemMeta();
                    if (s.getString("Display Name") != null) {
                        im.setDisplayName(
                                ChatColor.translateAlternateColorCodes('&', s.getString("Display Name")));
                    }
                    ArrayList<String> lore = new ArrayList<String>();
                    if (s.getConfigurationSection("Lore") != null) {
                        int o = 1;
                        while (s.getConfigurationSection("Lore").getString("Line " + o) != null) {
                            lore.add(ChatColor.translateAlternateColorCodes('&',
                                    s.getConfigurationSection("Lore").getString("Line " + o)));
                            o+=1;
                        }
                    }
                    im.setLore(lore);
                    item.setItemMeta(im);
                    rewards.add(item);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            i+=1;
        }

        return rewards;
    }

    private void addQuest(Quest quest) {
        if (quest == null) {
            return;
        }
        try {
            getQuestList().add(quest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Step> getCompletedSteps(Player p, Quest q) {
        ArrayList<Step> steps = new ArrayList<Step>();
        PlayerData pd = RPGCore.plugin.getPD(p);

        for (Step s : q.getSteps()) {
            if (q.getSteps().indexOf(s) < Integer.parseInt(pd.inProgressQuests.get(q).split("-")[0])) {
                steps.add(s);
            }
            continue;
        }

        return steps;
    }

    @SuppressWarnings("unused")
    private ArrayList<ItemStack> getRewardsTable(ItemStack... items) {
        ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
        for (ItemStack i : items) {
            rewards.add(i);
        }
        return rewards;
    }

    public ArrayList<Quest> getQuestList() {
        return quests;
    }

    public void setQuestList(ArrayList<Quest> questList) {
        this.quests = questList;
    }

    public Quest getQuest(int id) {
        for (Quest q : quests) {
            if (q.getId() == id) {
                return q;
            }
        }
        return null;
    }

    public ArrayList<Integer> getQuestIDList(QuestLine ql)
    {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (Quest q : this.getQuestList()) {
            if (q.getqLine() == ql) {
                ints.add( q.getId() );
            }
        }
        return ints;
    }

    public ArrayList<Integer> getQuestIDList()
    {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (Quest q : this.getQuestList()) {
            ints.add(q.getId());
        }
        return ints;
    }
}
