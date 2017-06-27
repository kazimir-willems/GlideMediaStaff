package delivery.com.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.consts.StateConsts;
import delivery.com.model.OutletItem;
import delivery.com.model.StaffItem;
import delivery.com.ui.OutletActivity;
import delivery.com.ui.ReasonActivity;
import delivery.com.ui.StaffActivity;
import delivery.com.ui.StockActivity;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private StaffActivity parent;
    private List<StaffItem> items = new ArrayList<>();

    public StaffAdapter(StaffActivity parent) {
        this.parent = parent;
    }

    @Override
    public StaffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StaffViewHolder holder, int position) {
        final StaffItem item = items.get(position);

        holder.tvStaff.setText(item.getStaffName());
        holder.staffLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.startZoneActivity(item);
            }
        });
    }

    public StaffItem getItem(int pos) {
        return items.get(pos);
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(StaffItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<StaffItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        @Bind(R.id.staff_layout)
        LinearLayout staffLayout;
        @Bind(R.id.tv_staff_name)
        TextView tvStaff;

        public StaffViewHolder(View view) {
            super(view);
            this.view = view;

            ButterKnife.bind(this, view);
        }
    }
}
