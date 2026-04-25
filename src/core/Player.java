package core;

import java.util.ArrayList;

import entities.*;
import environment.Board;
import javafx.scene.paint.Color;

/**
 * Represents a single participant in the game.
 * Stores player-specific data such as their ID and chosen Kingdom.
 */
public class Player {
	private String kingdom = "debug";
	
	public Color getKingdomColor() { Color ret = switch (kingdom) {
		case "Hussites"   -> Color.rgb(21, 64, 181);
		case "Portuguese" -> Color.rgb(38, 106, 40);
		case "Mongolians" -> Color.rgb(221, 200, 21);
		case "Aztec"	  -> Color.rgb(233, 120, 17);
		case "English"	-> Color.rgb(239, 28, 15);
		default		   -> Color.GRAY; // Fallback color for invalid kingdom
	}; return ret; }
	
	private ArrayList<Integer> unitIDs = new ArrayList<>();
	
	public int[] getUnitIDs() {
		int[] ret = new int[unitIDs.size()];
		for (int i = 0; i < unitIDs.size(); i++) {
			ret[i] = unitIDs.get(i); // Auto-unboxing converts Integer to int
		}
		return ret;
	}
	
	public void addUnitID(int unitID) {
		unitIDs.add(unitID);
	}
	
	public int getTotalHealth() {
        int totalHealth = 0;
        for (int unitID : unitIDs) { // Enhanced for-loop still works perfectly here
            Unit unit = GameManager.getUnitByID(unitID);
            if (unit != null) {
                totalHealth += unit.getHealth();
            }
        }
        return totalHealth;
    }
	
	public Unit[] getUnits(int x, int y) {
		return Kingdoms.getUnitsForKingdom(kingdom, playerID, new int[] {x, y});
	}
	
	private int playerID = -1;

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
	
}
