package View;

public enum Color {

    ANSI_BRIGHT_WHITE("\u001b[97m"),
    ANSI_BRIGHT_BLACK("\u001b[90m"),
    ANSI_BRIGHT_CYAN("\u001b[96m"),
    BACKGROUND_BRIGHT_WHITE("\u001b[107m"),
    BACKGROUND_BRIGHT_BLACK("\u001b[100m"),
    BACKGROUND_BRIGHT_CYAN("\u001b[106m");


    static final String CLEAR_CONSOLE = "\033[H\033[2J";
    static final String RESET = "\u001B[0m";
    static final String CLEAR = "\33[1A\33[2K";

    private String escape;

    Color(String escape) { this.escape = escape; }

    public String getEscape() { return escape; }

    @Override
    public String toString() { return escape; }


}
