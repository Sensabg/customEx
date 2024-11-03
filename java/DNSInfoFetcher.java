import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class DNSInfoFetcher {

    // Function to fetch and display different DNS record types
    public static void fetchDNSRecords(String domain) {
        try {
            // Fetch and print A records
            printRecords(domain, Type.A, "A Record");

            // Fetch and print MX records
            printRecords(domain, Type.MX, "MX Record");

            // Fetch and print NS records
            printRecords(domain, Type.NS, "NS Record");

            // Fetch and print TXT records
            printRecords(domain, Type.TXT, "TXT Record");

            // Fetch and print SOA record
            printRecords(domain, Type.SOA, "SOA Record");

            // Fetch and print CNAME record
            printRecords(domain, Type.CNAME, "CNAME Record");

        } catch (IOException e) {
            System.err.println("Error fetching DNS records: " + e.getMessage());
        }
    }

    // Helper function to print DNS records
    private static void printRecords(String domain, int recordType, String recordTypeName) throws IOException {
        Record[] records = new Lookup(domain, recordType).run();
        if (records == null) {
            System.out.println(recordTypeName + " not found for domain: " + domain);
            return;
        }
        System.out.println("\n" + recordTypeName + " for domain: " + domain);
        Arrays.stream(records).forEach(record -> System.out.println(record.rdataToString()));
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String domain = scanner.nextLine();


        fetchDNSRecords(domain);
    }
}
