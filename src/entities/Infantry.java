package entities;

public class Infantry extends Unit {
    public Infantry(int PID, String kingdom, int x, int y) {
        super(PID, kingdom, x, y);
        this.UNIT_TYPE = "infantry.png";
        this.maxMovement = 3;
        this.health = 10;
        this.attackDamage = 3;
        this.attackRange = 1;
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }

    @Override
    public boolean[][] canMoveTo(int row, int col) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canMoveTo'");
    }

    @Override
    public boolean[][] canAttack(int row, int col) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canAttack'");
    }
}
