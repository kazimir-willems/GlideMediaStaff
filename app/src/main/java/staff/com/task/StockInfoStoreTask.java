package staff.com.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import staff.com.consts.StateConsts;
import staff.com.db.BayDB;
import staff.com.db.IssueDB;
import staff.com.db.OtherLocationDB;
import staff.com.db.StaffDB;
import staff.com.db.StockDB;
import staff.com.db.WarehouseDB;
import staff.com.db.ZoneDB;
import staff.com.event.StockInfoStoreEvent;
import staff.com.model.BayItem;
import staff.com.model.IssueItem;
import staff.com.model.OtherLocationItem;
import staff.com.model.StaffItem;
import staff.com.model.StockItem;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;
import staff.com.vo.DownloadStockInfoResponseVo;

public class StockInfoStoreTask extends AsyncTask<DownloadStockInfoResponseVo, Void, Integer> {

    private DownloadStockInfoResponseVo responseVo;
    private Context ctx;

    private String stockID = "";

    public StockInfoStoreTask(Context context) {
        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Integer doInBackground(DownloadStockInfoResponseVo... params) {
        responseVo = params[0];
        try {
            //Store Staff Information
            JSONArray jsonStaffArray = new JSONArray(responseVo.staff);
            StaffDB staffDB = new StaffDB(ctx);

            for(int i = 0; i < jsonStaffArray.length(); i++) {
                JSONObject jsonStaffItem = jsonStaffArray.getJSONObject(i);

                StaffItem item = new StaffItem();
                item.setStaffID(jsonStaffItem.getString("id"));
                item.setStaffName(jsonStaffItem.getString("name"));

                staffDB.addStaff(item);
            }

            //Store Issue Information
            JSONArray jsonIssueArray = new JSONArray(responseVo.issue);
            IssueDB issueDB = new IssueDB(ctx);

            for(int i = 0; i < jsonIssueArray.length(); i++) {
                JSONObject jsonIssueItem = jsonIssueArray.getJSONObject(i);

                IssueItem item = new IssueItem();
                item.setIssueID(jsonIssueItem.getString("id"));
                item.setIssueName(jsonIssueItem.getString("reason"));

                issueDB.addIssue(item);
            }

            //Store Warehouse Information
            JSONArray jsonWarehouseArray = new JSONArray(responseVo.warehouseList);
            WarehouseDB warehouseDB = new WarehouseDB(ctx);
            ZoneDB zoneDB = new ZoneDB(ctx);

            for(int i = 0; i < jsonWarehouseArray.length(); i++) {
                JSONObject jsonWarehouseItem = jsonWarehouseArray.getJSONObject(i);

                String warehouseID = jsonWarehouseItem.getString("id");
                WarehouseItem item = new WarehouseItem();

                item.setID(warehouseID);
                item.setName(jsonWarehouseItem.getString("name"));
                item.setAddress(jsonWarehouseItem.getString("address"));

                JSONArray jsonZoneArray = new JSONArray(jsonWarehouseItem.getString("zone"));
                for(int j = 0; j < jsonZoneArray.length(); j++) {
                    JSONObject jsonZoneItem = jsonZoneArray.getJSONObject(j);

                    ZoneItem zoneItem = new ZoneItem();

                    zoneItem.setZoneID(jsonZoneItem.getString("id"));
                    zoneItem.setWarehouseID(warehouseID);
                    zoneItem.setZone(jsonZoneItem.getString("zone"));

                    zoneDB.addZone(zoneItem);
                }

                warehouseDB.addWarehouse(item);
            }

            //Store BayList Information
            JSONArray jsonBayArray = new JSONArray(responseVo.bayList);
            BayDB bayDB = new BayDB(ctx);
            StockDB stockDB = new StockDB(ctx);

            OtherLocationDB otherLocationDB = new OtherLocationDB(ctx);

            for(int i = 0; i < jsonBayArray.length(); i++) {
                JSONObject jsonBayItem = jsonBayArray.getJSONObject(i);

                String bayID = jsonBayItem.getString("id");
                String warehouseID = jsonBayItem.getString("warehouseid");
                String zoneID = jsonBayItem.getString("zoneid");

                BayItem bayItem = new BayItem();
                bayItem.setBayID(bayID);
                bayItem.setBay(jsonBayItem.getString("bay"));
                bayItem.setZoneID(zoneID);
                bayItem.setWarehouseID(warehouseID);
                bayItem.setCompleted(StateConsts.STATE_DEFAULT);

                JSONArray jsonStockArray = new JSONArray(jsonBayItem.getString("stock"));
                for(int j = 0; j < jsonStockArray.length(); j++) {
                    JSONObject jsonStockItem = jsonStockArray.getJSONObject(j);

                    StockItem stockItem = new StockItem();

                    stockID = jsonStockItem.getString("id");

                    stockItem.setId(jsonStockItem.getString("id"));
                    stockItem.setStockID(jsonStockItem.getString("stockid"));
                    stockItem.setTitleID(jsonStockItem.getString("titleid"));
                    stockItem.setWarehouseID(warehouseID);
                    stockItem.setZoneID(zoneID);
                    stockItem.setBayID(bayID);
                    stockItem.setTitle(jsonStockItem.getString("title"));
                    stockItem.setStatus(jsonStockItem.getString("status"));
                    stockItem.setCustomer(jsonStockItem.getString("customer"));
                    stockItem.setLastStockTakeQty(jsonStockItem.getString("laststocktakeqty"));
                    stockItem.setLastStockTakeDate(jsonStockItem.getString("laststocktakedate"));
                    stockItem.setQtyEstimate(jsonStockItem.getString("qtyEstimate"));
                    stockItem.setQtyBox(jsonStockItem.getString("qtyBox"));
                    stockItem.setLastPallet(jsonStockItem.getString("lastPallet"));
                    stockItem.setLastBox(jsonStockItem.getString("lastBox"));
                    stockItem.setLastLoose(jsonStockItem.getString("lastLoose"));
                    stockItem.setNewPallet("0");
                    stockItem.setNewBox("0");
                    stockItem.setNewLoose("0");
                    stockItem.setNewTotal("0");
                    stockItem.setNewIssue(jsonStockItem.getString("newIssue"));
                    stockItem.setNewWarehouse(jsonStockItem.getString("newWarehouse"));
                    stockItem.setNewZone(jsonStockItem.getString("newZone"));
                    stockItem.setNewBay(jsonStockItem.getString("newBay"));
                    stockItem.setDateTimeStamp(jsonStockItem.getString("datetimestamp"));
                    stockItem.setStaffID(jsonStockItem.getString("staffid"));
                    stockItem.setXLocation(jsonStockItem.getString("xLocation"));
                    stockItem.setStockReceived(jsonStockItem.getString("stockrecieved"));

                    JSONArray jsonOtherArray = new JSONArray(jsonStockItem.getString("otherlocation"));
                    for(int l = 0; l < jsonOtherArray.length(); l++) {
                        JSONObject jsonOtherItem = jsonOtherArray.getJSONObject(l);

                        OtherLocationItem item = new OtherLocationItem();
                        item.setStockID(stockID);
                        item.setOtherID(jsonOtherItem.getString("id"));
                        item.setWarehouseID(jsonOtherItem.getString("warehouseID"));
                        item.setZoneID(jsonOtherItem.getString("zoneID"));
                        item.setBayID(jsonOtherItem.getString("bayID"));

                        otherLocationDB.addOtherLocation(item);
                    }

                    stockDB.addStock(stockItem);
                }

                bayDB.addBay(bayItem);
            }

            /*JSONArray jsonDespatchArray = new JSONArray(despatches);
            DespatchDB despatchDB = new DespatchDB(ctx);
            OutletDB outletDB = new OutletDB(ctx);
            TierDB tierDB = new TierDB(ctx);
            StockDB stockDB = new StockDB(ctx);

            if(jsonDespatchArray.length() == 0)
                return 2;

            for(int i = 0; i < jsonDespatchArray.length(); i++) {
                JSONObject jsonDespatchItem = jsonDespatchArray.getJSONObject(i);
                DespatchItem despatchItem = new DespatchItem();

                String despatchID = jsonDespatchItem.getString("despatchID");

                despatchItem.setDespatchId(despatchID);
                despatchItem.setRunId(jsonDespatchItem.getString("run"));
                despatchItem.setDriverName(jsonDespatchItem.getString("driver"));
                despatchItem.setCreationDate(jsonDespatchItem.getString("date"));
                despatchItem.setRoute(jsonDespatchItem.getString("route"));
                despatchItem.setReg(jsonDespatchItem.getString("reg"));
                despatchItem.setCompleted(StateConsts.DESPATCH_DEFAULT);

                if(!despatchDB.isExist(despatchItem)) {
                    despatchDB.addDespatch(despatchItem);
                    String despatchOutlet = jsonDespatchItem.getString("outlet");
                    JSONArray jsonOutletArray = new JSONArray(despatchOutlet);
                    for(int j = 0; j < jsonOutletArray.length(); j++) {
                        JSONObject jsonOutletItem = jsonOutletArray.getJSONObject(j);

                        OutletItem outletItem = new OutletItem();
                        String outletID = jsonOutletItem.getString("outletID");
                        outletItem.setDespatchId(despatchID);
                        outletItem.setOutletId(outletID);
                        outletItem.setOutlet(jsonOutletItem.getString("outlet"));
                        outletItem.setAddress(jsonOutletItem.getString("address"));
                        outletItem.setServiceType(jsonOutletItem.getString("service"));
                        outletItem.setDelivered(jsonOutletItem.getInt("delivered"));
                        outletItem.setDeliveredTime(jsonOutletItem.getString("deliveredtime"));
                        outletItem.setTiers(jsonOutletItem.getInt("tierstotal"));
                        outletItem.setReason(jsonOutletItem.getInt("reason"));

                        outletDB.addOutlet(outletItem);

                        String tiers = jsonOutletItem.getString("tiers");
                        JSONArray jsonTierArray = new JSONArray(tiers);

                        for(int u = 0; u < jsonTierArray.length(); u++) {
                            JSONObject jsonTierItem = jsonTierArray.getJSONObject(u);
                            TierItem tierItem = new TierItem();

                            String tierNo = jsonTierItem.getString("tier_no");

                            tierItem.setDespatchID(despatchID);
                            tierItem.setOutletID(outletID);
                            tierItem.setTierNo(tierNo);
                            tierItem.setTierspace(jsonTierItem.getString("tierspace"));
                            tierItem.setSlots(jsonTierItem.getInt("slots"));
                            tierItem.setTierOrder(jsonTierItem.getInt("tierOrder"));

                            tierDB.addTier(tierItem);

                            String stock = jsonTierItem.getString("stock");
                            JSONArray jsonStockArray = new JSONArray(stock);
                            for (int k = 0; k < jsonStockArray.length(); k++) {
                                JSONObject jsonStockItem = jsonStockArray.getJSONObject(k);
                                StockItem stockItem = new StockItem();

                                stockItem.setDespatchID(despatchID);
                                stockItem.setOutletID(outletID);
                                stockItem.setStock(jsonStockItem.getString("stock"));
                                stockItem.setTitleID(jsonStockItem.getString("titleID"));
                                stockItem.setStockId(jsonStockItem.getString("stockID"));
                                stockItem.setSize(jsonStockItem.getString("size"));
                                stockItem.setTier(tierNo);
                                stockItem.setStatus(jsonStockItem.getString("status"));
                                stockItem.setSlotOrder(jsonStockItem.getInt("slotOrder"));
                                String slot = jsonStockItem.getString("slot");
                                if (!slot.isEmpty())
                                    stockItem.setSlot(jsonStockItem.getString("slot"));
                                else
                                    stockItem.setSlot("0");
                                stockItem.setQty(StateConsts.STOCK_QTY_NONE);
                                stockItem.setRemove(jsonStockItem.getString("remove"));
                                stockItem.setRemoveID(jsonStockItem.getString("removeID"));

                                stockDB.addStock(stockItem);
                            }
                        }
                    }
                }
            }*/

            return 1;
        } catch (JSONException ex) {
            Log.v("StockID", stockID);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        EventBus.getDefault().post(new StockInfoStoreEvent(result));
    }
}