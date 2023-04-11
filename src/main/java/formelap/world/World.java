package formelap.world;

import formelap.entity.Organism;

import java.util.*;

public final class World {
    private static World INSTANCE;
    private final int size;
    private final Organism[][] board;
    private final SortedSet<Organism> organisms;
    private final Set<Organism> organismsToAdd;
    private final Set<Organism> organismsToAnnihilate;
    private int numberOfOrganisms;
    private int numberOfNewOrganisms;
    private int numberOfKilledOrganisms;

    private World() {
        this.size = 20;
        this.board = new Organism[size][size];
        this.organisms = new TreeSet<>((o1, o2) -> {
            if(o1.getInitiative() != o2.getInitiative())
                return (o2.getInitiative() - o1.getInitiative());
            else if(o1.getAge() != o2.getAge())
                return (o2.getAge() - o1.getAge());
            else
                return o2.hashCode() - o1.hashCode();
        });
        this.organismsToAdd = new HashSet<>();
        this.organismsToAnnihilate = new HashSet<>();
        this.numberOfOrganisms = organisms.size();
        this.numberOfNewOrganisms = organismsToAdd.size();
        this.numberOfKilledOrganisms = organismsToAnnihilate.size();
    }

    public static World getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new World();
        }

        return INSTANCE;
    }

    public int getSize() {
        return size;
    }

    public Organism[][] getBoard() {
        return board;
    }

    public Set<Organism> getOrganisms() {
        return organisms;
    }

    public Organism getOrganismFromGrid(Position p) {
        return board[p.getRow()][p.getColumn()];
    }

    public int getNumberOfOrganisms() {
        return numberOfOrganisms;
    }

    public int getNumberOfNewOrganisms() {
        return numberOfNewOrganisms;
    }

    public int getNumberOfKilledOrganisms() {
        return numberOfKilledOrganisms;
    }

    public void addNewOrganism(Organism o) {
        Position p = o.getPosition();
        board[p.getRow()][p.getColumn()] = o;
        organismsToAdd.add(o);
    }

    public void killOrganism(Organism o) {
        o.die();
        Position p = o.getPosition();
        eraseOrganismFromGrid(p);
        organismsToAnnihilate.add(o);
    }

    public void moveOrganism(Organism o, Position p) {
        Position tempPosition = o.getPosition();
        eraseOrganismFromGrid(tempPosition);
        o.setPosition(p);
        setOrganismInGrid(o);
    }

    public void update() {
        updateNewOrganisms();
        annihilateOrganisms();
        numberOfOrganisms = organisms.size();
        numberOfNewOrganisms = organismsToAdd.size();
        numberOfKilledOrganisms = organismsToAnnihilate.size();
    }

    public Position getRandomEmptyPosition() {
        Random rand = new Random();
        int row = rand.nextInt(size);
        int column = rand.nextInt(size);
        Position p = new Position(row, column);
        Organism field = getOrganismFromGrid(p);

        while(field != null) {
            row = rand.nextInt(size);
            p.setRow(row);
            column = rand.nextInt(size);
            p.setColumn(column);
            field = getOrganismFromGrid(p);
        }

        return p;
    }

    public void nextTurn() {

        for(Organism o : organisms) {

            o.action();
        }

        numberOfNewOrganisms = organismsToAdd.size();
        numberOfKilledOrganisms = organismsToAnnihilate.size();

        updateNewOrganisms();
        annihilateOrganisms();

        numberOfOrganisms = organisms.size();
    }

    public void clear() {
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                board[i][j] = null;

        organismsToAnnihilate.clear();
        organismsToAdd.clear();
        organisms.clear();
    }


    private void setOrganismInGrid(Organism o) {
        //check if position available; exception ???
        Position p = o.getPosition();
        board[p.getRow()][p.getColumn()] = o;
    }

    private void eraseOrganismFromGrid(Position p) {
        board[p.getRow()][p.getColumn()] = null;
    }

    private void updateNewOrganisms() {
        organisms.addAll(organismsToAdd);
        organismsToAdd.clear();
    }

    private void annihilateOrganisms() {
        organisms.removeAll(organismsToAnnihilate);
        organismsToAnnihilate.clear();
    }

}
