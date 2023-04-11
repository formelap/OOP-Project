package formelap.model;

import formelap.world.Position;
import formelap.world.World;

import java.awt.*;
import java.io.Serializable;

public abstract class Organism implements Serializable {
    private static final long serialVersionUID = 1L;

    static World world = World.getInstance();
    private final int strength;
    private final int initiative;
    private final Color color;
    private Position position;
    private final Type type;
    private int age;
    private boolean isAlive;
    private boolean hasXMove;

    Organism(int strength, int initiative, Color color, Position position, Type type) {
        this.strength = strength;
        this.initiative = initiative;
        this.color = color;
        this.type = type;
        this.position = position;
        this.age = 0;
        this.isAlive = true;
        this.hasXMove = false;

        world.addNewOrganism(this);
    }


    abstract public void action();
    abstract void createNewOrganism(Position p);

    public int getInitiative() {
        return initiative;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return new Position(position);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public void die() {
        isAlive = false;
    }

    int getStrength() {
        return strength;
    }

    Type getType() {
        return type;
    }

    void increaseAge() {
        age += 1;
    }

    boolean isAlive() {
        return isAlive;
    }

    void addXMove() {
        hasXMove = true;
    }

    boolean canDefend(Organism attacker) {
        return false;
    }

    boolean hasXMove() {
        return hasXMove;
    }

    void useXMove() {
        hasXMove = false;
    }

}
