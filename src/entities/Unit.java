package entities;

import core.GameManager;
import environment.Board;
import javafx.scene.image.Image;
import utils.Debug;

// Surpresses warnings for variables that are unused.
@SuppressWarnings("unused")

/**
 * Represents a controllable character or entity on the board.
 * Tracks base stats (health, movement, attack) and current board position.
 */
public abstract class Unit {
	// The ID of the player that owns the unit
	protected int PLAYER_ID = -1; 
	protected String UNIT_TYPE = "debug.png";
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

	protected int[] attacking = {-1, -1}; // {row, col} of the unit this unit is currently attacking

	public Unit(int PID, String kingdom, int x, int y) {
		this.PLAYER_ID = PID;
		this.kingdom = kingdom;

		// Set the unit's initial position on the grid
		this.pos[0] = x;
		this.pos[1] = y;

		// Load the sprite image based on the unit type and kingdom
		sprite = new Image(getSpritePath());
	}

	public int getPlayerID() {
		return this.PLAYER_ID;
	}

	public int getHealth() { return this.health; }
	public int getX() { return this.pos[0]; }
	public int getY() { return this.pos[1]; }

	public int[] getPos() {
		return this.pos;
	}

	private void moveTo(int row, int col) {
		this.pos[0] = row;
		this.pos[1] = col;
	}

	/**
	 * Determines if the unit can move to the specified grid position based on its
	 * movement rules and the current game state.
	 * @param row
	 * @param col
	 * @return A boolean array where each tile represents a tile on the board, and the value is true if the unit can move to that tile, false otherwise.
	 */
	public abstract boolean[][] canMoveTo(int row, int col);
	/**
	 * Determines if the unit can attack a target at the specified grid position based on its attack rules and the current game state.
	 * This typically checks if the target is within the unit's attack range and if there are any obstacles in the way.
	 * @param row
	 * @param col
	 * @return A boolean array where each tile represents a tile on the board, and the value is true if the unit can attack that tile, false otherwise.
	 */
	public abstract boolean[][] canAttack(int row, int col);

	/**
	 * Retrieves the dynamic path for this unit's sprite based on their owning
	 * player's kingdom.
	 * IMPORTANT: This assumes a specific file structure:
	 * /assets/kingdoms/{kingdomName}/{UNIT_TYPE}
	 * 
	 * @return The file path string for the sprite image.
	 */
	public String getSpritePath() {

		return String.format("/assets/units/%s/%s", kingdom, UNIT_TYPE);
	}

	public Image getImage() {
		return this.sprite;
	}

	/**
	 * Resets currentMovement to the maxMovement speed at the start of the turn.
	 */
	public void endTurn() {
		this.currentMovement = this.maxMovement;
	}

    public boolean canAttackTile(int i, int j) {
		return canAttack(i, j)[i][j];
	}

    public boolean canMoveToTile(int i, int j) {
		return canMoveTo(i, j)[i][j];
	}
	
	@Override
	public String toString() {
		return String.format("Unit{type: %s, playerID: %d, pos: (%d,%d), health: %d/%d}%n", UNIT_TYPE, PLAYER_ID, pos[0], pos[1], currentHealth, health);
	}
}
