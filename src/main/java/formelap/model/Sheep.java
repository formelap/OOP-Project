package formelap.model;

import formelap.world.Position;

import java.awt.*;

public class Sheep extends Animal{
    public Sheep(Position position) {
        super(AnimalSpecies.SHEEP, 4, 4, Color.LIGHT_GRAY, position);
        System.out.println("Powstanie Owcy na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }


    @Override
    void createNewOrganism(Position p) {
        new Sheep(p);
    }
}
