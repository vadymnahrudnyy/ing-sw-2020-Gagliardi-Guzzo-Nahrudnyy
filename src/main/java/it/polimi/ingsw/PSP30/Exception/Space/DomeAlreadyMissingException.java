package it.polimi.ingsw.PSP30.Exception.Space;

public class DomeAlreadyMissingException extends RuntimeException{
    public DomeAlreadyMissingException() {super("An error occurred, trying to remove a missing dome");}
}
