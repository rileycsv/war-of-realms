package entities;

import java.util.LinkedList;
import java.util.Queue;

import environment.Board;

public class Infantry extends Unit {
    public Infantry(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "infantry.png";
        this.maxMovement = 3;
        this.health = 10;
        this.attackDamage = 3;
        this.attackRange = 1;
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
