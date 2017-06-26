package delivery.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import delivery.com.consts.DBConsts;
import delivery.com.model.IssueItem;
import delivery.com.model.StaffItem;
import delivery.com.util.DBHelper;

public class IssueDB extends DBHelper {
    private static final Object[] DB_LOCK 		= new Object[0];

    public IssueDB(Context context) {
        super(context);
    }

    public ArrayList<IssueItem> fetchAllIssue() {
        ArrayList<IssueItem> ret = null;
        try {
            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_ISSUE, null, null, null, null, null, null);
                ret = createIssueBeans(cursor);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean isExist(IssueItem item) {
        boolean bExist = false;
        try {
            String szWhere = DBConsts.FIELD_ISSUE_ID + " = '" + item.getIssueID() + "'";

            synchronized (DB_LOCK) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.query(DBConsts.TABLE_NAME_ISSUE, null, szWhere, null, null, null, null);
                if(cursor.moveToNext())
                    bExist = true;
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

        return bExist;
    }

    public long addIssue(IssueItem bean) {
        long ret = -1;
        if(!isExist(bean)) {
            try {
                ContentValues value = new ContentValues();
                value.put(DBConsts.FIELD_ISSUE_ID, bean.getIssueID());
                value.put(DBConsts.FIELD_ISSUE_NAME, bean.getIssueName());
                synchronized (DB_LOCK) {
                    SQLiteDatabase db = getWritableDatabase();
                    ret = db.insert(DBConsts.TABLE_NAME_ISSUE, null, value);
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
                db.delete(DBConsts.TABLE_NAME_ISSUE, null, null);
                db.close();
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<IssueItem> createIssueBeans(Cursor c) {
        ArrayList<IssueItem> ret = null;
        try {
            ret = new ArrayList();

            final int COL_ID	            = c.getColumnIndexOrThrow(DBConsts.FIELD_ID),
                    COL_ISSUE_ID     	    = c.getColumnIndexOrThrow(DBConsts.FIELD_ISSUE_ID),
                    COL_ISSUE_NAME     		= c.getColumnIndexOrThrow(DBConsts.FIELD_ISSUE_NAME);

            while (c.moveToNext()) {
                IssueItem bean = new IssueItem();
                bean.setIssueID(c.getString(COL_ISSUE_ID));
                bean.setIssueName(c.getString(COL_ISSUE_NAME));
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
