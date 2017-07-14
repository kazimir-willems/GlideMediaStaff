package staff.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import staff.com.consts.DBConsts;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;
import staff.com.util.DBHelper;

public class WarehouseDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    private Context ctx;

    public WarehouseDB(Context context) {
        super(context);

        ctx = context;
    }

    public ArrayList<WarehouseItem> fetchAllWarehouse() {
        ArrayList<WarehouseItem> ret = null;

        try {
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_WAREHOUSE, null, null, null, null, null, null);
                ret = createWarehouseBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public ArrayList<WarehouseItem> fetchWarehouseByID(String warehouseID) {
        ArrayList<WarehouseItem> ret = new ArrayList<>();
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + warehouseID + "'";
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_WAREHOUSE, null, szWhere, null, null, null, null);
                ret = createWarehouseBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(WarehouseItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_WAREHOUSE_ID + " = '" + item.getId() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_WAREHOUSE, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addWarehouse(WarehouseItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_WAREHOUSE_ID, bean.getId());
                value.put(DBConsts.FIELD_WAREHOUSE_NAME, bean.getName());
                value.put(DBConsts.FIELD_WAREHOUSE_ADDRESS, bean.getAddress());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_WAREHOUSE, null, value);
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
                db.delete(DBConsts.TABLE_NAME_WAREHOUSE, null, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<WarehouseItem> createWarehouseBeans(Cursor c) {
        ArrayList<WarehouseItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	                = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_WAREHOUSE_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ID),
                    COL_WAREHOUSE_NAME     		= c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_NAME),
                    COL_WAREHOUSE_ADDRESS  		= c.getColumnIndexOrThrow(DBConsts.FIELD_WAREHOUSE_ADDRESS);

            while (c.moveToNext()) {
                WarehouseItem bean = new WarehouseItem();
                bean.setID(c.getString(COL_WAREHOUSE_ID));
                bean.setName(c.getString(COL_WAREHOUSE_NAME));
                bean.setAddress(c.getString(COL_WAREHOUSE_ADDRESS));

                ZoneDB zoneDB = new ZoneDB(ctx);
                ArrayList<ZoneItem> items = zoneDB.fetchZoneByWarehouseID(bean.getId());
                if(items.size() > 0)
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
