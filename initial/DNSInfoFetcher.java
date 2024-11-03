import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class DNSInfoFetcher {

    public static void fetchDNSRecords(String domain) {
        try {
            printRecords(domain, Type.A, "A Record");
            printRecords(domain, Type.MX, "MX Record");
            printRecords(domain, Type.NS, "NS Record");
            printRecords(domain, Type.TXT, "TXT Record");
            printRecords(domain, Type.SOA, "SOA Record");
            printRecords(domain, Type.CNAME, "CNAME Record");
        } catch (IOException e) {
            System.err.println("Error fetching DNS records: " + e.getMessage());
        }
    }

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
