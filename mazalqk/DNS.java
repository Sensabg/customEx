import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.util.*;

public class DNS {
    private final int[] typesOfDNS = new int[]{Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME};
    private final Scanner scanner = new Scanner(System.in);
    private static String color;
    private static String reset;
    List<Record> records;
    String text;
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
        if (itContains(records, dnsRecord, domain, text)) {
            setColor(domain, dnsRecord, Colors.ANSI_GREEN, text);
            records.forEach(record -> System.out.println(record.rdataToString()));
        }
    }

    private static boolean itContains(List<Record> records, String recordTypeName, String domain, String text) {
        text = String.format(System.lineSeparator() + recordTypeName + " not found for domain: " + domain);
        if (records.isEmpty()) {
            setColor(domain, recordTypeName, Colors.ANSI_RED, text);
            return false;
        }
        return true;
    }

    private static void setColor(String domain, String dnsRecord, String color, String text) {
        text = String.format("\n" + dnsRecord + " records for domain: " + domain);
        System.out.print(color);
        System.out.println(text);
        System.out.print(Colors.ANSI_RESET);
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
