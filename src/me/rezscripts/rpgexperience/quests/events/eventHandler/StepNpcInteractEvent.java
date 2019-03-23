package me.rezscripts.rpgexperience.quests.events.eventHandler;

import me.rezscripts.rpgexperience.PlayerData;
import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.quests.Quest;
import me.rezscripts.rpgexperience.quests.Step;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

    public class StepNpcInteractEvent implements Listener {
        Quest q;
        private Step step;
        private boolean first;
        private String name;

        public StepNpcInteractEvent(boolean b, Quest q, Step step, String args) {
            setName(args);
            setStep(step);
            setFirst(b);
            setQ(q);
        }

        @EventHandler
        public void onEvent(PlayerInteractEntityEvent e) {
            if (e.getRightClicked() == null) {
                return;
            }

            if (e.getRightClicked().getName() == null) {
                return;
            }
            if (e.getRightClicked().getName().contains(getName())) {
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

        private String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }
    }
