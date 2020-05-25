package it.polimi.ingsw.PSP30.Exception.Space;

public class DomeAlreadyBuiltException extends RuntimeException{
    public DomeAlreadyBuiltException(){
        super("An error occurred, a dome has been already built in this Space");
    }
}