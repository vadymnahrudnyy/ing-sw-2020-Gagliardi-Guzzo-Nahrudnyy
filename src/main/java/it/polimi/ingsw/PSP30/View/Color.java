package it.polimi.ingsw.PSP30.View;

/**
 * Defines the color of the workers in CLI interface.
 */
public enum Color {

    ANSI_RED("\u001b[31m"),
    ANSI_BLACK("\u001b[30m"),
    ANSI_BLUE("\u001b[34m");


    static final String CLEAR_CONSOLE = "\033[H\033[2J";
    static final String RESET = "\u001B[0m";
    static final String CLEAR = "\33[1A\33[2K";

    private String escape;

    Color(String escape) { this.escape = escape; }

    @Override
    public String toString() { return escape; }


}
