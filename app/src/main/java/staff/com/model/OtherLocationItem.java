package staff.com.model;

/**
 * Created by Arthur on 7/7/2017.
 */

public class OtherLocationItem {
    private String stockID;
    private String otherID;
    private String warehouseID;
    private String zoneID;
    private String bayID;

    public OtherLocationItem() {
        stockID = "";
        otherID = "";
        warehouseID = "";
        zoneID = "";
        bayID = "";
    }

    public void setStockID(String value) {
        this.stockID = value;
    }

    public String getStockID() {
        return stockID;
    }

    public void setOtherID(String value) {
        this.otherID = value;
    }

    public String getOtherID() {
        return otherID;
    }

    public void setWarehouseID(String value) {
        this.warehouseID = value;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public void setZoneID(String value) {
        this.zoneID = value;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setBayID(String value) {
        this.bayID = value;
    }

    public String getBayID() {
        return bayID;
    }
}
