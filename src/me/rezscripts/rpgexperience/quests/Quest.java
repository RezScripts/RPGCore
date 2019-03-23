package me.rezscripts.rpgexperience.quests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import me.rezscripts.rpgexperience.ManagerInstances;
import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.quests.events.InteractionType;
import me.rezscripts.rpgexperience.quests.questLine.QuestLine;
import me.rezscripts.rpgexperience.utils.RUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;



public class Quest {

	public int id;
	public String name;
	public QuestLine qLine;
	public ArrayList<Step> steps;
	public ArrayList<ItemStack> rewards;
	public Integer[] reqQuests;
	private int reqLevel;

	Quest(int id,QuestLine ql, String name, ArrayList<ItemStack> rewards, Integer[] reqQuests, int reqLevel) {
		setId(id);
		setqLine( ql );
		setName(name);
		setSteps(new ArrayList<Step>());
		setRewards(rewards);
		setReqQuests(reqQuests);
		setReqLevel(reqLevel);
	}

	public void registerSteps() {
		try {
			addStepsFromFile();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		for (Step s : getSteps()) {
			s.register();
		}
	}

	private void addStepsFromFile() throws FileNotFoundException, IOException, InvalidConfigurationException 
	{
		File folder = new File(RPGCore.plugin.getDataFolder() + "/Quests/" + this.getqLine().getID() + "-" + this.getqLine().getName() + "/" + this.getName() + ".YAML");
		if (!folder.exists()) {
			Bukkit.getConsoleSender().sendMessage("Quest does not exist");
			return;
		}
		
		YamlConfiguration questFile = new YamlConfiguration();
		questFile.load(folder);
		
		ConfigurationSection cs = questFile.getConfigurationSection("Steps");
		
		int i = 1;
		
		while (cs.getConfigurationSection("Step " + i) !=null) {
			ConfigurationSection step = cs.getConfigurationSection("Step " + i);
			boolean start = step.getBoolean("Start");
			String type = step.getString("Type");
			InteractionType iType;
			String desc = step.getString("Description");
			String startText = step.getString("Text on start");
			String finishText = step.getString("Text on finish");
			int points = step.getInt("Points Needed");
			String args = step.getString("Args");
			
			switch(type) {
				case"Touch Block":
					iType = InteractionType.BlockInteract;
					break;
				case"Talk to NPC":
					iType = InteractionType.NpcInteract;
					break;
				case"Send Command":
					iType = InteractionType.CommandIsuue;
					break;
				default:
					iType = InteractionType.BlockInteract;
					break;
			}
			
			Step s = new Step(iType, start, points, this, desc, startText, finishText, args);
			this.addStep(s);
			i+=1;
		}
		
	}

	public void startQuest(Player p) {
		if (RPGCore.plugin.getPD(p).inProgressQuests.containsKey(this)
				|| RPGCore.plugin.getPD(p).completedQuests.contains(this)) {
			return;
		} else {
			getStartText(p);
			RPGCore.plugin.getPD(p).inProgressQuests.put(this, "1-0");
			addStepScore(1,this.getSteps().get(0),RPGCore.plugin.getPD(p));
			p.sendMessage( ChatColor.translateAlternateColorCodes( '&',this.getSteps().get( 0 ).getComplete() ) );
			p.sendMessage( ChatColor.translateAlternateColorCodes( '&',this.getSteps().get( 1 ).getStart() ) );
			return;
		}
	}

	public void completeQuest(Player p) {
		RPGCore.plugin.getPD(p).inProgressQuests.remove(this);
		RPGCore.plugin.getPD(p).completedQuests.add(this);
		getCompleteText(p);
		for (ItemStack i : getRewards()) {
			if (RUtils.hasInventorySpace(p, i)) {
			RUtils.giveItem(p, i);
			}else {
				p.getWorld().dropItemNaturally(p.getEyeLocation(), i);
			}
		}
	}

	private void getStartText(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&m----------------------&r&e&l[&r &6&lQuest &r&e&l]&r&e&m----------------------"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l  You have started the quest - &6" + this.getName()));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r&e&m-----------------------------------------------------"));

		//				"\n\n&e&l  You have started the quest - &6" + this.getName() + "\n\n"+
//		"&r&e&m-----------------------------------------------------"
	}
	
	private void getCompleteText(Player p) {

		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&e&m----------------------&r&e&l[&r &6&lQuest &r&e&l]&r&e&m----------------------"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l  You have completed the quest - &6" + this.getName()));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r&e&m-----------------------------------------------------"));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Step> arrayList) {
		this.steps = arrayList;
	}

	public ArrayList<ItemStack> getRewards() {
		return rewards;
	}

	public void setRewards(ArrayList<ItemStack> rewards) {
		this.rewards = rewards;
	}

	public Integer[] getReqQuests() {
		return reqQuests;
	}

	public void setReqQuests(Integer[] reqQuests) {
		this.reqQuests = reqQuests;
	}

	public int getReqLevel() {
		return reqLevel;
	}

	public void setReqLevel(int reqLevel) {
		this.reqLevel = reqLevel;
	}

	public Step getLastStep() {
		return getSteps().get(getSteps().size() - 1);
	}

	public void addStep(Step s) {
		getSteps().add(s);
	}

	public void addStepScore(int i,Step step, PlayerData pd) {
		Player p = pd.getPlayer();
		String[] splitStep = pd.inProgressQuests.get(this).split("-");
		Integer stepID = Integer.parseInt(splitStep[0]);
		Integer stepScore = Integer.parseInt(splitStep[1]);
		if (getSteps().indexOf(step) == stepID) {
			// TODO store in db as "[QuestID]:[StepID]-[Score]"

			if ((stepScore + i) >= step.getScore()) {
				// finish step
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', step.getComplete()));
				if (this.getLastStep() == step) {
					this.completeQuest(p);
					return;
				}
				return;
			} else {
				pd.inProgressQuests.put(this, stepID + "-" + (stepScore + i));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"   &aQuest Objective &7[" + (stepScore + i) + "/" + step.getScore() + "]"));
				return;
			}

		}
		return;
	}

	public ArrayList<Quest> getReqQuestsArray() 
	{
		if (getReqQuests() == null) {
			return null;
		}
		
		ArrayList<Quest> quests = new ArrayList<Quest>();
		for (int i : getReqQuests()) {
			QuestManager qm = (QuestManager) ManagerInstances.getInstance(QuestManager.class);
			quests.add(qm.getQuest(i));
		}
		return quests;
	}

	public boolean canStart(Player player) 
	{
		PlayerData pd = RPGCore.plugin.getPD(player);
		QuestManager qm = (QuestManager) ManagerInstances.getInstance(QuestManager.class);

		for (int i : getReqQuests()) {
			Quest q = qm.getQuest(i);

			if (!pd.completedQuests.contains(q)) {
				return false;
			}
		}
		
//		if (pd.get) < getReqLevel()) {
//			return false;
//		}
		
		return true;
	}

	public QuestLine getqLine() {
		return qLine;
	}

	public void setqLine(QuestLine qLine) {
		this.qLine = qLine;
	}
}
