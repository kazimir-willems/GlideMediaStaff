package staff.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import staff.com.consts.DBConsts;
import staff.com.model.StaffItem;
import staff.com.util.DBHelper;

public class StaffDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    public StaffDB(Context context) {
        super(context);
    }

    public ArrayList<StaffItem> fetchAllStaff() {
        ArrayList<StaffItem> ret = null;
        try {
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STAFF, null, null, null, null, null, null);
                ret = createStaffBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(StaffItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_STAFF_ID + " = '" + item.getStaffID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_STAFF, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addStaff(StaffItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_STAFF_ID, bean.getStaffID());
                value.put(DBConsts.FIELD_STAFF_NAME, bean.getStaffName());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_STAFF, null, value);
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
                db.delete(DBConsts.TABLE_NAME_STAFF, null, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<StaffItem> createStaffBeans(Cursor c) {
        ArrayList<StaffItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	            = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_STAFF_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_STAFF_ID),
                    COL_STAFF_NAME     		= c.getColumnIndexOrThrow(DBConsts.FIELD_STAFF_NAME);

            while (c.moveToNext()) {
                StaffItem bean = new StaffItem();
                bean.setStaffID(c.getString(COL_STAFF_ID));
                bean.setStaffName(c.getString(COL_STAFF_NAME));
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
