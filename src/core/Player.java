package core;

/**
 * Represents a single participant in the game.
 * Stores player-specific data such as their ID and chosen Kingdom.
 */
public class Player {
	private int PLAYER_ID = -1;
	private String kingdom = "debug";
	private String name = "";
	
	public Player(String name) {
		
	}
	public String getKingdom() {
		return this.kingdom;
	}
	
}
