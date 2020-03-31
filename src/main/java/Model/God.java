package Model;

/**
 * @author Alessia Guzzo
 */

public final class God {
    /**
     * This class creates all the God types with their respective attributes
     */

    private final int godID;
    private final String name;
    private final int playersAllowed;
    private final String description;
    private final Power[] powers;
    private final int numPowers;

    /**
     * @param godID is the univocal ID for the GodCard
     * @param name is the God's name
     * @param numPowers states how many power can be used by the God considered, some have only one power but others have two powers
     * @param playersAllowed states the number of players can be used by the God
     * @param description string for describe God's features
     * @param powers array that contains God's power, max two
     */
    public God(int godID, String name, int numPowers, int playersAllowed, String description, Power[] powers) {
        this.godID = godID;
        this.name = name;
        this.numPowers= numPowers;
        this.playersAllowed = playersAllowed;
        this.description = description;
        this.powers = powers;
    }


    public int getGodID() {
        return godID;
    }

    public String getName() {
        return name;
    } //non nullo

    public int getPlayersAllowed() {
        return playersAllowed;
    }

    public String getDescription() {
        return description;
    }

    public Power[] getPowers() {
        return powers;
    }

    public Power getSinglePower(int n) { return powers[n]; }

}
