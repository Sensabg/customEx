import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.util.*;

public class DNS {
    private final int[] typesOfDNS = {Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME};
    Input input;

    public DNS(Input input) throws TextParseException {
        this.setInput(input);
    }

    public void setInput(Input input) throws TextParseException {
        String domain = input.getDomain();
        String type = input.getType();
        int entry = getType(type);
        String dnsRecord = getEntry(entry);

        if (entry == 0) {
            for (int dns : typesOfDNS) {
                List<Record> records = getRecords(domain, dns);
                dnsRecord = getEntry(dns);
                printRecords(domain, dnsRecord, records);
            }
        } else {
            List<Record> records = getRecords(domain, entry);
            printRecords(domain, dnsRecord, records);
        }
        this.input = input;
    }

    private void printRecords(String domain, String dnsRecord, List<Record> records) {
        if (records.isEmpty()) {
            Colors.setColor(Colors.ANSI_RED,  dnsRecord + " not found for domain: " + domain + " ");
            System.out.print(Colors.ANSI_YELLOW);
            System.out.println("\n===================================================================================================");
            System.out.print(Colors.ANSI_RESET);
        } else {
            System.out.print("".trim());
            Colors.setColor(Colors.ANSI_GREEN,   dnsRecord + " records for domain: " + domain + " ");
            System.out.println("".trim());
            records.forEach(record -> System.out.println(record.rdataToString()));
        }
    }

    private static String getEntry(int entry) {
        return switch (entry) {
            case Type.A -> "A";
            case Type.MX -> "MX";
            case Type.NS -> "NS";
            case Type.TXT -> "TXT";
            case Type.SOA -> "SOA";
            case Type.CNAME -> "CNAME";
            default -> "INVALID";
        };
    }

    private static int getType(String type) {
        return switch (type) {
            case "A" -> Type.A;
            case "MX" -> Type.MX;
            case "NS" -> Type.NS;
            case "TXT" -> Type.TXT;
            case "SOA" -> Type.SOA;
            case "CNAME" -> Type.CNAME;
            case "ALL" -> 0;
            default -> -1;
        };
    }

    private static List<Record> getRecords(String domain, int recordType) throws TextParseException {
        Record[] records = new Lookup(domain, recordType).run();
        return records != null ? Arrays.asList(records) : Collections.emptyList();
    }
}