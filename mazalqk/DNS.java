import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DNS {
    private final int[] typesOfDNS = {Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME};
    private JTextPane outputArea;

    public DNS(String domain, String type, JTextPane outputArea) throws TextParseException {
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
        Color recordColor = Color.GREEN;
        Color notFoundColor = Color.RED;

        if (records.isEmpty()) {
            appendToTextPane(dnsRecord + " not found for domain: " + domain + "\n", notFoundColor);
        } else {
            appendToTextPane(dnsRecord + " records for domain: " + domain + "\n", recordColor);
            System.out.println();
            for (Record record : records) {
                appendToTextPane(record.rdataToString() + "\n", Color.WHITE);
            }
        }
        appendToTextPane("===================================================================================================\n", Color.WHITE);
    }
    private void appendToTextPane(String text, Color color) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument doc = outputArea.getStyledDocument();
                Style style = outputArea.addStyle("ColoredStyle", null);
                StyleConstants.setForeground(style, color);
                doc.insertString(doc.getLength(), text, style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
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
