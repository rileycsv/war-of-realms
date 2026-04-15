package entities;

public class Archer extends Unit {

    public Archer(int PID, String kingdom, int x, int y) {
        super(PID, kingdom, x, y);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean[][] canMoveTo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canMoveTo'");
    }

    @Override
    public boolean[][] canAttack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canAttack'");
    }
}
