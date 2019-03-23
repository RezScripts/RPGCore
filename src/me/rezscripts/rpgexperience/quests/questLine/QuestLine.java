package me.rezscripts.rpgexperience.quests.questLine;

import me.rezscripts.rpgexperience.quests.Quest;

import java.util.ArrayList;

public class QuestLine {

    private String Name;
    private int ID;
    private ArrayList<Quest> quests;

    public QuestLine(String name, int ID){
        setQuests(new ArrayList<Quest>());
        setName( name );
        setID( ID );
    }

    public void addQues(Quest q){
        getQuests().add( q );
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
