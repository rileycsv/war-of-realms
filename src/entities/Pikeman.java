package entities;

import utils.DatabaseIO;

public class Pikeman extends Unit {

    public Pikeman(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "Pikemen";
        this.maxMovement = DatabaseIO.getValue(super.UNIT_TYPE, "Movement_Speed");
        this.health = DatabaseIO.getValue(super.UNIT_TYPE, "UnitHealth");
        this.attackDamage = 5;
        this.attackRange = DatabaseIO.getValue(super.UNIT_TYPE, "Attack_Range");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
