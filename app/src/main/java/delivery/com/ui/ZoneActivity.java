package delivery.com.ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import delivery.com.R;
import delivery.com.adapter.AdminWarehouseAdapter;
import delivery.com.adapter.WarehouseAdapter;
import delivery.com.db.StockDB;
import delivery.com.db.WarehouseDB;
import delivery.com.db.ZoneDB;
import delivery.com.fragment.ZoneFragment;
import delivery.com.model.StaffItem;
import delivery.com.model.StockItem;
import delivery.com.model.WarehouseItem;
import delivery.com.model.ZoneItem;

public class ZoneActivity extends AppCompatActivity {
    @Bind(R.id.zone_list)
    ExpandableListView zoneList;
    @Bind(R.id.edt_title_id)
    TextInputEditText edtTitleID;

    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();
    HashMap<String, ArrayList<ZoneItem>> zoneItems = new HashMap<String, ArrayList<ZoneItem>>();

    private WarehouseAdapter adapter;
    private StaffItem staffItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        ButterKnife.bind(this);

        adapter = new WarehouseAdapter(ZoneActivity.this);
        zoneList.setAdapter(adapter);

        staffItem = (StaffItem) getIntent().getSerializableExtra("staff");

        for(int i = 0; i < adapter.getGroupCount(); i++) {
            zoneList.expandGroup(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshZoneList();
    }

    @OnClick(R.id.btn_search)
    void onClickBtnSearch() {
        String titleID = edtTitleID.getText().toString();

        StockDB stockDB = new StockDB(ZoneActivity.this);
        ArrayList<StockItem> searchResults = stockDB.fetchStockByTitleID(titleID);
        if(searchResults.size() == 0) {
            Toast.makeText(ZoneActivity.this, R.string.no_stock_exists, Toast.LENGTH_SHORT).show();
        } else {
            StockItem stockItem = searchResults.get(0);
            WarehouseDB warehouseDB = new WarehouseDB(ZoneActivity.this);
            WarehouseItem warehouseItem = warehouseDB.fetchWarehouseByID(stockItem.getWarehouseID()).get(0);

            ZoneDB zoneDB = new ZoneDB(ZoneActivity.this);
            ZoneItem zoneItem = zoneDB.fetchZoneByZoneID(stockItem.getZoneID()).get(0);

            Intent intent = new Intent(ZoneActivity.this, StockActivity.class);

            intent.putExtra("warehouse", warehouseItem);
            intent.putExtra("zone", zoneItem);
            intent.putExtra("staff", staffItem);
            intent.putExtra("from_search", true);
            intent.putExtra("search_stock", stockItem);

            startActivity(intent);
        }
    }

    private void refreshZoneList() {
        WarehouseDB warehouseDB = new WarehouseDB(ZoneActivity.this);
        ZoneDB zoneDB = new ZoneDB(ZoneActivity.this);
        warehouseItems = warehouseDB.fetchAllWarehouse();
        zoneItems.clear();
        for(int i = 0; i < warehouseItems.size(); i++) {
            ArrayList<ZoneItem> items = zoneDB.fetchZoneByWarehouseID(warehouseItems.get(i).getId());
            zoneItems.put(warehouseItems.get(i).getId(), items);
        }

        adapter.addItems(warehouseItems, zoneItems);
        adapter.notifyDataSetChanged();

        for(int i = 0; i < adapter.getGroupCount(); i++) {
            zoneList.expandGroup(i);
        }
    }

    public void startStockActivity(ZoneItem zoneItem, WarehouseItem warehouseItem) {
        Intent intent = new Intent(ZoneActivity.this, StockActivity.class);

        intent.putExtra("warehouse", warehouseItem);
        intent.putExtra("zone", zoneItem);
        intent.putExtra("staff", staffItem);

        startActivity(intent);
    }
}
