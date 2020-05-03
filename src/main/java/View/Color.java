package View;

public enum Color {

    ANSI_BRIGHT_YELLOW("\u001b[93m"),
    ANSI_WHITE("\u001b[37m"),
    ANSI_BLUE("\u001b[44m");


    static final String CLEAR_CONSOLE = "\033[H\033[2J";
    static final String RESET = "\u001B[0m";
    static final String CLEAR = "\33[1A\33[2K";

    private String escape;

    Color(String escape) { this.escape = escape; }

    @Override
    public String toString() { return escape; }


}
