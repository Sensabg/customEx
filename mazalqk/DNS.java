import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;


import java.util.*;

public class DNS {
    private final Scanner scanner = new Scanner(System.in);
    Input input;
    List<Record> records;

    public DNS(Input input) throws TextParseException {
        this.setInput(input);
    }

    public void setInput(Input input) throws TextParseException {
        String domain = input.getDomain();
        String type = input.getType();

        int entry = getType(type);
        String dnsRecord = getEntry(entry);

        if (entry == 0) {
            for (int rt : new int[]{Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME}) {
                List<Record> records = getRecords(domain, rt);
                String recordTypeName = Type.string(rt);

                if (records.isEmpty()) {
                    System.out.println(System.lineSeparator() + recordTypeName + " not found for domain: " + domain);
                    break;
                }
                System.out.println("\n" + recordTypeName + " records for domain: " + domain);
                records.forEach(
                        record -> System.out.println(record.rdataToString()));
            }
        } else {
            List<Record> records = getRecords(domain, entry);

            if (records.isEmpty()) {
                System.out.println(System.lineSeparator() + dnsRecord + " not found for domain: " + domain);
            } else {
                System.out.println("\n" + dnsRecord + " records for domain: " + domain);
                records.forEach(
                        record -> System.out.println(record.rdataToString()));
            }
        }
        this.input = input;
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

    private static boolean isValid(int entry) {
        return switch (entry) {
            case Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME, 0 -> true;
            default -> false;
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

//       String dnsRecord;
////        try {
////            if (recordType == 0) {
////                for (int entry : new int[]{Type.A, Type.MX, Type.NS, Type.TXT, Type.SOA, Type.CNAME}) {
////
////                    List<Record> records = getRecords(domain, entry);
////                    dnsRecord = Type.string(entry);
////                    System.out.println("\n" + dnsRecord + " records for domain: " + domain);
////                    records.forEach(record -> System.out.println(record.rdataToString()));
////                }
////            } else {
////                List<Record> records = getRecords(domain, recordType);
////                System.out.println("\n" + recordType + " for domain: " + domain);
////                records.forEach(record -> System.out.println(record.rdataToString()));
////            }
////        } catch (Exception e) {
////
////            System.out.println( dnsRecord + " Record not found for domain: "+ domain);
////        }

// switch (currentType) {
//            case Type.A -> type = "A";
//            case Type.MX -> type = "MX";
//            case Type.NS -> type = "NS";
//            case Type.TXT -> type = "TXT";
//            case Type.SOA -> type = "SOA";
//            case Type.CNAME -> type = "CNAME";
//            default -> {
//                System.out.println(type + "Please enter a valid type");
//                type = scanner.nextLine();
//            }


//DNS.recordTypes record = DNS.recordTypes.parseString(type);
//
//        String currentType = null;
//
//        while ("INVALID".equals(currentType)) {
//              currentType =  switch (record) {
//                case A -> currentType = "A";
//                case MX -> currentType = "MX";
//                case NS -> currentType = "NS";
//                case TXT -> currentType = "TXT";
//                case SOA -> currentType = "SOA";
//                case CNAME -> currentType = "CNAME";
//                case INVALID -> currentType = scanner.nextLine();
//            };
//        }

//private static void doesExist(String domain, int entry) throws TextParseException {
//        List<Record> records = getRecords(domain, entry);
//
//        while (records != null && !records.isEmpty()) {
//            System.out.println("\n" + entry + " records for domain: " + domain);
//            records.forEach(record -> System.out.println(record.rdataToString()));
//        }
//    }