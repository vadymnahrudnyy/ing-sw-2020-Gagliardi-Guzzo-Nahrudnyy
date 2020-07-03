package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * God class creates all the God types with their respective attributes.
 * @author Alessia Guzzo
 */

public class God implements Serializable {

    private static final long serialVersionUID = 50005L;
    private final String name;
    private final int playersAllowed;
    private final String description;
    private final int[] powers;
    private final int numPowers;

    public static final int ONLY_THREE_PLAYERS_ALLOWED = 2;
    public static final int THREE_AND_FOUR_PLAYERS_ALLOWED = 3;
    
    /**
     * Builds an instance of God class.
     * @param name is the God's name
     * @param numPowers states how many powers can be used by the God considered, some have only one power but others have two powers
     * @param playersAllowed states the number of players can be used with the God
     * @param description string for describe God's features
     * @param powers array that contains God's power, max two
     */
    public God(String name, int numPowers, int playersAllowed, String description, int[] powers) {
        this.name = name;
        this.numPowers= numPowers;
        this.playersAllowed = playersAllowed;
        this.description = description;
        this.powers = powers;
    }

    /**
     * Setter of the parameter name
     * @return god's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of the parameter playersAllowed
     * @return the value playerAllowed
     */
    public int getPlayersAllowed() {
        return playersAllowed;
    }

    /**
     * Getter of the parameter description
     * @return god's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter of the parameter powers
     * @return the number of powers of the god
     */
    public int[] getPowers() {
        return powers;
    }

    /**
     * Getter of the parameter powers[]
     * @param n number of the god's power
     * @return powers[n] of the god
     */
    public int getSinglePower(int n) { return powers[n]; }

}