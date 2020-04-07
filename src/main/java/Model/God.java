package Model;

/**
 * @author Alessia Guzzo
 */

public final class God {
    /**
     * God class creates all the God types with their respective attributes
     */
    private final String name;
    private final int playersAllowed;
    private final String description;
    private final int[] powers;
    private final int numPowers;

    /**
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


    public String getName() {
        return name;
    }

    public int getPlayersAllowed() {
        return playersAllowed;
    }

    public String getDescription() {
        return description;
    }

    public int[] getPowers() {
        return powers;
    }

    public int getSinglePower(int n) { return powers[n]; }

}