public class Text {
    private Text() {}

    static void setText(String text) {
        printLines();
        System.out.print(text);
    }

    static void printLines() {
        System.out.println("===================================================================================================");
    }
}
