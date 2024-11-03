import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.util.Scanner;

public class DNS {
    private final Scanner scanner = new Scanner(System.in);
    String domain;
    String type;
    Record[] records;

    public DNS() {
        System.out.print("Enter domain name: ");
        this.domain = scanner.nextLine();
        System.out.print("Enter DNS record type (A, MX, NS, TXT, SOA, CNAME): ");
        String inputType = scanner.nextLine();
        this.setType(inputType);
    }

    private static Record[] getRecords(String domain, int recordType) throws TextParseException {
        return new Lookup(domain, recordType).run();
    }

    public void setType(String inputType) {
        int recordType; 
        switch (inputType.toUpperCase()) {
            case "A":
                recordType = Type.A;
                break;
            case "MX":
                recordType = Type.MX;
                break;
            case "NS":
                recordType = Type.NS;
                break;
            case "TXT":
                recordType = Type.TXT;
                break;
            case "SOA":
                recordType = Type.SOA;
                break;
            case "CNAME":
                recordType = Type.CNAME;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + inputType.toUpperCase());
        }
        this.type = inputType;

        try {
            records = getRecords(domain, recordType);
            printRecords();
        } catch (TextParseException e) {
            System.err.println("Error fetching records: " + e.getMessage());
        }
    }

    private void printRecords() {
        if (records != null && records.length > 0) {
            System.out.println("Records for domain '" + domain + "' of type '" + type + "':");
            for (Record record : records) {
                System.out.println(record);
            }
        } else {
            System.out.println("No records found for domain: " + domain + " with type: " + type);
        }
    }

    public static void main(String[] args) {
        new DNS();
    }
}
