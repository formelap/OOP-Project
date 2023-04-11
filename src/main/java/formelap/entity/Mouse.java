package formelap.entity;

import formelap.world.Position;

import java.awt.*;

public class Mouse extends Animal {
    public Mouse(Position position) {
        super(AnimalSpecies.MOUSE, 1, 6, new Color(0.70f, 0.60f, 0.28f, 1.00f), position);
        System.out.println("Powstanie Myszy na polu (" + position.getColumn() + ", " + position.getRow() + ")");
    }

    @Override
    void createNewOrganism(Position p) {
        new Mouse(p);
    }

    @Override
    boolean canDefend(Organism attacker) {
        Position tempDefensePosition = this.getPosition();
        Position tempAttackPosition = this.getPosition();
        tempDefensePosition = chooseDirection(tempDefensePosition);
        Organism field = world.getOrganismFromGrid(tempDefensePosition);

        if(field == null) {
            world.moveOrganism(this, tempDefensePosition);
            world.moveOrganism(attacker, tempAttackPosition);

            return true;
        }

        return false;
    }
}
