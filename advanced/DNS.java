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
        this.domain = scanner.nextLine();
        this.setType(scanner.nextLine());
    }

    private static Record[] getRecords(String domain, int recordType) throws TextParseException {
        return new Lookup(domain, recordType).run();
    }


    public void setType(String inputType) {

        int currentType = getRecordType(inputType);

        String type = "Invalid type";
        switch (currentType) {
            case Type.A -> type = "A";
            case Type.MX -> type = "MX";
            case Type.NS -> type = "NS";
            case Type.TXT -> type = "TXT";
            case Type.SOA -> type = "SOA";
            case Type.CNAME -> type = "CNAME";
            default -> {
                System.out.println(type + "Please enter a valid type");
                type = scanner.nextLine();
            }
        }
    }

    private int getRecordType(String type) {
        return switch (type) {
            case "A" ->Type.A;
            case "MX" ->Type.MX;
            case "NS" ->Type.NS;
            case "TXT" ->Type.TXT;
            case "SOA" -> Type.SOA;
            case "CNAME" -> Type.CNAME;
            default -> throw new IllegalStateException("Unexpected value");
        };
    }
}
