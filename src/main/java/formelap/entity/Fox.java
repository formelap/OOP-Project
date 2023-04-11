package formelap.entity;

import formelap.world.Position;

import java.awt.*;

public class Fox extends Animal {
    public Fox(Position position) {
        super(AnimalSpecies.FOX, 3, 7, Color.RED, position);
        System.out.println("Powstanie Lisa na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }

    @Override
    void createNewOrganism(Position p) {
        new Fox(p);
    }

    @Override
    void collision(Organism defender) {
        if(this.getStrength() < defender.getStrength()){
            return;
        }
        else if(defender.canDefend(this)){
            return;
        }
        else if(this.getStrength() > defender.getStrength()) {
            world.killOrganism(defender);
            world.moveOrganism(this, defender.getPosition());
        }
        else if(this.getAge() > defender.getAge()) {
            world.killOrganism(defender);
            world.moveOrganism(this, defender.getPosition());
        }
        else {
            world.killOrganism(this);
        }
    }
}
