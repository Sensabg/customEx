import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

public class DNS {
    private final int[] typesOfDNS = {Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME};
    private final JTextPane outputArea;
    Color recordColor = Color.GREEN;
    Color notFoundColor = Color.RED;

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
        Map<String, List<String>> recordMap = new HashMap<>();

        if (records.isEmpty()) {
            appendToTextPane(dnsRecord + " not found for domain: " + domain + "\n", notFoundColor);
            recordMap.put(dnsRecord, new ArrayList<>());
        } else {
            appendToTextPane(dnsRecord + " records for domain: " + domain + "\n", recordColor);
            System.out.println();
            for (Record record : records) {
                String recordData = record.rdataToString();
                appendToTextPane(recordData + "\n", Color.WHITE);
                String recordType = getEntry(record.getType());
                recordMap.computeIfAbsent(recordType, k -> new ArrayList<>()).add(recordData);
            }
        }
        forwardLookupResult(domain, recordMap);
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
                throw new IllegalArgumentException("Invalid text");
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

    private void forwardLookupResult(String domain, Map<String, List<String>> records) {
        try {
            URI uri = new URI("https://sub.freak-bg.com/dnsReceiver.php");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            StringBuilder jsonInputString = new StringBuilder();
            jsonInputString.append("{\"domain\":\"").append(domain).append("\",\"records\":{");

            boolean firstRecordType = true;

            for (Map.Entry<String, List<String>> entry : records.entrySet()) {
                if (!firstRecordType) jsonInputString.append(",");
                jsonInputString.append("\"").append(entry.getKey()).append("\":[");
                List<String> values = entry.getValue();

                boolean firstValue = true;

                for (String value : values) {
                    if (!firstValue) jsonInputString.append(",");
                    jsonInputString.append("\"").append(value.replace("\"", "\\\"")).append("\"");
                    firstValue = false;
                }
                jsonInputString.append("]");
                firstRecordType = false;
            }
            jsonInputString.append("}}");
//            System.out.println("Sending JSON: " + jsonInputString);
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(jsonInputString.toString());
                writer.flush();
            }
            System.out.println();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                appendToTextPane("Forwarded successfully for " + domain + "\n", recordColor);
            } else {
                System.out.println("Failed to forward lookup for " + domain + ": " + responseCode + notFoundColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Record> getRecords(String domain, int recordType) throws TextParseException {
        Record[] records = new Lookup(domain, recordType).run();
        return records != null ? Arrays.asList(records) : Collections.emptyList();
    }
}
