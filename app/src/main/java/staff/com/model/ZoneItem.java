package staff.com.model;

import java.io.Serializable;

/**
 * Created by Caesar on 6/20/2017.
 */

public class ZoneItem implements Serializable {
    private String warehouseID;
    private String zoneID;
    private String zone;
    private int completed;

    public ZoneItem() {
        this.warehouseID = "";
        this.zoneID = "";
        this.zone = "";
        this.completed = 0;
    }

    public ZoneItem(String warehouseID, String zoneID, String zone) {
        this.warehouseID = warehouseID;
        this.zoneID = zoneID;
        this.zone = zone;
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

    public void setZone(String value) {
        this.zone = value;
    }

    public String getZone() {
        return  zone;
    }

    public void setCompleted(int value) {
        this.completed = value;
    }

    public int getCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return zone;
    }
}
