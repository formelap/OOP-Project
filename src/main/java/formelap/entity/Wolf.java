package formelap.entity;

import formelap.world.Position;

import java.awt.*;

public class Wolf extends Animal{
    public Wolf(Position position) {
        super(AnimalSpecies.WOLF, 9, 5, Color.BLACK, position);
        System.out.println("Powstanie Wilka na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }


    @Override
    void createNewOrganism(Position p) {
        new Wolf(p);
    }
}
