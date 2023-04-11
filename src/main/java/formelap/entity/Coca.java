package formelap.entity;

import formelap.world.Position;

import java.awt.*;

public class Coca extends Plant {
    public Coca(Position position) {
        super(0.15f, new Color(0.30f, 0.80f, 0.90f, 1.00f), position);
        System.out.println("Powstanie Koki na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }

    @Override
    void createNewOrganism(Position p) {
        new Coca(p);
    }

    @Override
    boolean canDefend(Organism attacker) {

        attacker.addXMove();

        world.killOrganism(this);
        world.moveOrganism(attacker, this.getPosition());

        return true;
    }
}
