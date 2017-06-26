package delivery.com.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.adapter.StockAdapter;
import delivery.com.db.StockDB;
import delivery.com.model.BayItem;
import delivery.com.model.StockItem;
import delivery.com.ui.DividerItemDecoration;

/**
 * Created by Caesar on 4/25/2017.
 */

public class StockFragment extends Fragment {

    @Bind(R.id.stock_list)
    RecyclerView stockList;

    private String tier;
    private String tierspace = "0";
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
        stockList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        adapter = new StockAdapter(StockFragment.this);
        stockList.setAdapter(adapter);

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
}