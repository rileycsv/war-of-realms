package entities;

import java.util.LinkedList;
import java.util.Queue;

import environment.Board;

public class Cavalry extends Unit {

    public Cavalry(int PID, int UID, String kingdom, int x, int y) {
        super(PID, UID, kingdom, x, y);
        super.UNIT_TYPE = "cavalry.png";
        this.maxMovement = 3;
        this.health = 10;
        this.attackDamage = 3;
        this.attackRange = 1;
        this.currentHealth = health;
        this.currentMovement = maxMovement;
    }
}
