package core;

import entities.*;

/**
 * Represents a single participant in the game.
 * Stores player-specific data such as their ID and chosen Kingdom.
 */
public class Player {
	private String kingdom = "debug";
	
	private int playerID = -1;
	
	private int selectedUnitIndex = -1; // Index of the currently selected unit in the UNITS array
	
	public Player(int ID, String kingdom, Unit[] units) {
		this.playerID = ID;
		this.kingdom = kingdom;
		this.UNITS = units;
	}
	
	public String getKingdom() {
		return this.kingdom;
	}
	
	private Unit[] UNITS = new Unit[8]; 
	
	public void endTurn() {
		// Reset movement for all units at the end of the turn
		for (Unit unit : UNITS) {
			if (unit != null) {
				unit.endTurn();
			}
		}
	}
	
	public Unit getActiveUnit() {
		return UNITS[selectedUnitIndex]; 
	}

	/**
	 * Returns the unit at the specified board position, or null if none.
	 */
	public Unit getUnitAtPosition(int row, int col) {
		for (Unit unit : UNITS) {
			if (unit != null && unit.getX() == row && unit.getY() == col) {
				return unit;
			}
		}
		return null;
	}
	public Unit[] getUnits() {
		return UNITS;
	}
}
