package codingTechniques.model;

public enum Role {
    FARMER("Farmer"),
    BUYER("Buyer"),
    MARKET_OFFICIAL("Market Official");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
