package staff.com.task;

import android.content.Context;
import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import staff.com.db.BayDB;
import staff.com.db.IssueDB;
import staff.com.db.OtherLocationDB;
import staff.com.db.StaffDB;
import staff.com.db.StockDB;
import staff.com.db.WarehouseDB;
import staff.com.db.ZoneDB;
import staff.com.event.RemoveAllDataEvent;

public class RemoveAllDataTask extends AsyncTask<String, Void, Boolean> {

    private Context context;

    public RemoveAllDataTask(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            StockDB stockDB = new StockDB(context);
            stockDB.removeAllDatas();
            BayDB bayDB = new BayDB(context);
            bayDB.removeAllDatas();
            ZoneDB zoneDB = new ZoneDB(context);
            zoneDB.removeAllDatas();
            WarehouseDB warehouseDB = new WarehouseDB(context);
            warehouseDB.removeAllDatas();
            IssueDB issueDB = new IssueDB(context);
            issueDB.removeAllDatas();
            StaffDB staffDB = new StaffDB(context);
            staffDB.removeAllDatas();
            OtherLocationDB otherLocationDB = new OtherLocationDB(context);
            otherLocationDB.removeAllDatas();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        EventBus.getDefault().post(new RemoveAllDataEvent(result));
    }
}