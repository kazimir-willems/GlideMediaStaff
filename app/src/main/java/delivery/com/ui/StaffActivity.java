package delivery.com.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.adapter.StaffAdapter;
import delivery.com.db.StaffDB;
import delivery.com.model.StaffItem;

public class StaffActivity extends AppCompatActivity
{
    private ArrayList<StaffItem> staffItems = new ArrayList<>();

    @Bind(R.id.staff_list)
    RecyclerView staffList;

    private LinearLayoutManager mLinearLayoutManager;
    private StaffAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        ButterKnife.bind(this);
        staffList.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(StaffActivity.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        staffList.setLayoutManager(mLinearLayoutManager);

        adapter = new StaffAdapter(StaffActivity.this);
        staffList.setAdapter(adapter);

        refreshStaff();
    }

    public void startZoneActivity(StaffItem staffItem) {
        Intent intent = new Intent(StaffActivity.this, ZoneActivity.class);
        intent.putExtra("staff", staffItem);

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshStaff();
    }

    private void refreshStaff() {
        StaffDB staffDB = new StaffDB(StaffActivity.this);
        staffItems = staffDB.fetchAllStaff();
        adapter.addItems(staffItems);

        adapter.notifyDataSetChanged();
    }
}