package formelap.exceptions;

public class OrganismOutOfBordersException extends Exception {

    public OrganismOutOfBordersException(int numberOfOrganisms) {
        super("Ilość organismów która nie zmieściła się na planszy: " + numberOfOrganisms);
    }
}
