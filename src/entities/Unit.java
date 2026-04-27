package entities;

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
	protected boolean hasAttackedThisTurn = false;

	// The unit's current grid position
	protected int[] pos = {-1, -1}; // {row, col}

	public Unit(int PID, int UID, String kingdom, int x, int y) {
		this.PLAYER_ID = PID;
		this.unitID = UID;
		GameManager.players[PID].addUnitID(UID);
		this.kingdom = kingdom;
		this.UNIT_TYPE = "../debug.png";

		this.pos[0] = x;
		this.pos[1] = y;
		// Sprite is NOT loaded here — UNIT_TYPE is set by the subclass constructor
		// after super() returns, so loading here would always use "../debug.png".
	}
	
	public boolean getHasAttackedThisTurn() {
		return this.hasAttackedThisTurn;
	}
	
	public void hasAttackedThisTurn() {
		this.hasAttackedThisTurn = true;
	}
	
	public int getAttackDamage() {
		return this.attackDamage;
	}
	
	public void receiveDamage(int damage) {
		this.currentHealth -= damage;
		if (this.currentHealth <= 0) {
			GameManager.removeUnit(this);
		}
	}

	public boolean canAttackTile(int i, int j) {
		return canAttack()[i][j];
	}
	
	public boolean canMoveToTile(int i, int j) {
		return canMoveTo()[i][j];
	}
	
	/** Spends all remaining movement points (called after the unit attacks). */
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
	
	public void setPos(int row, int col) {
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
		this.hasAttackedThisTurn = false;
	}

	@Override
	public String toString() {
		return String.format("Unit{type: %s, playerID: %d, pos: (%d,%d), health: %d/%d}%n", UNIT_TYPE, PLAYER_ID, pos[0], pos[1], currentHealth, health);
	}
	
	/**
	 * Determines what tiles a unit can move to based on its current movement points and the terrain costs.
	 * Uses a BFS flood fill algorithm to explore reachable tiles.
	 * @return
	 */
	public boolean[][] canMoveTo() {
		boolean debugInstakillOnClick = false; // Set to true to test unit death and removal from board
		if (Debug.isEnabled() && debugInstakillOnClick) {
			receiveDamage(10);; // Temporary hack to test unit death and removal from board
		}
		char[][] board = Board.getBoard();
		int[][] boardCosts = Board.getBoardCosts();
		int rows = board.length;
		int cols = board[0].length;
		
		boolean[][] canMove = new boolean[rows][cols];
		boolean[][] visited = new boolean[rows][cols];
		
		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[]{pos[0], pos[1], currentMovement});
		visited[pos[0]][pos[1]] = true;
		canMove[pos[0]][pos[1]] = true;
		
		// 4-directional movement: up, down, left, right
		int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int row = current[0];
			int col = current[1];
			int remainingMovement = current[2];
			
			for (int[] dir : directions) {
				int newRow = row + dir[0];
				int newCol = col + dir[1];
				
				// Check bounds
				if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
					// Check if already visited
					if (!visited[newRow][newCol]) {
						int tileCost = boardCosts[newRow][newCol];
						
						// Check if we have enough movement to reach this tile
						if (remainingMovement >= tileCost) {
							visited[newRow][newCol] = true;
							canMove[newRow][newCol] = true;
							queue.add(new int[]{newRow, newCol, remainingMovement - tileCost});
						}
					}
				}
			}
		}
		
		return canMove;
	}
	
	/**
	 * Determines what tiles a unit can attack based on its attack range.
	 * Uses a BFS flood fill algorithm to explore tiles within attack range.
	 * @return A boolean array where each index represents a tile on the board, and the value is true if the unit can attack that tile, false otherwise.
	 */
	public boolean[][] canAttack() {
		char[][] board = Board.getBoard();
		int rows = board.length;
		int cols = board[0].length;
		
		boolean[][] canAttackTile = new boolean[rows][cols];
		boolean[][] visited = new boolean[rows][cols];
		
		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[]{pos[0], pos[1], 0});
		visited[pos[0]][pos[1]] = true;
		
		// 4-directional movement: up, down, left, right
		int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		
		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int row = current[0];
			int col = current[1];
			int distance = current[2];
			
			// Mark current tile as attackable if within range
			if (distance <= attackRange) {
				canAttackTile[row][col] = true;
			}
			
			// Continue exploring if we haven't reached max range
			if (distance < attackRange) {
				for (int[] dir : directions) {
					int newRow = row + dir[0];
					int newCol = col + dir[1];
					
					// Check bounds
					if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
						// Check if already visited
						if (!visited[newRow][newCol]) {
							visited[newRow][newCol] = true;
							queue.add(new int[]{newRow, newCol, distance + 1});
						}
					}
				}
			}
		}
		
		return canAttackTile;
	}
}
