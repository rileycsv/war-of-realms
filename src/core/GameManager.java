package core;

import entities.*;
import environment.Board;
import utils.Debug;

/**
 * Global tracker for the current state of the game.
 *
 * <p>GameManager is a static utility class — all fields and methods are static
 * because there is only ever one game running at a time. It is responsible for:
 * <ul>
 *   <li>Tracking whose turn it is and advancing turns</li>
 *   <li>Detecting the setup phase (negative turn count)</li>
 *   <li>Querying the board for units by tile position</li>
 *   <li>Maintaining the currently selected unit</li>
 * </ul>
 *
 * <p>Turn numbering starts at {@code -players.length} so that each player gets
 * one "setup turn" before normal gameplay begins. Once the count reaches 0 the
 * game is live.
 */
public class GameManager {
	private static int TURN_COUNT;
	/** The two players in the current game. Index 0 = Player 1, index 1 = Player 2. */
	public static Player[] players = new Player[2];
	private static int activePlayerID = -1;
	private static Unit selectedUnit = null;

	// =====================================================
	// Selection Management
	// =====================================================

	/**
	 * Sets the unit that the active player has selected.
	 *
	 * @param unit The unit to select.
	 */
	public static void setSelectedUnit(Unit unit) {
		Debug.log(3, "Selected unit: " + unit);
		selectedUnit = unit;
	}

	/**
	 * Returns the currently selected unit, or {@code null} if nothing is selected.
	 *
	 * @return The selected unit, or {@code null}.
	 */
	public static Unit getSelectedUnit() {
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
	 * @param row The row of the tile to check.
	 * @param col The column of the tile to check.
	 * @return The unit at {@code (row, col)}, or {@code null} if unoccupied.
	 */
	public static Unit getUnitAtTile(int row, int col) {
		return Board.getUnitAtTile(row, col);
	}

	/**
	 * Returns {@code true} if any unit (ally or enemy) occupies the given tile.
	 *
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return {@code true} if the tile is occupied.
	 */
	public static boolean isTileBlocked(int row, int col) {
		return getUnitAtTile(row, col) != null;
	}

	/**
	 * Returns {@code true} if an enemy unit (a unit belonging to a different player
	 * than {@code unit}) occupies the given tile.
	 *
	 * @param row  The row to check.
	 * @param col  The column to check.
	 * @param unit The reference unit used to determine ownership.
	 * @return {@code true} if the tile holds an enemy of {@code unit}.
	 */
	public static boolean isEnemyAtTile(int row, int col, Unit unit) {
		Unit blocker = getUnitAtTile(row, col);
		return blocker != null && blocker.getPlayerID() != unit.getPlayerID();
	}

	/**
	 * Returns {@code true} if an allied unit (a unit belonging to the same player
	 * as {@code unit}) occupies the given tile.
	 *
	 * @param row  The row to check.
	 * @param col  The column to check.
	 * @param unit The reference unit used to determine ownership.
	 * @return {@code true} if the tile holds an ally of {@code unit}.
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
	 *
	 * @return The active player ID (0 or 1), or {@code -1} before the game starts.
	 */
	public static int getActivePlayerID() {
		return activePlayerID;
	}

	/**
	 * Initialises turn tracking and determines the first active player.
	 *
	 * <p>The turn count is set to {@code -players.length} so that each player
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
	 * <p>Safe to call before the game has started — it checks bounds before
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
	 *
	 * @return The current turn count.
	 */
	public static int getTurnCount() {
		return TURN_COUNT;
	}

	/**
	 * Returns {@code true} if the game is still in the setup phase, where players
	 * are placing their initial units. The setup phase lasts while the turn count
	 * is negative (one setup turn per player).
	 *
	 * @return {@code true} during the setup phase, {@code false} once gameplay begins.
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
	 * @param unit The unit to query.
	 * @return The player ID that owns the unit (0 or 1).
	 */
	public static int getPlayerOfUnit(Unit unit) {
		return unit.getPlayerID();
	}

	/**
	 * Returns the {@link Player} object for the player whose turn it currently is.
	 *
	 * @return The active player.
	 */
    public static Player getActivePlayer() {
        return players[activePlayerID];
    }

	/**
	 * Searches every tile on the board to find where the given unit is located.
	 *
	 * @param unit The unit to locate.
	 * @return An {@code int[]} of {@code {row, col}}, or {@code {-1, -1}} if the
	 *         unit is not found on the board.
	 */
	public static int[] getPositionOfUnit(Unit unit) {
		for(int i = 0; i < Board.getBoard().length; i++) {
			for(int j = 0; j < Board.getBoard()[0].length; j++) {
				Unit comp = Board.getUnitAtTile(i, j);
				if(comp == null) {
					continue; // Skip empty tiles
				}
				if(comp.getUnitID() == unit.getUnitID()) {
					return new int[] {i, j};
				}
			}
		}
		Debug.log(2, "Warning: Unit not found on board in GameManager.getPositionOfUnit. Returning invalid position.");
		return new int[] {-1, -1}; // Return an invalid position if the unit is not found
	}
}
