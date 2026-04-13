package core;

import entities.*;

/**
 * A global tracker for the current state of the game.
 * Holds references to the players and dictates whose turn it is.
 */
public class GameManager {
	public static Player[] players = {};
	private static int activePlayer = -1;
	private static Unit selectedUnit = null;
	
	// =====================================================
	// Selection Management
	// =====================================================
	
	public static void setSelectedUnit(Unit unit) {
		selectedUnit = unit;
	}
	
	public static Unit getSelectedUnit() {
		return selectedUnit;
	}
	
	public static void clearSelection() {
		selectedUnit = null;
	}

	// =====================================================
	// Utility Methods - Unit Queries
	// =====================================================

	/**
	 * Returns the unit occupying a tile, or null if empty.
	 */
	public static Unit getUnitAtTile(int row, int col) {
		for (Player player : players) {
			Unit unit = player.getUnitAtPosition(row, col);
			if (unit != null) {
				return unit;
			}
		}
		return null;
	}

	/**
	 * Returns whether a tile is blocked by a unit (allied or enemy).
	 */
	public static boolean isTileBlocked(int row, int col) {
		return getUnitAtTile(row, col) != null;
	}

	/**
	 * Returns whether a tile is blocked by an ENEMY unit.
	 */
	public static boolean isEnemyAtTile(int row, int col, Unit unit) {
		Unit blocker = getUnitAtTile(row, col);
		return blocker != null && blocker.getPlayerID() != unit.getPlayerID();
	}

	/**
	 * Returns whether a tile is blocked by an ALLIED unit.
	 */
	public static boolean isAllyAtTile(int row, int col, Unit unit) {
		Unit blocker = getUnitAtTile(row, col);
		return blocker != null && blocker.getPlayerID() == unit.getPlayerID();
	}
	
	// =====================================================
	// Player & Turn Management
	// =====================================================

	public static int getActivePlayer() {
		return activePlayer;
	}

	public static void setActivePlayer(int id) {
		if (id >= 0 && id < players.length) {
			activePlayer = id;
		}
	}
	
	public static void startGame() {

	}
	
	/**
	 * Ends the current player's turn and advances to the next player. Also resets unit movement for the player ending their turn.
	 * Called by the "End Turn" button in the UI.
	 */
	public static void endTurn() {
		// End the current player's turn
		players[activePlayer].endTurn();
		nextTurn();
	}
	
	private static void nextTurn() {
		if (activePlayer < players.length - 1) {
			activePlayer++; // Move to the next player
		} else {
			activePlayer = 0; // Loop back to the first player
		}
	}

	/**
	 * Returns the player ID (as int) for a given unit.
	 * Converts Unit.getPlayerID() (boolean) to int (0 or 1).
	 */
	public static int getPlayerOfUnit(Unit unit) {
		return unit.getPlayerID();
	}
}
