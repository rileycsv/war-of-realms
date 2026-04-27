package entities;

import utils.DatabaseIO;

public class Archer extends Unit {

    public Archer(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "Archers";
        this.maxMovement = DatabaseIO.getValue(super.UNIT_TYPE, "Movement_Speed");
        this.health = DatabaseIO.getValue(super.UNIT_TYPE, "UnitHealth");
        this.attackDamage = 2;
        this.attackRange = DatabaseIO.getValue(super.UNIT_TYPE, "Attack_Range");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
