import java.util.Scanner;

public class Input {
    private final Scanner scanner = new Scanner(System.in);
    String domain;
    String type;
    String text;

    public Input() {
        this.setDomain(domain);
        this.setType(type);
    }

    public void setDomain(String domain) {
        final String HTTP = "http//";
        final String HTTPS = "https//";
        text = "Please enter a domain name: ";


        Colors.setColor(domain, type, Colors.ANSI_GREEN, text);
        domain = scanner.nextLine();
        String formatedDomain = domain;

        if (domain.contains(HTTPS)) {
            formatedDomain = domain.replaceAll(HTTPS, "");
        } else if (domain.contains(HTTP)) {
            formatedDomain = domain.replaceAll(HTTP, "");
        }

        this.domain = formatedDomain.trim().toLowerCase();
    }

    public void setType(String type) {
        text = "A, MX, NS, TXT, SOA, CNAME:\nPlease enter a type: ";
        Colors.setColor(domain, type, Colors.ANSI_GREEN, text);
        type = scanner.nextLine().toUpperCase();
        RecordType record = RecordType.fromString(type);

        while (record == RecordType.INVALID) {
            text = "Please enter a valid type: ";
            type = scanner.nextLine().toUpperCase();
            Colors.setColor(domain, type, Colors.ANSI_RED, text);
            record = RecordType.fromString(type);
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