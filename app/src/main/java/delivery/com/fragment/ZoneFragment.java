package delivery.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.adapter.AdminWarehouseAdapter;
import delivery.com.db.BayDB;
import delivery.com.db.StockDB;
import delivery.com.db.WarehouseDB;
import delivery.com.db.ZoneDB;
import delivery.com.model.WarehouseItem;
import delivery.com.model.ZoneItem;

public class ZoneFragment extends Fragment {

    @Bind(R.id.zone_list)
    ExpandableListView zoneList;
    @Bind(R.id.whole_layout)
    CoordinatorLayout wholeLayout;

    private AdminWarehouseAdapter adapter;

    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();
    HashMap<String, ArrayList<ZoneItem>> zoneItems = new HashMap<String, ArrayList<ZoneItem>>();

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
        View view = inflater.inflate(R.layout.fragment_zone, container, false);
        ButterKnife.bind(this, view);

        adapter = new AdminWarehouseAdapter(ZoneFragment.this);
        zoneList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshZoneList();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

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

    public void deleteZoneItem(String warehouseID, ZoneItem zoneItem) {
        ZoneDB zoneDB = new ZoneDB(getActivity());
        BayDB bayDB = new BayDB(getActivity());
        StockDB stockDB = new StockDB(getActivity());

        stockDB.removeDatasByZoneID(zoneItem.getWarehouseID(), zoneItem.getZoneID());
        bayDB.removeBayByZone(zoneItem);
        zoneDB.removeZone(warehouseID, zoneItem);

        refreshZoneList();
    }
}