package delivery.com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.adapter.StockAdapter;
import delivery.com.db.StockDB;
import delivery.com.model.BayItem;
import delivery.com.model.StaffItem;
import delivery.com.model.StockItem;
import delivery.com.ui.DividerItemDecoration;
import delivery.com.ui.StockActivity;

/**
 * Created by Caesar on 4/25/2017.
 */

public class StockFragment extends Fragment {

    @Bind(R.id.stock_list)
    RecyclerView stockList;
    @Bind(R.id.tv_bay)
    TextView tvBay;

    private BayItem bayItem;

    private LinearLayoutManager mLinearLayoutManager;
    private StockAdapter adapter;

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

        return v;
    }

    public static StockFragment newInstance(BayItem item) {
        StockFragment f = new StockFragment();
        f.bayItem = item;
        return f;
    }

    private void getStocks() {
        StockDB db = new StockDB(getActivity());
        ArrayList<StockItem> items = db.fetchStocksByBay(bayItem);

        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    public void updateStockItem(StockItem item) {
        StaffItem staffItem = ((StockActivity) getActivity()).getStaffItem();
        item.setStaffID(staffItem.getStaffID());

        Toast.makeText(getActivity(), staffItem.getStaffName(), Toast.LENGTH_SHORT).show();

        StockDB stockDB = new StockDB(getActivity());
        stockDB.updateStock(item);
    }
}