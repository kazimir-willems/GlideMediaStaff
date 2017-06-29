package staff.com.task;

import android.content.Context;
import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import staff.com.db.BayDB;
import staff.com.db.StockDB;
import staff.com.db.ZoneDB;
import staff.com.event.MakeUploadDataEvent;
import staff.com.model.BayItem;
import staff.com.model.StockItem;
import staff.com.model.ZoneItem;

public class MakeUploadDataTask extends AsyncTask<Void, Void, String> {

    private String despatches;
    private Context ctx;

    public MakeUploadDataTask(Context context) {
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            JSONObject allData = new JSONObject();

            JSONArray jsonBayListArray = new JSONArray();
            ZoneDB zoneDB = new ZoneDB(ctx);
            BayDB bayDB = new BayDB(ctx);
            StockDB stockDB = new StockDB(ctx);
            ArrayList<ZoneItem> zoneList = zoneDB.fetchCompletedZone();
            for(int i = 0; i < zoneList.size(); i++) {
                ZoneItem zoneItem = zoneList.get(i);
                ArrayList<BayItem> bayList = bayDB.fetchBayByZone(zoneItem);

                for(int j = 0; j < bayList.size(); j++) {
                    BayItem bayItem = bayList.get(j);
                    JSONObject jsonBayItem = new JSONObject();
                    jsonBayItem.put("id", bayItem.getBayID());
                    jsonBayItem.put("warehouseid", bayItem.getWarehouseID());
                    jsonBayItem.put("zoneid", bayItem.getZoneID());
                    jsonBayItem.put("bay", bayItem.getBay());

                    JSONArray jsonStockArray = new JSONArray();

                    ArrayList<StockItem> stockItems = stockDB.fetchStocksByBay(bayItem);
                    for(int k = 0; k < stockItems.size(); k++) {
                        StockItem stockItem = stockItems.get(k);

                        JSONObject jsonStockItem = new JSONObject();
                        jsonStockItem.put("id", stockItem.getStockID());
                        jsonStockItem.put("titleid", stockItem.getTitleID());
                        jsonStockItem.put("warehouseID", stockItem.getWarehouseID());
                        jsonStockItem.put("zoneID", stockItem.getZoneID());
                        jsonStockItem.put("bayID", stockItem.getBayID());
                        jsonStockItem.put("title", stockItem.getTitle());
                        jsonStockItem.put("status", stockItem.getStatus());
                        jsonStockItem.put("customer", stockItem.getCustomer());
                        jsonStockItem.put("laststocktakeqty", stockItem.getLastStockTakeQty());
                        jsonStockItem.put("laststocktakedate", stockItem.getLastStockTakeDate());
                        jsonStockItem.put("qtyEstimate", stockItem.getQtyEstimate());
                        jsonStockItem.put("qtyBox", stockItem.getQtyBox());
                        jsonStockItem.put("lastPallet", stockItem.getLastPallet());
                        jsonStockItem.put("lastBox", stockItem.getLastBox());
                        jsonStockItem.put("lastLoose", stockItem.getLastLoose());
                        jsonStockItem.put("newPallet", stockItem.getNewPallet());
                        jsonStockItem.put("newBox", stockItem.getNewBox());
                        jsonStockItem.put("newLoose", stockItem.getNewLoose());
                        jsonStockItem.put("newIssue", stockItem.getNewIssue());
                        jsonStockItem.put("newWarehouse", stockItem.getNewWarehouse());
                        jsonStockItem.put("newZone", stockItem.getNewZone());
                        jsonStockItem.put("newBay", stockItem.getNewBay());
                        jsonStockItem.put("datetimestamp", stockItem.getDateTimeStamp());
                        jsonStockItem.put("staffid", stockItem.getStaffID());

                        jsonStockArray.put(jsonStockItem);
                    }

                    jsonBayItem.put("stock", jsonStockArray);
                    jsonBayListArray.put(jsonBayItem);
                }
            }

            allData.put("bayList", jsonBayListArray);
            return allData.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        EventBus.getDefault().post(new MakeUploadDataEvent(result));
    }
}