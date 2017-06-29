package staff.com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import staff.com.R;
import staff.com.adapter.StockAdapter;
import staff.com.consts.StateConsts;
import staff.com.db.BayDB;
import staff.com.db.StockDB;
import staff.com.db.ZoneDB;
import staff.com.model.BayItem;
import staff.com.model.StaffItem;
import staff.com.model.StockItem;
import staff.com.model.ZoneItem;
import staff.com.ui.StockActivity;

/**
 * Created by Caesar on 4/25/2017.
 */

public class StockFragment extends Fragment {

    @Bind(R.id.stock_list)
    RecyclerView stockList;
    @Bind(R.id.tv_bay)
    TextView tvBay;

    private BayItem bayItem;
    private StockItem searchItem;

    private LinearLayoutManager mLinearLayoutManager;
    private StockAdapter adapter;

    ArrayList<StockItem> items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stock, container, false);

        ButterKnife.bind(StockFragment.this, v);

        stockList.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stockList.setLayoutManager(mLinearLayoutManager);
//        stockList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        adapter = new StockAdapter(StockFragment.this);
        stockList.setAdapter(adapter);

        tvBay.setText(bayItem.getBay());

        getStocks();

        setListPos(searchItem);

        return v;
    }

    public static StockFragment newInstance(BayItem item, StockItem searchItem) {
        StockFragment f = new StockFragment();
        f.bayItem = item;
        f.searchItem = searchItem;
        return f;
    }

    private void getStocks() {
        StockDB db = new StockDB(getActivity());
        items = db.fetchStocksByBay(bayItem);

        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    public void updateStockItem(StockItem item) {
        StaffItem staffItem = ((StockActivity) getActivity()).getStaffItem();
        item.setStaffID(staffItem.getStaffID());

        StockDB stockDB = new StockDB(getActivity());
        stockDB.updateStock(item);

        if(stockDB.getAllCount(bayItem) == stockDB.getCompletedCount(bayItem)) {
            bayItem.setCompleted(StateConsts.STATE_COMPLETED);
            BayDB bayDB = new BayDB(getActivity());
            bayDB.updateBay(bayItem);

            ZoneItem zoneItem = ((StockActivity) getActivity()).getZoneItem();
            if(bayDB.getAllCount(zoneItem) == bayDB.getCompletedCount(zoneItem)) {
                zoneItem.setCompleted(StateConsts.STATE_COMPLETED);

                ZoneDB zoneDB = new ZoneDB(getActivity());
                zoneDB.updateZone(zoneItem);
            }
        } else {
            bayItem.setCompleted(StateConsts.STATE_DEFAULT);
            BayDB bayDB = new BayDB(getActivity());
            bayDB.updateBay(bayItem);

            ZoneItem zoneItem = ((StockActivity) getActivity()).getZoneItem();
            zoneItem.setCompleted(StateConsts.STATE_DEFAULT);
            ZoneDB zoneDB = new ZoneDB(getActivity());
            zoneDB.updateZone(zoneItem);
        }

        //Check whether bay is completed also zone is completed

    }

    public void setListPos(StockItem stockItem) {
        if(stockItem != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getTitleID().equals(stockItem.getTitleID()))
                    stockList.scrollToPosition(i);
            }
        }
    }
}