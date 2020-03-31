package Exceptions.Space;

public class DomeAlreadyBuiltException extends RuntimeException{
    public DomeAlreadyBuiltException(){
        super("An error occurred, a dome has been already built in this Space");
    }
}