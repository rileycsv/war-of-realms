package entities;

public class Archer extends Unit {

    public Archer(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "archer.png";
        this.maxMovement = 3;
        this.health = 10;
        this.attackDamage = 3;
        this.attackRange = 1;
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
    
    /**
     * Determines what tiles a unit can move to based on its movement range.
	 * @param row
	 * @param col
	 * @return A boolean array where each index represents a tile on the board, and the value is true if the unit can move to that tile, false otherwise.
	 */
    @Override
    public boolean[][] canMoveTo() {
        throw new UnsupportedOperationException("Unimplemented method 'canMoveTo'");
    }
    
    /**
	 * Determines what tiles a unit can attack based on its attack range.
	 * @param row
	 * @param col
	 * @return A boolean array where each index represents a tile on the board, and the value is true if the unit can attack that tile, false otherwise.
	 */
    @Override
    public boolean[][] canAttack() {
        throw new UnsupportedOperationException("Unimplemented method 'canAttack'");
    }
}
