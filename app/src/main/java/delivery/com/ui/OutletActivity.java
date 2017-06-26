package delivery.com.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.adapter.OutletAdapter;
import delivery.com.db.OutletDB;
import delivery.com.model.DespatchItem;
import delivery.com.model.OutletItem;

public class OutletActivity extends AppCompatActivity {

    @Bind(R.id.outlet_list)
    RecyclerView outletList;

    @Bind(R.id.tv_despatch_id)
    TextView tvDespatchID;
    @Bind(R.id.tv_run)
    TextView tvRun;
    @Bind(R.id.tv_driver_name)
    TextView tvDriverName;
    @Bind(R.id.tv_route)
    TextView tvRoute;
    @Bind(R.id.tv_reg)
    TextView tvReg;

    private OutletAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DespatchItem despatchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        despatchItem = (DespatchItem) getIntent().getSerializableExtra("despatch");

        tvDespatchID.setText(despatchItem.getDespatchId());
        tvRun.setText(despatchItem.getRunId());
        tvDriverName.setText(despatchItem.getDriverName());
        tvRoute.setText(despatchItem.getRoute());
        tvReg.setText(despatchItem.getReg());

        outletList.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(OutletActivity.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        outletList.setLayoutManager(mLinearLayoutManager);
//        outletList.addItemDecoration(new DividerItemDecoration(OutletActivity.this, DividerItemDecoration.VERTICAL_LIST));

        adapter = new OutletAdapter(OutletActivity.this);
        outletList.setAdapter(adapter);
    }

    private void getOutlets() {
        String despatchID = despatchItem.getDespatchId();
        OutletDB db = new OutletDB(OutletActivity.this);
        ArrayList<OutletItem> outletItems = db.fetchOutletsByDespatchID(despatchID);

        adapter.addItems(outletItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        getOutlets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}