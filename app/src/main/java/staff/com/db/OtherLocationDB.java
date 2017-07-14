package staff.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import staff.com.consts.DBConsts;
import staff.com.model.IssueItem;
import staff.com.model.OtherLocationItem;
import staff.com.model.StockItem;
import staff.com.util.DBHelper;

public class OtherLocationDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    public OtherLocationDB(Context context) {
        super(context);
    }

    public ArrayList<OtherLocationItem> fetchOtherLocation(StockItem item) {
        ArrayList<OtherLocationItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_STOCK_ID + " = '" + item.getStockID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_OTHER_LOCATION, null, szWhere, null, null, null, null);
                ret = createIssueBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(OtherLocationItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_STOCK_ID + " = '" + item.getWarehouseID() + "' AND " + DBConsts.FIELD_OTHER_ID + " = '" + item.getOtherID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_OTHER_LOCATION, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addOtherLocation(OtherLocationItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_STOCK_ID, bean.getStockID());
                value.put(DBConsts.FIELD_OTHER_ID, bean.getOtherID());
                value.put(DBConsts.FIELD_WAREHOUSE_ID, bean.getWarehouseID());
                value.put(DBConsts.FIELD_ZONE_ID, bean.getZoneID());
                value.put(DBConsts.FIELD_BAY_ID, bean.getBayID());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_OTHER_LOCATION, null, value);
                    db.close();
                }
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }

        return ret;
    }

    public void removeAllDatas() {
        try {
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                db.delete(DBConsts.TABLE_NAME_OTHER_LOCATION, null, null);
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
                db.delete(DBConsts.TABLE_NAME_OTHER_LOCATION, szWhere, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<OtherLocationItem> createIssueBeans(Cursor c) {
        ArrayList<OtherLocationItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	            = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_STOCK_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_STOCK_ID),
                    COL_OTHER_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_OTHER_ID),
                    COL_WAREHOUSE_ID   		= c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ID),
                    COL_ZONE_ID        		= c.getColumnIndexOrThrow(DBConsts.FIELD_ZONE_ID),
                    COL_BAY_ID         		= c.getColumnIndexOrThrow(DBConsts.FIELD_BAY_ID);

            while (c.moveToNext()) {
                OtherLocationItem bean = new OtherLocationItem();
                bean.setStockID(c.getString(COL_STOCK_ID));
                bean.setOtherID(c.getString(COL_OTHER_ID));
                bean.setWarehouseID(c.getString(COL_WAREHOUSE_ID));
                bean.setZoneID(c.getString(COL_ZONE_ID));
                bean.setBayID(c.getString(COL_BAY_ID));
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
