package core;

import entities.*;
import environment.Board;
import utils.Debug;

/**
 * A global tracker for the current state of the game.
 * Holds references to the players and dictates whose turn it is.
 */
public class GameManager {
	private static int TURN_COUNT;
	public static Player[] players = new Player[2];
	private static int activePlayerID = -1;
	private static Unit selectedUnit = null;
	
	// =====================================================
	// Selection Management
	// =====================================================
	
	public static void setSelectedUnit(Unit unit) {
		Debug.log(3, "Selected unit: " + unit);
		selectedUnit = unit;
	}
	
	public static Unit getSelectedUnit() {
		return selectedUnit;
	}
	
	public static void clearSelection() {
		Debug.log(3, "Clearing selected unit");
		selectedUnit = null;
	}
	
	// =====================================================
	// Utility Methods - Unit Queries
	// =====================================================
	
	/**
	 * Returns the unit (if any) that is currently occupying a given tile on the
	 * board.
	 * 
	 * @param row // The coordinates of the tile to check.
	 * @param col // The coordinates of the tile to check.
	 * @return // The unit at the specified tile, or null if the tile is unoccupied.
	 */
	public static Unit getUnitAtTile(int row, int col) {
		return Board.getUnitAtTile(row, col);
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

	public static int getActivePlayerID() {
		return activePlayerID;
	}
	
	public static void startGame() {
		// Set TURN_COUNT to a negative number based on the amount of players
		// This is because the first few turns are "setup turns" where players place their units before the first player gets to move
		TURN_COUNT = -players.length; 
		
		// Determine the first active player ID
		updateActivePlayer(); 
	}

	/**
	 * Ends the current player's turn and advances to the next player. Also resets
	 * unit movement for the player ending their turn.
	 * Called by the "End Turn" button in the UI.
	 */
	public static void endTurn() {
		Debug.log(2, "Ending turn for player " + activePlayerID);
		// Null and bounds check just in case endTurn is clicked before game starts
		if (activePlayerID >= 0 && activePlayerID < players.length && players[activePlayerID] != null) {
			players[activePlayerID].endTurn();
			clearSelection(); // Clear any selected unit at the end of the turn
		}
		
		TURN_COUNT++;
		updateActivePlayer();
	}
	
	/**
	 * Returns the current turn count
	 * @return
	 */
	public static int getTurnCount() {
		return TURN_COUNT;
	}
	
	/**
	 * Returns true if the game is currently in the setup phase (turn count < 0), where players are placing their initial units on the board
	 *  and false if the game has started (turn count >= 0)
	 * @return
	 */
	public static boolean isSetupTurn() {
		return TURN_COUNT < 0;
	}

	/**
	 * Calculates and sets the activePlayerID based strictly on the TURN_COUNT.
	 * Automatically handles wrapping and negative setup turns using floorMod.
	 */
	private static void updateActivePlayer() {
		if (players.length > 0) {
			activePlayerID = Math.floorMod(TURN_COUNT, players.length);
		}
	}

	/**
	 * Returns the player ID (as int) for a given unit.
	 * Converts Unit.getPlayerID() (boolean) to int (0 or 1).
	 */
	public static int getPlayerOfUnit(Unit unit) {
		return unit.getPlayerID();
	}

	private static void nextTurn() {
		if (activePlayerID < players.length - 1) {
			activePlayerID++; // Move to the next player
		} else {
			activePlayerID = 0; // Loop back to the first player
		}
	}

    public static Player getActivePlayer() {
        return players[activePlayerID];
    }
	
	/**
	 * Searches the board for the specified unit and returns its current position as an array [row, col].
	 * Returns null if the unit is not found on the board.
	 * @param unit
	 * @return
	 */
	public static int[] getPositionOfUnit(Unit unit) {
		for(int i = 0; i < Board.getBoard().length; i++) {
			for(int j = 0; j < Board.getBoard()[0].length; j++) {
				if(Board.getUnitAtTile(i, j) == unit) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}
}
