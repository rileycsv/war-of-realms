package entities;

import utils.DatabaseIO;

public class Archer extends Unit {

    public Archer(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "archer.png";
        this.maxMovement = DatabaseIO.getValue("Archers", "Movement_Speed");
        this.health = DatabaseIO.getValue("Archers", "Unit_Health");
        this.attackDamage = 2;
        this.attackRange = DatabaseIO.getValue("Archers", "Attack_Range");
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
