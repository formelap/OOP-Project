package formelap.model;

import formelap.world.Position;

import java.awt.*;

public class DeadlyNightshade extends Plant {
    public DeadlyNightshade(Position position) {
        super(0.10f, new Color(0.55f, 0.40f, 0.70f, 1.00f), position);
        System.out.println("Powstanie Wilczej Jagody na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }

    @Override
    void createNewOrganism(Position p) {
        new DeadlyNightshade(p);
    }

    @Override
    boolean canDefend(Organism attacker) {

        world.killOrganism(this);
        world.killOrganism(attacker);

        return true;
    }
}
