package delivery.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import delivery.com.consts.DBConsts;
import delivery.com.model.BayItem;
import delivery.com.model.ZoneItem;
import delivery.com.util.DBHelper;

public class BayDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    public BayDB(Context context) {
        super(context);
    }

    public ArrayList<BayItem> fetchBayByZone(ZoneItem zoneItem) {
        ArrayList<BayItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + zoneItem.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + zoneItem.getZoneID() + "'";
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_BAY, null, szWhere, null, null, null, null);
                ret = createBayBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(BayItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_BAY_ID + " = '" + item.getBayID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_BAY, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addBay(BayItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_BAY_ID, bean.getBayID());
                value.put(DBConsts.FIELD_BAY, bean.getBay());
                value.put(DBConsts.FIELD_ZONE_ID, bean.getZoneID());
                value.put(DBConsts.FIELD_WAREHOUSE_ID, bean.getWarehouseID());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_BAY, null, value);
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
                db.delete(DBConsts.TABLE_NAME_BAY, null, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void removeBayByZone(ZoneItem zoneItem) {
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + zoneItem.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + zoneItem.getZoneID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                db.delete(DBConsts.TABLE_NAME_BAY, szWhere, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<BayItem> createBayBeans(Cursor c) {
        ArrayList<BayItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	        = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_BAY_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_BAY_ID),
                    COL_BAY     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ID),
                    COL_ZONE_ID  		= c.getColumnIndexOrThrow(DBConsts.FIELD_ZONE_ID),
                    COL_WAREHOUSE_ID  	= c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ID);

            while (c.moveToNext()) {
                BayItem bean = new BayItem();
                bean.setBayID(c.getString(COL_BAY_ID));
                bean.setBay(c.getString(COL_BAY));
                bean.setZoneID(c.getString(COL_ZONE_ID));
                bean.setWarehouseID(c.getString(COL_WAREHOUSE_ID));
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
