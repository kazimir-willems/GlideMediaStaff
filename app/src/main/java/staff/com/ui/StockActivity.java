package staff.com.ui;

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
import staff.com.R;
import staff.com.db.BayDB;
import staff.com.fragment.StockFragment;
import staff.com.model.BayItem;
import staff.com.model.StaffItem;
import staff.com.model.StockItem;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;

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

        StockItem searchItem = (StockItem) getIntent().getSerializableExtra("search_stock");

        fragments = new StockFragment[bayList.size()];
        for(int i = 0; i < bayList.size(); i++) {
            fragments[i] = new StockFragment().newInstance(bayList.get(i), searchItem);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(bayList.size());

        if(getIntent().getBooleanExtra("from_search", false)) {
            for(int i = 0; i < bayList.size(); i++) {
                if(bayList.get(i).getBayID().equals(searchItem.getBayID())) {
                    viewPager.setCurrentItem(i);
                }
            }
        }
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

    public ZoneItem getZoneItem() {
        return zoneItem;
    }
}