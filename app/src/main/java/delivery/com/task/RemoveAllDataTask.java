package delivery.com.task;

import android.content.Context;
import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import delivery.com.db.BayDB;
import delivery.com.db.IssueDB;
import delivery.com.db.StaffDB;
import delivery.com.db.StockDB;
import delivery.com.db.WarehouseDB;
import delivery.com.db.ZoneDB;
import delivery.com.event.RemoveAllDataEvent;

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