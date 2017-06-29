package staff.com.model;

import java.io.Serializable;

/**
 * Created by Caesar on 6/20/2017.
 */

public class WarehouseItem implements Serializable{
    private String id;
    private String name;
    private String address;

    public WarehouseItem() {
        this.id = "";
        this.name = "";
        this.address = "";
    }

    public WarehouseItem(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getId(){
        return id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return name;
    }
}
