package core;

import entities.*;
import environment.Board;

/**
 * Represents a single participant in the game.
 * Stores player-specific data such as their ID and chosen Kingdom.
 */
public class Player {
	private String kingdom = "debug";
	
	private Kingdoms kingdomUnits = new Kingdoms();
	
	public Unit[] getUnits(int x, int y) {
		return Kingdoms.getUnitsForKingdom(kingdom, playerID, new int[] {x, y});
	}
	
	private int playerID = -1;
	
	private int[] selectedUnitIndex = {-1, -1}; // Index of the currently selected unit in the UNITS array
	
	public Player(int ID, String kingdom) {
		this.playerID = ID;
		this.kingdom = kingdom;
	}
	
	public String getKingdom() {
		return this.kingdom;
	}
	
	public void endTurn() {
		for (Unit[] row : Board.getUnitsBoard()) {
			for (Unit unit : row) {
				if (unit != null && unit.getPlayerID() == playerID) {
					unit.endTurn();
				}
			}
		}
	}
	
	public int[] getActiveUnit() {
		return selectedUnitIndex; 
	}
}
