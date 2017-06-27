package delivery.com.model;

import java.io.Serializable;

/**
 * Created by Kazimir on 6/20/2017.
 */

public class BayItem implements Serializable {
    private String warehouseID;
    private String zoneID;
    private String bayID;
    private String bay;

    public BayItem() {
        this.warehouseID = "";
        this.zoneID = "";
        this.bayID = "";
        this.bay = "";
    }

    public BayItem(String warehouseID, String zoneID, String bayID, String bay) {
        this.warehouseID = warehouseID;
        this.zoneID = zoneID;
        this.bayID = bayID;
        this.bay = bay;
    }

    public void setBayID(String value) {
        this.bayID = value;
    }

    public String getBayID() {
        return bayID;
    }

    public void setBay(String value) {
        this.bay = value;
    }

    public String getBay() {
        return bay;
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

    @Override
    public String toString() {
        return bay;
    }
}
