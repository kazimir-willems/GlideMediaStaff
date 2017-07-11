package staff.com.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import staff.com.R;
import staff.com.db.BayDB;
import staff.com.db.StockDB;
import staff.com.db.WarehouseDB;
import staff.com.db.ZoneDB;
import staff.com.fragment.StaffFragment;
import staff.com.model.BayItem;
import staff.com.model.OtherLocationItem;
import staff.com.model.StaffItem;
import staff.com.model.StockItem;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;
import staff.com.ui.MainActivity;
import staff.com.ui.StockActivity;

public class OtherLocationAdapter extends RecyclerView.Adapter<OtherLocationAdapter.OtherLocationViewHolder> {

    private StockAdapter parent;
    private List<OtherLocationItem> items = new ArrayList<>();

    public OtherLocationAdapter(StockAdapter parent) {
        this.parent = parent;
    }

    @Override
    public OtherLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_other, parent, false);
        return new OtherLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OtherLocationViewHolder holder, int position) {
        final OtherLocationItem item = items.get(position);

        WarehouseDB warehouseDB = new WarehouseDB(parent.getParent().getActivity());
        ZoneDB zoneDB = new ZoneDB(parent.getParent().getActivity());
        BayDB bayDB = new BayDB(parent.getParent().getActivity());

        final WarehouseItem warehouseItem = warehouseDB.fetchWarehouseByID(item.getWarehouseID()).get(0);
        final ZoneItem zoneItem = zoneDB.fetchZoneByWarehouseAndZoneID(item.getWarehouseID(), item.getZoneID()).get(0);
        BayItem bayItem = bayDB.fetchBayByBayID(item.getBayID()).get(0);

        StockDB stockDB = new StockDB(parent.getParent().getActivity());

        final StockItem stockItem = stockDB.fetchStockByStockID(item.getOtherID()).get(0);

        String strLocation = warehouseItem.getName() + " " + zoneItem.getZone() + " " + bayItem.getBay();

        SpannableString content = new SpannableString(strLocation);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        holder.tvOtherLocation.setText(content);
        holder.tvOtherLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent.getParent().getActivity(), StockActivity.class);

                intent.putExtra("warehouse", warehouseItem);
                intent.putExtra("zone", zoneItem);
                intent.putExtra("staff", ((StockActivity) parent.getParent().getActivity()).getStaffItem());
                intent.putExtra("from_search", true);
                intent.putExtra("search_stock", stockItem);

                parent.getParent().getActivity().startActivity(intent);
                parent.getParent().getActivity().finish();
            }
        });
    }

    public OtherLocationItem getItem(int pos) {
        return items.get(pos);
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(OtherLocationItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<OtherLocationItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OtherLocationViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        @Bind(R.id.tv_other_location)
        TextView tvOtherLocation;

        public OtherLocationViewHolder(View view) {
            super(view);
            this.view = view;

            ButterKnife.bind(this, view);
        }
    }
}
