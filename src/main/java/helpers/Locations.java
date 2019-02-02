package helpers;

public enum Locations {
    CHALFONT_ST_PETER("gcpte6m6w", "Chalfont St. Peter"),
    UXBRIDGE("gcptm05c7", "Uxbridge");

    public final String metOfficeHash;
    public final String metOfficeLocation;

    Locations(String metOfficeHash, String metOfficeLocation) {
        this.metOfficeHash = metOfficeHash;
        this.metOfficeLocation = metOfficeLocation;
    }
}
