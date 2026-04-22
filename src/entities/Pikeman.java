package entities;

public class Pikeman extends Unit {

    public Pikeman(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "pikeman.png";
        this.maxMovement = 3;
        this.health = 10;
        this.attackDamage = 3;
        this.attackRange = 1;
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
