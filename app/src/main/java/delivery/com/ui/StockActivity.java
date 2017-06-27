package delivery.com.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.db.BayDB;
import delivery.com.fragment.StockFragment;
import delivery.com.model.BayItem;
import delivery.com.model.StaffItem;
import delivery.com.model.WarehouseItem;
import delivery.com.model.ZoneItem;

public class StockActivity extends AppCompatActivity
{
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tv_staff_name)
    TextView tvStaff;
    @Bind(R.id.tv_warehouse)
    TextView tvWarehouse;
    @Bind(R.id.tv_zone)
    TextView tvZone;

    private StaffItem staffItem;
    private ZoneItem zoneItem;
    private WarehouseItem warehouseItem;
    private int tiers = 0;
    private StockFragment[] fragments;

    private ArrayList<BayItem> bayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        ButterKnife.bind(this);

        staffItem = (StaffItem) getIntent().getSerializableExtra("staff");
        zoneItem = (ZoneItem) getIntent().getSerializableExtra("zone");
        warehouseItem = (WarehouseItem) getIntent().getSerializableExtra("warehouse");

        tvStaff.setText(staffItem.getStaffName());
        tvWarehouse.setText(warehouseItem.getName());
        tvZone.setText(zoneItem.getZone());

        BayDB bayDB = new BayDB(StockActivity.this);

        bayList = bayDB.fetchBayByZone(zoneItem);

        fragments = new StockFragment[bayList.size()];
        for(int i = 0; i < bayList.size(); i++) {
            fragments[i] = new StockFragment().newInstance(bayList.get(i));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(bayList.size());
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments[pos];
        }

        @Override
        public int getCount() {
            return bayList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public StaffItem getStaffItem() {
        return staffItem;
    }
}