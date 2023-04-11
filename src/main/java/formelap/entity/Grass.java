package formelap.entity;

import formelap.world.Position;

import java.awt.*;

public class Grass extends Plant {
    public Grass(Position position) {
        super(0.20f, Color.GREEN, position);
        System.out.println("Powstanie Trawy na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }

    @Override
    void createNewOrganism(Position p) {
        new Grass(p);
    }
}
