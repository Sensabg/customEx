import java.util.Scanner;

public class Input {
    private final Scanner scanner = new Scanner(System.in);
    String domain;
    String type;

    public Input() {
        this.setDomain(domain);
        this.setType(type);
    }

    public void setDomain(String domain) {
        final String HTTP = "http//";
        final String HTTPS = "https//";
        System.out.print(Colors.ANSI_GREEN);
        System.out.print("Please enter a domain name: ");
        System.out.print(Colors.ANSI_RESET);
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
        System.out.print(Colors.ANSI_GREEN);
        System.out.println("A, MX, NS, TXT, SOA, CNAME:");
        System.out.println("Please enter a type: ");
        System.out.print(Colors.ANSI_RESET);
        type = scanner.nextLine().toUpperCase();
        RecordType record = RecordType.fromString(type);

        while (record == RecordType.INVALID) {
            System.out.print(Colors.ANSI_RED);
            System.out.println("Please enter a valid type: ");
            type = scanner.nextLine().toUpperCase();
            record = RecordType.fromString(type);
        }
        System.out.print(Colors.ANSI_RESET);
        this.type = record.toString().toUpperCase();
    }

    public String getDomain() {
        return domain.toLowerCase();
    }

    public String getType() {
        return type.toUpperCase();
    }
}