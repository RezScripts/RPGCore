package me.rezscripts.rpgexperience.quests.events.eventHandler;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.quests.Quest;
import me.rezscripts.rpgexperience.quests.Step;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class StepOnCommandEvent implements Listener {
    Quest q;
    private Step step;
    private boolean first;
    private String command;

    public StepOnCommandEvent(boolean b, Quest q, Step step, String args) {
        setCommand(args);
        setStep(step);
        setFirst(b);
        setQ(q);
    }

    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent e) {
        if (e.getMessage() == null){
            return;
        }

        if (e.getMessage().replaceAll("/", "").equalsIgnoreCase(getCommand()))
        {
            PlayerData pd = RPGCore.plugin.getPD(e.getPlayer());
            if (pd.inProgressQuests.containsKey(getQ())) {
                getQ().addStepScore(1, getStep(), pd);
            } else if (getStep().isFirst() && !pd.completedQuests.contains(getQ())
                    && !pd.inProgressQuests.containsKey(getQ())) {
                if (getQ().getReqQuests() == null) {
                    getQ().startQuest(e.getPlayer());
                } else {
                    if (getQ().canStart(e.getPlayer())) {
                        getQ().startQuest(e.getPlayer());
                    }
                    return;
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
