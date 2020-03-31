package Exceptions.Space;

public class DomeAlreadyMissingException extends RuntimeException{
    public DomeAlreadyMissingException() {super("An error occurred, trying to remove a missing dome");}
}
