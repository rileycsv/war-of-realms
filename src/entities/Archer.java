package entities;

import utils.DatabaseIO;

public class Archer extends Unit {

    public Archer(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "archer.png";
        this.maxMovement = DatabaseIO.getValue(super.UNIT_TYPE, "movement");
        this.health = DatabaseIO.getValue(super.UNIT_TYPE, "health");
        this.attackDamage = DatabaseIO.getValue(super.UNIT_TYPE, "attackDamage");
        this.attackRange = DatabaseIO.getValue(super.UNIT_TYPE, "attackRange");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
