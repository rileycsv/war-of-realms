package entities;

import core.GameManager;
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
	public int PLAYER_ID = -1;
	public String UNIT_TYPE = "debug.png";
	private Image sprite;
	private String kingdom;
	
	// Unit base stats
    private int maxMovement;
	private int maxHealth;
    private int attackDamage;
    private int attackRange;
    
    // Game state stats (These fluctuate during gameplay)
    private int currentHealth;
	private int currentMovement;
    
    // The unit's current grid position
    private int row = -1;
    private int col = -1;

	public Unit(int PID, String kingdom) {
		this.PLAYER_ID = PID;
		this.kingdom = kingdom;
		sprite = new Image(getSpritePath());
	}
	
	/**
	 * Retrieves the dynamic path for this unit's sprite based on their owning player's kingdom.
	 * IMPORTANT: This assumes a specific file structure: /assets/units/{kingdomName}/{UNIT_TYPE}
	 * @return The file path string for the sprite image.
	 */
	public String getSpritePath() {
		return String.format("/assets/units/%s/%s", kingdom, UNIT_TYPE);
	}
	
	public Image getImage() {
		return sprite;
	}
	
	/**
	 * Resets currentMovement to the maxMovement speed at the start of the turn.
	 */
	public void turnStart() {
		this.currentMovement = this.maxMovement;
	}
}
