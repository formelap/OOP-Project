package formelap.entity;

import formelap.world.Position;

import java.awt.*;
import java.util.*;
import java.util.List;

abstract class Animal extends Organism {
    private final AnimalSpecies species;

    Animal(AnimalSpecies species, int strength, int initiative, Color color, Position position) {
        super(strength, initiative, color, position, Type.ANIMAL);
        this.species = species;
    }

    @Override
    public void action() {
        increaseAge();

        if (hasXMove() && isAlive()) {
            useXMove();
            basicMove();
        }

        if (isAlive()) {
            basicMove();
        }
    }

    AnimalSpecies getSpecies() {
        return species;
    }

    void basicMove() {
        Position tempPosition = getPosition();
        tempPosition = chooseDirection(tempPosition);
        Organism field = world.getOrganismFromGrid(tempPosition);

        if (field == null) {
            world.moveOrganism(this, tempPosition);
        } else if (field.getType() == Type.ANIMAL) {
            Animal animal = (Animal) field;

            if (getSpecies() == animal.getSpecies()) {
                if ((getAge() > 5) && (animal.getAge() > 5)) {
                    reproduce();
                }
            } else {
                collision(animal);
            }
        } else {
            collision(field);
        }
    }

    void collision(Organism defender) {
        if (defender.canDefend(this)) {
            return;
        } else if (this.getStrength() > defender.getStrength()) {
            world.killOrganism(defender);
            world.moveOrganism(this, defender.getPosition());
        } else if (this.getStrength() < defender.getStrength()) {
            world.killOrganism(this);
        } else if (this.getAge() > defender.getAge()) {
            world.killOrganism(defender);
            world.moveOrganism(this, defender.getPosition());
        } else {
            world.killOrganism(this);
        }
    }

    Position chooseDirection(Position p) {

        List<String> directions = new ArrayList<>();
        directions.add("N");
        directions.add("E");
        directions.add("S");
        directions.add("W");

        if (p.getRow() == 0)
            directions.remove("N");
        if (p.getRow() == (world.getSize() - 1))
            directions.remove("S");
        if (p.getColumn() == 0)
            directions.remove("W");
        if (p.getColumn() == (world.getSize() - 1))
            directions.remove("E");

        int index = new Random().nextInt(directions.size());
        String direction = directions.get(index);

        switch (direction) {
            case "N" -> p.setRow(p.getRow() - 1);
            case "S" -> p.setRow(p.getRow() + 1);
            case "W" -> p.setColumn(p.getColumn() - 1);
            case "E" -> p.setColumn(p.getColumn() + 1);
            default -> {
            }
        }

        return p;
    }

    void reproduce() {
        Position babyPosition = getPosition();
        babyPosition = chooseDirection(babyPosition);
        Organism field = world.getOrganismFromGrid(babyPosition);

        if (field == null)
            createNewOrganism(babyPosition);
    }

}
