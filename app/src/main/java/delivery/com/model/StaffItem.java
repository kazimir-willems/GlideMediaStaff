package delivery.com.model;

import java.io.Serializable;

/**
 * Created by Caesar on 6/20/2017.
 */

public class StaffItem implements Serializable{
    private String staffID;
    private String staffName;

    public StaffItem() {
        staffID = "";
        staffName = "";
    }

    public StaffItem(String id, String name) {
        this.staffID = id;
        this.staffName = name;
    }

    public void setStaffID(String value) {
        this.staffID = value;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffName(String value) {
        this.staffName = value;
    }

    public String getStaffName() {
        return staffName;
    }
}
