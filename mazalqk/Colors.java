public class Colors {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private Colors() {}

    static void setColor(String color, String text) {
        System.out.print(ANSI_YELLOW);
        System.out.println("===================================================================================================");
        System.out.print(ANSI_RESET);
        System.out.print(color);
        System.out.print(text);
        System.out.print(ANSI_RESET);
    }

}
