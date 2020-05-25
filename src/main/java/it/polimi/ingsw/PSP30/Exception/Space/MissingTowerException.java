package it.polimi.ingsw.PSP30.Exception.Space;

public class MissingTowerException extends RuntimeException{
    public MissingTowerException(){
        super ("An error occurred, the space doesn't have a tower");
    }
}
