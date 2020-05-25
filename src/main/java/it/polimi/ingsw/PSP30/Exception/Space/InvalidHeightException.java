package it.polimi.ingsw.PSP30.Exception.Space;

public class InvalidHeightException extends RuntimeException{
    public InvalidHeightException() {
        super ("Invalid height for a Space");
    }
}
