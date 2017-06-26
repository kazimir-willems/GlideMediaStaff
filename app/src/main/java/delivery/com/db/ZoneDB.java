package delivery.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import delivery.com.consts.DBConsts;
import delivery.com.model.WarehouseItem;
import delivery.com.model.ZoneItem;
import delivery.com.util.DBHelper;

public class ZoneDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    public ZoneDB(Context context) {
        super(context);
    }

    public ArrayList<ZoneItem> fetchZoneByWarehouseID(String warehouseID) {
        ArrayList<ZoneItem> ret = null;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + warehouseID + "'";
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_ZONE, null, szWhere, null, null, null, null);
                ret = createZoneBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public ArrayList<ZoneItem> fetchAllZone() {
        ArrayList<ZoneItem> ret = null;
        try {
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_ZONE, null, null, null, null, null, null);
                ret = createZoneBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(ZoneItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + item.getWarehouseID() + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + item.getZoneID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_ZONE, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addZone(ZoneItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_ZONE_ID, bean.getZoneID());
                value.put(DBConsts.FIELD_WAREHOUSE_ID, bean.getWarehouseID());
                value.put(DBConsts.FIELD_ZONE, bean.getZone());
                value.put(DBConsts.FIELD_COMPLETED, bean.getCompleted());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_ZONE, null, value);
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
                db.delete(DBConsts.TABLE_NAME_ZONE, null, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void removeZone(String warehouseID, ZoneItem zoneItem) {
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + warehouseID + "' AND " + DBConsts.FIELD_ZONE_ID + " = '" + zoneItem.getZoneID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                db.delete(DBConsts.TABLE_NAME_ZONE, szWhere, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<ZoneItem> createZoneBeans(Cursor c) {
        ArrayList<ZoneItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	            = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_ZONE_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_ZONE_ID),
                    COL_WAREHOUSE_ID     	= c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ID),
                    COL_ZONE  		        = c.getColumnIndexOrThrow(DBConsts.FIELD_ZONE),
                    COL_COMPLETED           = c.getColumnIndexOrThrow(DBConsts.FIELD_COMPLETED);

            while (c.moveToNext()) {
                ZoneItem bean = new ZoneItem();
                bean.setZoneID(c.getString(COL_ZONE_ID));
                bean.setWarehouseID(c.getString(COL_WAREHOUSE_ID));
                bean.setZone(c.getString(COL_ZONE));
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
