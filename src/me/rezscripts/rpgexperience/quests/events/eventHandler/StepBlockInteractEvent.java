package me.rezscripts.rpgexperience.quests.events.eventHandler;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.quests.Quest;
import me.rezscripts.rpgexperience.quests.Step;
import me.rezscripts.rpgexperience.utils.RUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;



public class StepBlockInteractEvent implements Listener {
	Quest q;
	Location loc;
	private Step step;
	private boolean first;

	public StepBlockInteractEvent(boolean b, Quest q, Step step, String args) {
		setLoc(RUtils.getLocationString(args));
		setStep(step);
		setFirst(b);
		setQ(q);
	}

	@EventHandler
	public void onEvent(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) {
			return;
		}
		if (e.getClickedBlock().getType().equals(Material.AIR)) {
			return;
		}
		if (e.getClickedBlock().getLocation().equals(getLoc())) {
			PlayerData pd = RPGCore.plugin.getPD(e.getPlayer());
			
			if (pd.inProgressQuests.containsKey(getQ())) {
				getQ().addStepScore(1, getStep(), pd);
				return;
			}else if (getStep().isFirst() && !pd.completedQuests.contains(getQ())
					&& !pd.inProgressQuests.containsKey(getQ())) {
				if (getQ().getReqQuests() == null) {
					getQ().startQuest(e.getPlayer());
					return;
				}else {
					if (getQ().canStart(e.getPlayer())) {
						getQ().startQuest(e.getPlayer());
						return;
					}
				}
			}
		}
	}

	public Quest getQ() {
		return q;
	}

	public void setQ(Quest q) {
		this.q = q;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
}
