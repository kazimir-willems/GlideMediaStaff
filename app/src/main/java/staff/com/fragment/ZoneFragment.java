package staff.com.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import staff.com.R;
import staff.com.adapter.AdminWarehouseAdapter;
import staff.com.adapter.WarehouseAdapter;
import staff.com.db.BayDB;
import staff.com.db.StockDB;
import staff.com.db.WarehouseDB;
import staff.com.db.ZoneDB;
import staff.com.model.StaffItem;
import staff.com.model.StockItem;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;
import staff.com.ui.MainActivity;
import staff.com.ui.StockActivity;
import staff.com.ui.ZoneActivity;

public class ZoneFragment extends Fragment {

    @BindView(R.id.zone_list)
    ExpandableListView zoneList;
    @BindView(R.id.edt_title_id)
    TextInputEditText edtTitleID;

    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();
    HashMap<String, ArrayList<ZoneItem>> zoneItems = new HashMap<String, ArrayList<ZoneItem>>();

    private WarehouseAdapter adapter;
    private StaffItem staffItem;

    public static ZoneFragment newInstance() {
        ZoneFragment fragment = new ZoneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_zone, container, false);
        ButterKnife.bind(this, view);

        adapter = new WarehouseAdapter(ZoneFragment.this);
        zoneList.setAdapter(adapter);

        staffItem = ((MainActivity) getActivity()).getStaffItem();

        for(int i = 0; i < adapter.getGroupCount(); i++) {
            zoneList.expandGroup(i);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshZoneList();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

    }

    @OnClick(R.id.btn_search)
    void onClickBtnSearch() {
        String titleID = edtTitleID.getText().toString();

        StockDB stockDB = new StockDB(getActivity());
        ArrayList<StockItem> searchResults = stockDB.fetchStockByTitleID(titleID);
        if(searchResults.size() == 0) {
            Toast.makeText(getActivity(), R.string.no_stock_exists, Toast.LENGTH_SHORT).show();
        } else {
            StockItem stockItem = searchResults.get(0);
            WarehouseDB warehouseDB = new WarehouseDB(getActivity());
            WarehouseItem warehouseItem = warehouseDB.fetchWarehouseByID(stockItem.getWarehouseID()).get(0);

            ZoneDB zoneDB = new ZoneDB(getActivity());
            ZoneItem zoneItem = zoneDB.fetchZoneByZoneID(stockItem.getZoneID()).get(0);

            if(staffItem == null) {
                Toast.makeText(getActivity(), R.string.choose_staff, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getActivity(), StockActivity.class);

            intent.putExtra("warehouse", warehouseItem);
            intent.putExtra("zone", zoneItem);
            intent.putExtra("staff", staffItem);
            intent.putExtra("from_search", true);
            intent.putExtra("search_stock", stockItem);

            startActivity(intent);
        }
    }

    private void refreshZoneList() {
        WarehouseDB warehouseDB = new WarehouseDB(getActivity());
        ZoneDB zoneDB = new ZoneDB(getActivity());
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
        if(staffItem == null) {
            Toast.makeText(getActivity(), R.string.choose_staff, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), StockActivity.class);

        intent.putExtra("warehouse", warehouseItem);
        intent.putExtra("zone", zoneItem);
        intent.putExtra("staff", staffItem);

        startActivity(intent);
    }

}