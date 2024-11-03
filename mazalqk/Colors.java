public class Colors {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    private Colors() {}

    static void setColor(String domain, String dnsRecord, String color, String text) {
        System.out.print(color);
        System.out.println(text);
        System.out.print(Colors.ANSI_RESET);
    }
}
