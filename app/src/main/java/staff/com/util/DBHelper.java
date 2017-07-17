package staff.com.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import staff.com.consts.DBConsts;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME_PREFIX = "GLIDE_STAFF_DB";
    private static final int DB_VERSION = 2;

    protected static String STAFF_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_STAFF + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_STAFF_ID + " TEXT," +
                    DBConsts.FIELD_STAFF_NAME + " TEXT);";

    protected static String ISSUE_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_ISSUE + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_ISSUE_ID + " TEXT," +
                    DBConsts.FIELD_ISSUE_NAME + " TEXT);";

    protected static String WAREHOUSE_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_WAREHOUSE + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_WAREHOUSE_ID + " TEXT," +
                    DBConsts.FIELD_WAREHOUSE_NAME + " TEXT," +
                    DBConsts.FIELD_WAREHOUSE_ADDRESS + " TEXT);";

    protected static String ZONE_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_ZONE + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_WAREHOUSE_ID + " TEXT," +
                    DBConsts.FIELD_ZONE_ID + " TEXT," +
                    DBConsts.FIELD_ZONE + " TEXT, " +
                    DBConsts.FIELD_COMPLETED + " INTEGER);";

    protected static String BAY_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_BAY + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_BAY_ID + " TEXT," +
                    DBConsts.FIELD_BAY + " TEXT," +
                    DBConsts.FIELD_WAREHOUSE_ID + " TEXT," +
                    DBConsts.FIELD_ZONE_ID + " TEXT," +
                    DBConsts.FIELD_COMPLETED + " INTEGER);";

    protected static String STOCK_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_STOCK + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_UNIQUE_ID + " TEXT," +
                    DBConsts.FIELD_STOCK_ID + " TEXT," +
                    DBConsts.FIELD_TITLE_ID + " TEXT," +
                    DBConsts.FIELD_WAREHOUSE_ID + " TEXT," +
                    DBConsts.FIELD_ZONE_ID + " TEXT," +
                    DBConsts.FIELD_BAY_ID + " TEXT," +
                    DBConsts.FIELD_TITLE  + " TEXT," +
                    DBConsts.FIELD_STATUS + " TEXT," +
                    DBConsts.FIELD_CUSTOMER + " TEXT," +
                    DBConsts.FIELD_LAST_STOCKTAKE_QTY + " TEXT," +
                    DBConsts.FIELD_LAST_STOCKTAKE_DATE + " TEXT," +
                    DBConsts.FIELD_QTY_ESTIMATE + " TEXT," +
                    DBConsts.FIELD_QTY_BOX + " TEXT," +
                    DBConsts.FIELD_LAST_PALLET + " TEXT," +
                    DBConsts.FIELD_LAST_BOX + " TEXT," +
                    DBConsts.FIELD_LAST_LOOSE + " TEXT," +
                    DBConsts.FIELD_NEW_PALLET + " TEXT," +
                    DBConsts.FIELD_NEW_BOX + " TEXT," +
                    DBConsts.FIELD_NEW_LOOSE + " TEXT," +
                    DBConsts.FIELD_NEW_TOTAL + " TEXT," +
                    DBConsts.FIELD_NEW_ISSUE + " TEXT," +
                    DBConsts.FIELD_NEW_WAREHOUSE + " INTEGER," +
                    DBConsts.FIELD_NEW_ZONE + " TEXT," +
                    DBConsts.FIELD_NEW_BAY + " TEXT," +
                    DBConsts.FIELD_DATE_TIMESTAMP + " TEXT," +
                    DBConsts.FIELD_STAFF_ID + " TEXT," +
                    DBConsts.FIELD_STOCK_RECEIVED + " TEXT," +
                    DBConsts.FIELD_COMPLETED + " INTEGER);";

    protected static String OTHER_TABLE_CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + DBConsts.TABLE_NAME_OTHER_LOCATION + " (" +
                    DBConsts.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBConsts.FIELD_STOCK_ID + " TEXT," +
                    DBConsts.FIELD_OTHER_ID + " TEXT," +
                    DBConsts.FIELD_WAREHOUSE_ID + " TEXT," +
                    DBConsts.FIELD_ZONE_ID + " TEXT," +
                    DBConsts.FIELD_BAY_ID + " TEXT);";

    public DBHelper(Context context) {
        super(context, DB_NAME_PREFIX, null, DB_VERSION);
        this.getWritableDatabase().close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(STAFF_TABLE_CREATE_SQL);
            db.execSQL(ISSUE_TABLE_CREATE_SQL);
            db.execSQL(WAREHOUSE_TABLE_CREATE_SQL);
            db.execSQL(ZONE_TABLE_CREATE_SQL);
            db.execSQL(BAY_TABLE_CREATE_SQL);
            db.execSQL(STOCK_TABLE_CREATE_SQL);
            db.execSQL(OTHER_TABLE_CREATE_SQL);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            onCreate(db);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }
}
