import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import javax.swing.*;
import java.util.*;

public class DNS {
    private final int[] typesOfDNS = {Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME};
    private JTextArea outputArea;

    public DNS(String domain, String type, JTextArea outputArea) throws TextParseException {
        this.outputArea = outputArea;
        performLookup(domain, type);
    }

    private void performLookup(String domain, String type) throws TextParseException {
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
    }

    private void printRecords(String domain, String dnsRecord, List<Record> records) {
        if (records.isEmpty()) {
            outputArea.append(dnsRecord + " not found for domain: " + domain + "\n");
        } else {
            outputArea.append(dnsRecord + " records for domain: " + domain + "\n");
            System.out.println();
            records.forEach(record -> outputArea.append(record.rdataToString() + "\n"));
            Text.setText("");
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
