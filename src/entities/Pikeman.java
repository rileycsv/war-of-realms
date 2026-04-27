package entities;

import utils.DatabaseIO;

public class Pikeman extends Unit {

    public Pikeman(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "pikeman.png";
        this.maxMovement = DatabaseIO.getValue("Pikemen", "Movement_Speed");
        this.health = DatabaseIO.getValue("Pikemen", "UnitHealth");
        this.attackDamage = 5;
        this.attackRange = DatabaseIO.getValue("Pikemen", "Attack_Range");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
