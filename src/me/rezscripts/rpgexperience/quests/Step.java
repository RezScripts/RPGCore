package me.rezscripts.rpgexperience.quests;

import me.rezscripts.rpgexperience.RPGCore;
import me.rezscripts.rpgexperience.quests.events.InteractionType;
import me.rezscripts.rpgexperience.quests.events.eventHandler.StepBlockInteractEvent;
import me.rezscripts.rpgexperience.quests.events.eventHandler.StepNpcInteractEvent;
import me.rezscripts.rpgexperience.quests.events.eventHandler.StepOnCommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;


public class Step implements Listener {
    // Step object - takes interactionType (enum), score (1 = 1 time event),
    // string[] args (for Npc vars ect), String - Desc, String - completion
    // text(edited)

    private InteractionType interactType;
    private int score;
    private String desc, start, complete;
    private String args;
    private Quest q;
    private boolean first;

    Step(InteractionType ie, boolean first, int score, Quest q, String desc, String start, String complete,
         String args) {
        setInteractType( ie );
        setScore( score );
        setDesc( desc );
        setStart( start );
        setComplete( complete );
        setArgs( args );
        setFirst( first );
        setQ( q );
    }

    public InteractionType getInteractType() {
        return interactType;
    }

    public void setInteractType(InteractionType interactType) {
        this.interactType = interactType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void register() {
        if (getQ() == null) {
            RPGCore.plugin.onDisable();
            return;
        }
        switch (getInteractType()) {
            default:
                Bukkit.getConsoleSender().sendMessage( "Error setting up step with desc: " + getDesc() );
                Bukkit.getServer().shutdown();
                break;
            case NpcInteract:
                Bukkit.getServer().getPluginManager().registerEvents( (Listener) new
                                StepNpcInteractEvent( isFirst(), getQ(), this, args ),
                        (Plugin) RPGCore.plugin );
                break;
            case BlockInteract:
                Bukkit.getServer().getPluginManager().registerEvents(
                        (Listener) new StepBlockInteractEvent( isFirst(), getQ(), this, args ), (Plugin) RPGCore.plugin );
                break;
            case CommandIsuue:
                Bukkit.getServer().getPluginManager().registerEvents( (Listener)  new StepOnCommandEvent(isFirst(), getQ(), this, args), (Plugin) RPGCore.plugin);
                break;
        }
    }

    public Quest getQ() {
        return q;
    }

    public void setQ(Quest q) {
        this.q = q;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

}
