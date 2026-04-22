package entities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import core.GameManager;
import environment.Board;
import javafx.scene.image.Image;
import utils.Debug;

/**
 * Represents a controllable character or entity on the board.
 * Tracks base stats (health, movement, attack) and current board position.
 */
public abstract class Unit {
	// The ID of the player that owns the unit
	protected int PLAYER_ID = -1;
	private int unitID = -1; // Unique identifier for the unit
	protected String UNIT_TYPE;
	private Image sprite;
	private String kingdom = "debug";

	// Unit base stats
	protected int maxMovement;
	protected int health;
	protected int attackDamage;
	protected int attackRange;

	// Game state stats (These fluctuate during gameplay)
	protected int currentHealth;
	protected int currentMovement;

	// The unit's current grid position
	protected int[] pos = {-1, -1}; // {row, col}

	public Unit(int PID, int UID, String kingdom, int x, int y) {
		this.PLAYER_ID = PID;
		this.unitID = UID;
		this.kingdom = kingdom;
		this.UNIT_TYPE = "../debug.png";

		this.pos[0] = x;
		this.pos[1] = y;
		// Sprite is NOT loaded here — UNIT_TYPE is set by the subclass constructor
		// after super() returns, so loading here would always use "../debug.png".
	}
	
    /**
     * Determines what tiles a unit can move to based on its movement range.
	 * @param row
	 * @param col
	 * @return A boolean array where each index represents a tile on the board, and the value is true if the unit can move to that tile, false otherwise.
	 */
	public abstract boolean[][] canMoveTo();
	
	/**
	 * Determines what tiles a unit can attack based on its attack range.
	 * @param row
	 * @param col
	 * @return A boolean array where each index represents a tile on the board, and the value is true if the unit can attack that tile, false otherwise.
	 */
	public abstract boolean[][] canAttack();

	public boolean canAttackTile(int i, int j) {
		return canAttack()[i][j];
	}
	
	public boolean canMoveToTile(int i, int j) {
		return canMoveTo()[i][j];
	}
	
	/** Spends all remaining movement points (called after the unit moves). */
	public void spendAllMovement() {
		this.currentMovement = 0;
	}
	
	public int getPlayerID() {
		return this.PLAYER_ID;
	}
	
	public int getUnitID() {
		return this.unitID;
	}
	
	public int getHealth() { return this.currentHealth; }
	public int getMaxHealth() { return this.health; }
	/**
	 * Returns the row (x) coordinate of the unit.
	 */
	public int getX() {
		return getPos()[0];
	}

	/**
	 * Returns the column (y) coordinate of the unit.
	 */
	public int getY() {
		return getPos()[1];
	}

	/**
	 * Returns the unit's current grid position as {row, col}.
	 */
	public int[] getPos() {
		Debug.log(3, String.format("Unit.getPos() called for %s/%s at (%d,%d)", kingdom, UNIT_TYPE, pos[0], pos[1]));
		return this.pos;
	}

	public void moveTo(int row, int col) {
		this.pos[0] = row;
		this.pos[1] = col;
	}

	/**
	 * Retrieves the dynamic path for this unit's sprite based on their owning
	 * player's kingdom.
	 * IMPORTANT: This assumes a specific file structure:
	 * /assets/kingdoms/{kingdomName}/{UNIT_TYPE}
	 *
	 * @return The file path string for the sprite image.
	 */
	public String getSpritePath() {
		return String.format("file:assets/kingdoms/%s/%s", kingdom.toLowerCase(), UNIT_TYPE);
	}

	public Image getImage() {
		if (sprite == null || sprite.isError()) {
			sprite = new Image(getSpritePath());
		}
		return this.sprite;
	}

	/**
	 * Resets currentMovement to maxMovement at the start of the next turn.
	 */
	public void endTurn() {
		this.currentMovement = this.maxMovement;
	}

	@Override
	public String toString() {
		return String.format("Unit{type: %s, playerID: %d, pos: (%d,%d), health: %d/%d}%n", UNIT_TYPE, PLAYER_ID, pos[0], pos[1], currentHealth, health);
	}
}
