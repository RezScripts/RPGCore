package me.rezscripts.rpgexperience.quests.events;

public enum InteractionType {
	
	/*\
	 * 
	 * ID list
	 * 
	 * 0 = Player Interact Event
	 * 1 = Player Block Interact
	 * 2 = Block Break
	 * 3 = Entity kill
	 * 
	\*/
	
	NpcInteract(0),
	BlockInteract(1),
	CommandIsuue(2),
	PickUpItem(3);

	private int ID;
	
	InteractionType(int id) {
		setID(id);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
