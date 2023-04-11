package formelap.entity;

import formelap.world.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class Plant extends Organism{
    private final float spreadProbability;

    Plant(float spreadProbability, Color color, Position position) {
        super(0, 0, color, position, Type.PLANT);
        this.spreadProbability = spreadProbability;
    }

    @Override
    public void action() {
        this.increaseAge();
        float probabilityFactor = new Random().nextFloat();

        if(this.getSpreadProbability() >= probabilityFactor) {
            Position seedlingPosition = this.getPosition();
            List<String> directions = new ArrayList<>();
            directions.add("N");
            directions.add("E");
            directions.add("S");
            directions.add("W");

            if(seedlingPosition.getRow() == 0)
                directions.remove("N");
            if(seedlingPosition.getRow() == (world.getSize() - 1))
                directions.remove("S");
            if(seedlingPosition.getColumn() == 0)
                directions.remove("W");
            if(seedlingPosition.getColumn() == (world.getSize() - 1))
                directions.remove("E");

            int index = new Random().nextInt(directions.size());
            String direction = directions.get(index);

            switch (direction) {
                case "N" -> seedlingPosition.setRow(seedlingPosition.getRow() - 1);
                case "S" -> seedlingPosition.setRow(seedlingPosition.getRow() + 1);
                case "W" -> seedlingPosition.setColumn(seedlingPosition.getColumn() - 1);
                case "E" -> seedlingPosition.setColumn(seedlingPosition.getColumn() + 1);
                default -> {
                }
            }

            Organism field = world.getOrganismFromGrid(seedlingPosition);

            if(field == null)
                createNewOrganism(seedlingPosition);
        }
    }

    float getSpreadProbability() {
        return spreadProbability;
    }
}
