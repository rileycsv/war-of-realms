package core;

import entities.*;
import environment.Board;
import environment.Boards;
import utils.Debug;

/**
 * Global tracker for the current state of the game.
 *
 * GameManager is a static utility class — all fields and methods are static
 * because there is only ever one game running at a time. It is responsible for:
 * 
 *   Tracking whose turn it is and advancing turns
 *   Detecting the setup phase (negative turn count)
 *   Querying the board for units by tile position
 *   Maintaining the currently selected unit
 * 
 *
 * Turn numbering starts at {@code -players.length} so that each player gets
 * one "setup turn" before normal gameplay begins. Once the count reaches 0 the
 * game is live.
 */
public class GameManager {
	private static int TURN_COUNT;
	/** The two players in the current game. Index 0 = Player 1, index 1 = Player 2. */
	public static Player[] players = new Player[2];
	private static int activePlayerID = -1;
	private static Unit selectedUnit = null;
	
	private static boolean[][] unitCanMoveTo = null; 
	public static boolean[][] getUnitCanMoveTo() {
		return unitCanMoveTo;
	}
	private static boolean[][] unitCanAttack = null;
	public static boolean[][] getUnitCanAttack() {
		if(selectedUnit.getHasAttackedThisTurn()) {
			// If the unit has already attacked this turn, return a blank grid of false values
			return new boolean[unitCanAttack.length][unitCanAttack[0].length]; 
		}
		return unitCanAttack;
	}

	// =====================================================
	// Selection Management
	// =====================================================

	/**
	 * Sets the unit that the active player has selected.
	 *
	 * unit = The unit to select.
	 */
	public static void setSelectedUnit(Unit unit) {
		unitCanMoveTo = unit.canMoveTo();
		unitCanAttack = unit.canAttack();
		Debug.log(3, "Selected unit: " + unit);
		selectedUnit = unit;
	}

	/**
	 * Returns the currently selected unit, or {@code null} if nothing is selected.
	 */
	public static Unit getSelectedUnit() {
		if (selectedUnit == null) {
			Debug.log(3, "No unit currently selected");
		}		
		return selectedUnit;
	}

	/**
	 * Clears the current unit selection.
	 */
	public static void clearSelection() {
		Debug.log(3, "Clearing selected unit");
		selectedUnit = null;
	}

	// =====================================================
	// Utility Methods - Unit Queries
	// =====================================================

	/**
	 * Returns the unit occupying a given tile, or {@code null} if the tile is empty.
	 *
	 * row = The row of the tile to check.
	 * col = The column of the tile to check.
	 */
	public static Unit getUnitAtTile(int row, int col) {
		return Boards.getUnitAtTile(row, col);
	}

	/**
	 * Returns {@code true} if any unit (ally or enemy) occupies the given tile.
	 *
	 * row = The row to check.
	 * col = The column to check.
	 */
	public static boolean isTileBlocked(int row, int col) {
		return getUnitAtTile(row, col) != null;
	}

	/**
	 * Returns {@code true} if an enemy unit (a unit belonging to a different player
	 * than {@code unit}) occupies the given tile.
	 *
	 * row =  The row to check.
	 * col =  The column to check.
	 * unit = The reference unit used to determine ownership.
	 */
	public static boolean isEnemyAtTile(int row, int col, Unit unit) {
		Unit blocker = getUnitAtTile(row, col);
		return blocker != null && blocker.getPlayerID() != unit.getPlayerID();
	}

	/**
	 * Returns {@code true} if an allied unit (a unit belonging to the same player
	 * as {@code unit}) occupies the given tile.
	 *
	 * row =  The row to check.
	 * col =  The column to check.
	 * unit = The reference unit used to determine ownership.
	 */
	public static boolean isAllyAtTile(int row, int col, Unit unit) {
		Unit blocker = getUnitAtTile(row, col);
		return blocker != null && blocker.getPlayerID() == unit.getPlayerID();
	}

	// =====================================================
	// Player & Turn Management
	// =====================================================

	/**
	 * Returns the ID of the player whose turn it currently is.
	 */
	public static int getActivePlayerID() {
		return activePlayerID;
	}

	/**
	 * Initialises turn tracking and determines the first active player.
	 *
	 * The turn count is set to {@code -players.length} so that each player
	 * gets one setup turn (for unit placement) before the regular game begins.
	 * Call this once after both players have been created.
	 */
	public static void startGame() {
		// Set TURN_COUNT to a negative number based on the amount of players
		// This is because the first few turns are "setup turns" where players place their units before the first player gets to move
		TURN_COUNT = -players.length;

		// Determine the first active player ID
		updateActivePlayer();
	}

	/**
	 * Ends the active player's turn, resets their units' movement, and advances to
	 * the next player. Also clears any current unit selection.
	 *
	 * Safe to call before the game has started — it checks bounds before
	 * touching the players array.
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
	 * Returns the current turn count. Negative values indicate the setup phase;
	 * zero and above indicate normal gameplay.
	 */
	public static int getTurnCount() {
		return TURN_COUNT;
	}

	/**
	 * Returns {@code true} if the game is still in the setup phase, where players
	 * are placing their initial units. The setup phase lasts while the turn count
	 * is negative (one setup turn per player).
	 */
	public static boolean isSetupTurn() {
		return TURN_COUNT < 0;
	}

	/**
	 * Recalculates and stores the active player ID from the current turn count.
	 * Uses {@link Math#floorMod} so negative turn counts (setup phase) wrap
	 * correctly around the player array.
	 */
	private static void updateActivePlayer() {
		if (players.length > 0) {
			activePlayerID = Math.floorMod(TURN_COUNT, players.length);
		}
	}

	/**
	 * Returns the player ID for a given unit as an {@code int}.
	 *
	 * unit = The unit to query.
	 */
	public static int getPlayerOfUnit(Unit unit) {
		return unit.getPlayerID();
	}

	/**
	 * Returns the {@link Player} object for the player whose turn it currently is.
	 */
	public static Player getActivePlayer() {
		return players[activePlayerID];
	}

	/**
	 * Searches every tile on the board to find where the given unit is located.
	 *
	 * unit = The unit to locate.
	 */
	public static int[] getPositionOfUnit(Unit unit) {
		if (unit != null && unit.getX() >= 0 && unit.getY() >= 0) {
			return new int[] {unit.getX(), unit.getY()};
		}
		Debug.log(2, "Warning: Unit has invalid position.");
		return new int[] {-1, -1};
	}

	public static void moveUnit(Unit unit, int row, int col) {
		Boards.getUnitsBoard()[unit.getX()][unit.getY()] = null; // Clear the unit's current position on the board
		Boards.getUnitsBoard()[row][col] = unit; 
		unit.setPos(row, col); // Update the unit's internal position
	}
	
	public static void unitAttacks(Unit defender) {
		if (selectedUnit == null) {
			Debug.log(2, "Error: No unit selected for attack.");
			return;
		}
		
		if (selectedUnit.getHasAttackedThisTurn()) {
			Debug.log(2, "Selected unit has already attacked this turn.");
			return;
		}
		
		// Deal damage to the defender
		int damage = selectedUnit.getAttackDamage();
		defender.receiveDamage(damage);
		
		// Unit can no longer move after attacking
		selectedUnit.hasAttackedThisTurn();
		selectedUnit.spendAllMovement();
	}
	
	/**
	 * Removes a unit from the board and checks if the player has lost all of their units.
	 */
	public static void removeUnit(Unit unit) {
		// 1. Instantly remove the unit using its known coordinates
		int x = unit.getX();
		int y = unit.getY();
		
		if (Boards.getUnitAtTile(x, y) != null && Boards.getUnitAtTile(x, y).getUnitID() == unit.getUnitID()) {
			Boards.getUnitsBoard()[x][y] = null; 
			Debug.log(2, "Unit removed: " + unit);
		}

		// 2. Check if the player has any units left
		boolean hasUnitsLeft = false;
		for (Unit[] row : Boards.getUnitsBoard()) {
			for (Unit u : row) {
				if (u != null && u.getPlayerID() == unit.getPlayerID()) {
					hasUnitsLeft = true;
					break; // Found one! No need to keep looking at this row
				}
			}
			if (hasUnitsLeft) break; // Stop checking the rest of the board
		}

		if (!hasUnitsLeft) {
			Board.showEndGameScreen();
			System.out.println("Player " + unit.getPlayerID() + " has lost all their units! Game Over.");
		}
	}
	
	public static Unit getUnitByID(int unitID) {
		for (int i = 0; i < Boards.getUnitsBoard().length; i++) {
			for (int j = 0; j < Boards.getUnitsBoard()[0].length; j++) {
				Unit unit = Boards.getUnitAtTile(i, j);
				if (unit != null && unit.getUnitID() == unitID) {
					return unit;
				}
			}
		}
		Debug.log(2, "Warning: No unit found with ID " + unitID);
		return null; // Return null if no matching unit is found
	}
}
