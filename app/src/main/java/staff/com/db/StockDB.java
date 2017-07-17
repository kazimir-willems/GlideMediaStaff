package staff.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import staff.com.consts.DBConsts;
import staff.com.consts.StateConsts;
import staff.com.model.BayItem;
import staff.com.model.StockItem;
import staff.com.util.DBHelper;

public class StockDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    public StockDB(Context context) {
        super(context);
    }

    public ArrayList<StockItem> fetchStocksByBay(BayItem item) {
        ArrayList<StockItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + item.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + item.getZoneID() + "' AND " + DBConsts.FIELD_BAY_ID + " = '" + item.getBayID() + "'";
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                ret = createStockBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public ArrayList<StockItem> fetchCompletedStocks(BayItem bayItem) {
        ArrayList<StockItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + bayItem.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + bayItem.getZoneID() + "' AND " + DBConsts.FIELD_BAY_ID + " = '" + bayItem.getBayID() + "' AND " + DBConsts.FIELD_COMPLETED + " = " + StateConsts.STATE_COMPLETED;
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                ret = createStockBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public ArrayList<StockItem> fetchStockByTitleID(String titleID) {
        ArrayList<StockItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_TITLE_ID + " = '" + titleID + "'";
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                ret = createStockBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public ArrayList<StockItem> fetchStockByStockID(String id) {
        ArrayList<StockItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_UNIQUE_ID + " = '" + id + "'";
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                ret = createStockBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(StockItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + item.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + item.getZoneID() + "' AND " + DBConsts.FIELD_BAY_ID + " = '" + item.getBayID() + "' AND " + DBConsts.FIELD_UNIQUE_ID + " = '" + item.getId() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addStock(StockItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_UNIQUE_ID, bean.getId());
                value.put(DBConsts.FIELD_STOCK_ID, bean.getStockID());
                value.put(DBConsts.FIELD_TITLE_ID, bean.getTitleID());
                value.put(DBConsts.FIELD_WAREHOUSE_ID, bean.getWarehouseID());
                value.put(DBConsts.FIELD_ZONE_ID, bean.getZoneID());
                value.put(DBConsts.FIELD_BAY_ID, bean.getBayID());
                value.put(DBConsts.FIELD_TITLE, bean.getTitle());
                value.put(DBConsts.FIELD_STATUS, bean.getStatus());
                value.put(DBConsts.FIELD_CUSTOMER, bean.getCustomer());
                value.put(DBConsts.FIELD_LAST_STOCKTAKE_QTY, bean.getLastStockTakeQty());
                value.put(DBConsts.FIELD_LAST_STOCKTAKE_DATE, bean.getLastStockTakeDate());
                value.put(DBConsts.FIELD_QTY_ESTIMATE, bean.getQtyEstimate());
                value.put(DBConsts.FIELD_QTY_BOX, bean.getQtyBox());
                value.put(DBConsts.FIELD_LAST_PALLET, bean.getLastPallet());
                value.put(DBConsts.FIELD_LAST_BOX, bean.getLastBox());
                value.put(DBConsts.FIELD_LAST_LOOSE, bean.getLastLoose());
                value.put(DBConsts.FIELD_NEW_PALLET, bean.getNewPallet());
                value.put(DBConsts.FIELD_NEW_BOX, bean.getNewBox());
                value.put(DBConsts.FIELD_NEW_LOOSE, bean.getNewLoose());
                value.put(DBConsts.FIELD_NEW_TOTAL, bean.getNewTotal());
                value.put(DBConsts.FIELD_NEW_ISSUE, bean.getNewIssue());
                value.put(DBConsts.FIELD_NEW_WAREHOUSE, bean.getNewWarehouse());
                value.put(DBConsts.FIELD_NEW_ZONE, bean.getNewZone());
                value.put(DBConsts.FIELD_NEW_BAY, bean.getNewBay());
                value.put(DBConsts.FIELD_DATE_TIMESTAMP, bean.getDateTimeStamp());
                value.put(DBConsts.FIELD_STAFF_ID, bean.getStaffID());
                value.put(DBConsts.FIELD_STOCK_RECEIVED, bean.getStockReceived());
                value.put(DBConsts.FIELD_COMPLETED, bean.getCompleted());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_STOCK, null, value);
                    db.close();
                }
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }

        return ret;
    }

    public int getAllCount(BayItem bayItem) {
        int count = 0;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + bayItem.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + bayItem.getZoneID() + "' AND " + DBConsts.FIELD_BAY_ID + " = '" + bayItem.getBayID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                count = cursor.getCount();
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return count;
    }

    public int getCompletedCount(BayItem bayItem) {
        int count = 0;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + bayItem.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + bayItem.getZoneID() + "' AND " + DBConsts.FIELD_BAY_ID + " = '" + bayItem.getBayID() + "' AND " + DBConsts.FIELD_COMPLETED + " = " + StateConsts.STATE_COMPLETED;

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STOCK, null, szWhere, null, null, null, null);
                count = cursor.getCount();
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return count;
    }

    public void updateStock(StockItem item) {
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + item.getWarehouseID() + "' AND " +
                    DBConsts.FIELD_ZONE_ID + " = '" + item.getZoneID() + "' AND " +
                    DBConsts.FIELD_BAY_ID + " = '" + item.getBayID() + "' AND " +
                    DBConsts.FIELD_UNIQUE_ID + " = '" + item.getId() + "'";
            ContentValues value = new ContentValues();

            value.put(DBConsts.FIELD_NEW_PALLET, item.getNewPallet());
            value.put(DBConsts.FIELD_NEW_BOX, item.getNewBox());
            value.put(DBConsts.FIELD_NEW_LOOSE, item.getNewLoose());
            value.put(DBConsts.FIELD_NEW_TOTAL, item.getNewTotal());
            value.put(DBConsts.FIELD_NEW_WAREHOUSE, item.getNewWarehouse());
            value.put(DBConsts.FIELD_NEW_ZONE, item.getNewZone());
            value.put(DBConsts.FIELD_NEW_BAY, item.getNewBay());
            value.put(DBConsts.FIELD_NEW_ISSUE, item.getNewIssue());
            value.put(DBConsts.FIELD_DATE_TIMESTAMP, item.getDateTimeStamp());
            value.put(DBConsts.FIELD_COMPLETED, item.getCompleted());
            value.put(DBConsts.FIELD_STAFF_ID, item.getStaffID());

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                int res = db.update(DBConsts.TABLE_NAME_STOCK, value, szWhere, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void removeDatasByZoneID(String warehouseID, String zoneID) {
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + warehouseID + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + zoneID + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                db.delete(DBConsts.TABLE_NAME_STOCK, szWhere, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void removeAllDatas() {
        try {
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                db.delete(DBConsts.TABLE_NAME_STOCK, null, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<StockItem> createStockBeans(Cursor c) {
        ArrayList<StockItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	                = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_UNIQUE_ID               = c.getColumnIndexOrThrow(DBConsts.FIELD_UNIQUE_ID),
                    COL_STOCK_ID 	            = c.getColumnIndexOrThrow(DBConsts.FIELD_STOCK_ID),
                    COL_TITLE_ID                = c.getColumnIndexOrThrow(DBConsts.FIELD_TITLE_ID),
                    COL_WAREHOUSE_ID         	= c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ID),
                    COL_ZONE_ID   		        = c.getColumnIndexOrThrow(DBConsts.FIELD_ZONE_ID),
                    COL_BAY_ID                  = c.getColumnIndexOrThrow(DBConsts.FIELD_BAY_ID),
                    COL_TITLE    	 	        = c.getColumnIndexOrThrow(DBConsts.FIELD_TITLE),
                    COL_STATUS     		        = c.getColumnIndexOrThrow(DBConsts.FIELD_STATUS),
                    COL_CUSTOMER     	 	    = c.getColumnIndexOrThrow(DBConsts.FIELD_CUSTOMER),
                    COL_LASTSTOCKTAKE_QTY    	= c.getColumnIndexOrThrow(DBConsts.FIELD_LAST_STOCKTAKE_QTY),
                    COL_LASTSTOCKTAKE_DATE   	= c.getColumnIndexOrThrow(DBConsts.FIELD_LAST_STOCKTAKE_DATE),
                    COL_QTY_ESTIMATE    	 	= c.getColumnIndexOrThrow(DBConsts.FIELD_QTY_ESTIMATE),
                    COL_QTY_BOX     		    = c.getColumnIndexOrThrow(DBConsts.FIELD_QTY_BOX),
                    COL_LAST_PALLET  		    = c.getColumnIndexOrThrow(DBConsts.FIELD_LAST_PALLET),
                    COL_LAST_BOX     	 	    = c.getColumnIndexOrThrow(DBConsts.FIELD_LAST_BOX),
                    COL_LAST_LOOSE    	 	    = c.getColumnIndexOrThrow(DBConsts.FIELD_LAST_LOOSE),
                    COL_NEW_PALLET   	 	    = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_PALLET),
                    COL_NEW_BOX    	 	        = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_BOX),
                    COL_NEW_LOOSE     		    = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_LOOSE),
                    COL_NEW_TOTAL               = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_TOTAL),
                    COL_NEW_ISSUE  		        = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_ISSUE),
                    COL_NEW_WAREHOUSE     	 	= c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_WAREHOUSE),
                    COL_NEW_ZONE    	 	    = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_ZONE),
                    COL_NEW_BAY     		    = c.getColumnIndexOrThrow(DBConsts.FIELD_NEW_BAY),
                    COL_DATETIME_STAMP  		= c.getColumnIndexOrThrow(DBConsts.FIELD_DATE_TIMESTAMP),
                    COL_STAFF_ID     	 	    = c.getColumnIndexOrThrow(DBConsts.FIELD_STAFF_ID),
                    COL_STOCK_RECEIVED          = c.getColumnIndexOrThrow(DBConsts.FIELD_STOCK_RECEIVED),
                    COL_COMPLETED               = c.getColumnIndexOrThrow(DBConsts.FIELD_COMPLETED);

            while (c.moveToNext()) {
                StockItem bean = new StockItem();
                bean.setId(c.getString(COL_UNIQUE_ID));
                bean.setStockID(c.getString(COL_STOCK_ID));
                bean.setTitleID(c.getString(COL_TITLE_ID));
                bean.setWarehouseID(c.getString(COL_WAREHOUSE_ID));
                bean.setZoneID(c.getString(COL_ZONE_ID));
                bean.setBayID(c.getString(COL_BAY_ID));
                bean.setTitle(c.getString(COL_TITLE));
                bean.setStatus(c.getString(COL_STATUS));
                bean.setCustomer(c.getString(COL_CUSTOMER));
                bean.setLastStockTakeQty(c.getString(COL_LASTSTOCKTAKE_QTY));
                bean.setLastStockTakeDate(c.getString(COL_LASTSTOCKTAKE_DATE));
                bean.setQtyEstimate(c.getString(COL_QTY_ESTIMATE));
                bean.setQtyBox(c.getString(COL_QTY_BOX));
                bean.setLastPallet(c.getString(COL_LAST_PALLET));
                bean.setLastBox(c.getString(COL_LAST_BOX));
                bean.setLastLoose(c.getString(COL_LAST_LOOSE));
                bean.setNewPallet(c.getString(COL_NEW_PALLET));
                bean.setNewBox(c.getString(COL_NEW_BOX));
                bean.setNewLoose(c.getString(COL_NEW_LOOSE));
                bean.setNewTotal(c.getString(COL_NEW_TOTAL));
                bean.setNewIssue(c.getString(COL_NEW_ISSUE));
                bean.setNewWarehouse(c.getString(COL_NEW_WAREHOUSE));
                bean.setNewZone(c.getString(COL_NEW_ZONE));
                bean.setNewBay(c.getString(COL_NEW_BAY));
                bean.setDateTimeStamp(c.getString(COL_DATETIME_STAMP));
                bean.setStaffID(c.getString(COL_STAFF_ID));
                bean.setStockReceived(c.getString(COL_STOCK_RECEIVED));
                bean.setCompleted(c.getInt(COL_COMPLETED));
                ret.add(bean);
            }

            c.close();
            getReadableDatabase().close();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }
}
