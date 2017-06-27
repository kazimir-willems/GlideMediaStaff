package delivery.com.model;

import java.io.Serializable;

/**
 * Created by Caesar on 4/21/2017.
 */

public class StockItem implements Serializable {
    //NNew Stock Information
    private String stockID;
    private String titleID;
    private String warehouseID;
    private String zoneID;
    private String bayID;
    private String title;
    private String status;
    private String customer;
    private String lastStockTakeQty;
    private String lastStockTakeDate;
    private String qtyEstimate;
    private String qtyBox;
    private String lastPallet;
    private String lastBox;
    private String lastLoose;
    private String newPallet;
    private String newBox;
    private String newLoose;
    private String newIssue;
    private String newWarehouse;
    private String newZone;
    private String newBay;
    private String dateTimeStamp;
    private String staffID;
    private int completed;


    public StockItem() {
    }

    public void setStockID(String value) {
        this.stockID = value;
    }

    public String getStockID() {
        return stockID;
    }

    public void setTitleID(String value) {
        this.titleID = value;
    }

    public String getTitleID() {
        return titleID;
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

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getStatus() {
        return status;
    }

    public void setCustomer(String value) {
        this.customer = value;
    }

    public String getCustomer() {
        return customer;
    }

    public void setLastStockTakeQty(String value) {
        this.lastStockTakeQty = value;
    }

    public String getLastStockTakeQty() {
        return lastStockTakeQty;
    }

    public void setLastStockTakeDate(String value) {
        this.lastStockTakeDate = value;
    }

    public String getLastStockTakeDate() {
        return lastStockTakeDate;
    }

    public void setQtyEstimate(String value) {
        this.qtyEstimate = value;
    }

    public String getQtyEstimate() {
        return qtyEstimate;
    }

    public void setQtyBox(String value) {
        this.qtyBox = value;
    }

    public String getQtyBox() {
        return qtyBox;
    }

    public void setLastPallet(String value) {
        this.lastPallet = value;
    }

    public String getLastPallet() {
        return lastPallet;
    }

    public void setLastBox(String value) {
        this.lastBox = value;
    }

    public String getLastBox() {
        return lastBox;
    }

    public void setLastLoose(String value) {
        this.lastLoose = value;
    }

    public String getLastLoose() {
        return lastLoose;
    }

    public void setNewPallet(String value) {
        this.newPallet = value;
    }

    public String getNewPallet() {
        return newPallet;
    }

    public void setNewBox(String value) {
        this.newBox = value;
    }

    public String getNewBox() {
        return newBox;
    }

    public void setNewLoose(String value) {
        this.newLoose = value;
    }

    public String getNewLoose() {
        return newLoose;
    }

    public void setNewIssue(String value) {
        this.newIssue = value;
    }

    public String getNewIssue() {
        return newIssue;
    }

    public void setNewWarehouse(String value) {
        this.newWarehouse = value;
    }

    public String getNewWarehouse() {
        return newWarehouse;
    }

    public void setNewZone(String value) {
        this.newZone = value;
    }

    public String getNewZone() {
        return newZone;
    }

    public void setNewBay(String value) {
        this.newBay = value;
    }

    public String getNewBay() {
        return newBay;
    }

    public void setDateTimeStamp(String value) {
        this.dateTimeStamp = value;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setStaffID(String value) {
        this.staffID = value;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setCompleted(int value) {
        this.completed = value;
    }

    public int getCompleted() {
        return completed;
    }
}
