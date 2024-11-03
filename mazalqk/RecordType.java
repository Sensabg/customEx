public enum RecordType {
    A,
    MX,
    NS,
    TXT,
    SOA,
    CNAME,
    ALL,
    INVALID;

    public static RecordType fromString(String input) {
        return switch (input) {
            case "A" -> A;
            case "MX" ->MX;
            case "NS" ->NS;
            case "TXT" ->TXT;
            case "SOA" ->SOA;
            case "CNAME" -> CNAME;
            case "ALL" -> ALL;
            default -> INVALID;
        };
    }
}