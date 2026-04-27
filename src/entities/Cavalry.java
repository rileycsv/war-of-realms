package entities;

import utils.DatabaseIO;

public class Cavalry extends Unit {

    public Cavalry(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "cavalry.png";
        this.maxMovement = DatabaseIO.getValue("Cavalry", "Movement_Speed");
        this.health = DatabaseIO.getValue("Cavalry", "Unit_Health");
        this.attackDamage = 7;
        this.attackRange = DatabaseIO.getValue("Cavalry", "Attack_Range");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
