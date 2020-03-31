package Exceptions.Space;

public class TowerCompleteException extends RuntimeException{
    public TowerCompleteException(){
        super ("An error occurred, the tower is already complete");
    }
}