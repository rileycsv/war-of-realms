package core;

import entities.*;

/**
 * Represents a single participant in the game.
 * Stores player-specific data such as their ID and chosen Kingdom.
 */
public class Player {
	private String kingdom = "debug";

	public Player(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getKingdom() {
		return this.kingdom;
	}
	
}
