package staff.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import staff.com.R;
import staff.com.adapter.StaffAdapter;
import staff.com.db.StaffDB;
import staff.com.model.StaffItem;
import staff.com.ui.MainActivity;

public class StaffFragment extends Fragment {

    private ArrayList<StaffItem> staffItems = new ArrayList<>();

    @BindView(R.id.staff_list)
    RecyclerView staffList;

    private LinearLayoutManager mLinearLayoutManager;
    private StaffAdapter adapter;

    public static StaffFragment newInstance() {
        StaffFragment fragment = new StaffFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_staff, container, false);
        ButterKnife.bind(this, view);

        staffList.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        staffList.setLayoutManager(mLinearLayoutManager);

        adapter = new StaffAdapter(StaffFragment.this);
        staffList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshStaff();
    }

    public void startMainActivity(StaffItem staffItem) {
        ((MainActivity) getActivity()).setStaffItem(staffItem);
        ((MainActivity) getActivity()).moveToZone();
    }


    private void refreshStaff() {
        StaffDB staffDB = new StaffDB(getActivity());
        staffItems = staffDB.fetchAllStaff();
        adapter.addItems(staffItems);

        adapter.notifyDataSetChanged();
    }

}