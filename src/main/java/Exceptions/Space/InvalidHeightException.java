package Exceptions.Space;

public class InvalidHeightException extends RuntimeException{
    public InvalidHeightException() {
        super ("Invalid height for a Space");
    }
}
