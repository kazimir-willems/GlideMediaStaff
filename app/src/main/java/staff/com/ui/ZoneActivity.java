package staff.com.ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import staff.com.R;
import staff.com.adapter.WarehouseAdapter;
import staff.com.db.StockDB;
import staff.com.db.WarehouseDB;
import staff.com.db.ZoneDB;
import staff.com.model.StaffItem;
import staff.com.model.StockItem;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;

public class ZoneActivity extends AppCompatActivity {
    @BindView(R.id.zone_list)
    ExpandableListView zoneList;
    @BindView(R.id.edt_title_id)
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

//        adapter = new WarehouseAdapter(ZoneActivity.this);
        zoneList.setAdapter(adapter);

        staffItem = (StaffItem) getIntent().getSerializableExtra("staff");

        for(int i = 0; i < adapter.getGroupCount(); i++) {
            zoneList.expandGroup(i);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshZoneList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
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
