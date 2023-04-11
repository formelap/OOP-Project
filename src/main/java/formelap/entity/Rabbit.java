package formelap.entity;

import formelap.world.Position;

import java.awt.*;

public class Rabbit extends Animal{
    public Rabbit(Position position) {
        super(AnimalSpecies.RABBIT, 2, 8, new Color(0.40f, 0.10f, 0.00f, 0.95f), position);
        System.out.println("Powstanie Królika na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }

    @Override
    void createNewOrganism(Position p) {
        new Rabbit(p);
    }

    @Override
    public void action() {
        super.action();

        if(this.isAlive()) {
            basicMove();
        }
    }
}
