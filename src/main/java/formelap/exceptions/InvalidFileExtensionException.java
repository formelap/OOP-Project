package formelap.exceptions;

public class InvalidFileExtensionException extends Exception {

    public InvalidFileExtensionException(String extension) {
        super("Plik musi mieć rozszerzenie " + extension);
    }
}
