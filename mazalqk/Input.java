import java.util.Scanner;

public class Input {
    private final Scanner scanner = new Scanner(System.in);
    private String domain;
    private String type;

    public Input() {
        this.setDomain();
        this.setType();
    }

    public void setDomain() {
        final String HTTP = "http//";
        final String HTTPS = "https//";

        Colors.setColor(Colors.ANSI_GREEN, "Please enter a domain name: ");
        String inputDomain = scanner.nextLine();

        if (inputDomain.contains(HTTPS)) {
            inputDomain = inputDomain.replaceAll(HTTPS, "");
        } else if (inputDomain.contains(HTTP)) {
            inputDomain = inputDomain.replaceAll(HTTP, "");
        }
        this.domain = inputDomain.trim().toLowerCase();
    }

    public void setType() {
        Colors.setColor(Colors.ANSI_GREEN, "A, MX, NS, TXT, SOA, CNAME:\nPlease enter a type: ");
        String inputType = scanner.nextLine().toUpperCase();
        RecordType record = RecordType.fromString(inputType);

        while (record == RecordType.INVALID) {
            Colors.setColor(Colors.ANSI_RED, "Please enter a valid type: ");
            inputType = scanner.nextLine().toUpperCase();
            record = RecordType.fromString(inputType);
        }
        this.type = record.toString().toUpperCase();
    }

    public String getDomain() {
        return domain.toLowerCase();
    }

    public String getType() {
        return type.toUpperCase();
    }
}