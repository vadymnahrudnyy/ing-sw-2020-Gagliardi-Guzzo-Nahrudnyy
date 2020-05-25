package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * Enumeration used in Game and Power classes.
 * SETUP is used in Game only at the beginning of the game (Turn 0)
 *
 * ANYTIME and WINCONDITION will never be used in Game class cause they don't indicate
 * a real phase of the turn but are useful to indicate the moment a power can be used.
 */

public enum TurnPhase implements Serializable {
    SETUP,
    START,
    MOVE,
    BUILD,
    END,
    ANYTIME,
    WINCONDITION
}
