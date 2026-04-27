package entities;

import utils.DatabaseIO;

public class Infantry extends Unit {
    public Infantry(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "infantry.png";
        this.maxMovement = DatabaseIO.getValue("Infantry", "Movement_Speed");
        this.health = DatabaseIO.getValue("Infantry", "UnitHealth");
        this.attackDamage = 9;
        this.attackRange = DatabaseIO.getValue("Infantry", "Attack_Range");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
