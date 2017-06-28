package delivery.com.task;

import android.content.Context;
import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import delivery.com.db.BayDB;
import delivery.com.db.StockDB;
import delivery.com.db.ZoneDB;
import delivery.com.event.MakeModifiedDataEvent;
import delivery.com.event.MakeUploadDataEvent;
import delivery.com.model.BayItem;
import delivery.com.model.StockItem;
import delivery.com.model.ZoneItem;

public class MakeModifiedDataTask extends AsyncTask<Void, Void, String> {

    private String despatches;
    private Context ctx;

    public MakeModifiedDataTask(Context context) {
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

            JSONArray jsonStockArray = new JSONArray();

            ArrayList<StockItem> stockItems = stockDB.fetchCompletedStocks();
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

            allData.put("stockList", jsonStockArray);
            return allData.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        EventBus.getDefault().post(new MakeModifiedDataEvent(result));
    }
}