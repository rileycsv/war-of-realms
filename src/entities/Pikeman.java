package entities;

public class Pikeman extends Unit {

    public Pikeman(int PID, String kingdom, int x, int y) {
        super(PID, kingdom, "pikeman.png", x, y);
        this.maxMovement = 3;
        this.health = 10;
        this.attackDamage = 3;
        this.attackRange = 1;
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }

    @Override
    public boolean[][] canMoveTo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canMoveTo'");
    }

    @Override
    public boolean[][] canAttack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canAttack'");
    }
}
