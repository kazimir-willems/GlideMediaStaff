package delivery.com.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.consts.StateConsts;
import delivery.com.db.BayDB;
import delivery.com.db.IssueDB;
import delivery.com.db.StockDB;
import delivery.com.db.WarehouseDB;
import delivery.com.db.ZoneDB;
import delivery.com.fragment.StockFragment;
import delivery.com.model.BayItem;
import delivery.com.model.IssueItem;
import delivery.com.model.OutletItem;
import delivery.com.model.StockItem;
import delivery.com.model.WarehouseItem;
import delivery.com.model.ZoneItem;
import delivery.com.ui.OutletActivity;
import delivery.com.ui.StockActivity;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private StockFragment parent;
    private List<StockItem> items = new ArrayList<>();

    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();
    private ArrayList<ZoneItem> zoneItems = new ArrayList<>();
    private ArrayList<BayItem> bayItems = new ArrayList<>();
    private ArrayList<IssueItem> issueList = new ArrayList<>();

    private WarehouseItem newWarehouse = new WarehouseItem();
    private ZoneItem newZone = new ZoneItem();
    private BayItem newBay = new BayItem();

    public StockAdapter(StockFragment parent) {
        this.parent = parent;

        WarehouseDB warehouseDB = new WarehouseDB(parent.getActivity());
        ZoneDB zoneDB = new ZoneDB(parent.getActivity());
        BayDB bayDB = new BayDB(parent.getActivity());
        IssueDB issueDB = new IssueDB(parent.getActivity());

        warehouseItems = warehouseDB.fetchAllWarehouse();
        zoneItems = zoneDB.fetchZoneByWarehouseID(warehouseItems.get(0).getId());
        bayItems = bayDB.fetchBayByZone(zoneItems.get(0));
        issueList = issueDB.fetchAllIssue();
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StockViewHolder holder, int position) {
        if(position % 2 == 0) {
            holder.totalLayout.setBackgroundColor(parent.getResources().getColor(R.color.colorLightGray));
        } else {
            holder.totalLayout.setBackgroundColor(parent.getResources().getColor(R.color.colorBackGray));
        }

        final StockItem item = items.get(position);

        holder.tvTitleID.setText(item.getTitleID());
        holder.tvStockTitle.setText(item.getTitle());
        holder.tvCustomer.setText(item.getCustomer());
        holder.tvStatus.setText(item.getStatus());

        if(item.getCompleted() == StateConsts.STATE_DEFAULT) {
            holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_delete));
        } else if (item.getCompleted() == StateConsts.STATE_COMPLETED) {
            holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_complete));
        }

        if(item.getStatus().equals("In Stock")) {
            holder.tvStatus.setTextColor(parent.getResources().getColor(R.color.colorGreen));
        } else if (item.getStatus().equals("Out of Stock")) {
            holder.tvStatus.setTextColor(parent.getResources().getColor(R.color.colorRemove));
        }

        holder.tvLastPallets.setText(item.getLastPallet());
        holder.tvLastBoxes.setText(item.getLastBox());
        holder.tvLastLoose.setText(item.getLastLoose());
        holder.tvEstQty.setText(item.getQtyEstimate());
        holder.tvLastStocktakeDate.setText(item.getLastStockTakeDate());
        holder.tvBoxQty.setText(item.getQtyBox());

        holder.warehouseSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WarehouseItem warehouseItem = warehouseItems.get(position);
                ZoneDB zoneDB = new ZoneDB(StockAdapter.this.parent.getActivity());
                zoneItems = zoneDB.fetchZoneByWarehouseID(warehouseItem.getId());
                ArrayAdapter zoneAdapter = new ArrayAdapter(StockAdapter.this.parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, zoneItems);
                holder.zoneSpin.setAdapter(zoneAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.zoneSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ZoneItem zoneItem = zoneItems.get(position);
                BayDB bayDB = new BayDB(StockAdapter.this.parent.getActivity());
                bayItems = bayDB.fetchBayByZone(zoneItem);
                ArrayAdapter bayAdapter = new ArrayAdapter(StockAdapter.this.parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, bayItems);
                holder.baySpin.setAdapter(bayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setCompleted(StateConsts.STATE_COMPLETED);

                holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_complete));
                parent.updateStockItem(item);
            }
        });

        holder.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setCompleted(StateConsts.STATE_DEFAULT);

                holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_delete));
                parent.updateStockItem(item);
            }
        });

    }

    public StockItem getItem(int pos) {
        return items.get(pos);
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(StockItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<StockItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        @Bind(R.id.total_layout)
        LinearLayout totalLayout;
        @Bind(R.id.tv_title_id)
        TextView tvTitleID;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_title_stock)
        TextView tvStockTitle;
        @Bind(R.id.tv_customer)
        TextView tvCustomer;
        @Bind(R.id.tv_last_pallets)
        TextView tvLastPallets;
        @Bind(R.id.tv_last_boxes)
        TextView tvLastBoxes;
        @Bind(R.id.tv_last_loose)
        TextView tvLastLoose;
        @Bind(R.id.edt_pallets)
        EditText edtPallets;
        @Bind(R.id.edt_boxes)
        EditText edtBoxes;
        @Bind(R.id.edt_loose)
        EditText edtLoose;
        @Bind(R.id.tv_est_qty)
        TextView tvEstQty;
        @Bind(R.id.tv_last_stocktake)
        TextView tvLastStocktakeDate;
        @Bind(R.id.tv_box_qty)
        TextView tvBoxQty;
        @Bind(R.id.tv_new_total)
        TextView tvNewTotal;
        @Bind(R.id.warehouse_spinner)
        Spinner warehouseSpin;
        @Bind(R.id.zone_spinner)
        Spinner zoneSpin;
        @Bind(R.id.bay_spinner)
        Spinner baySpin;
        @Bind(R.id.issue_spinner)
        Spinner issueSpin;
        @Bind(R.id.btn_update)
        Button btnUpdate;
        @Bind(R.id.iv_status)
        ImageView ivStatus;

        public StockViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);

            ArrayAdapter warehouseAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, warehouseItems);
            ArrayAdapter zoneAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, zoneItems);
            ArrayAdapter bayAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, bayItems);
            ArrayAdapter issueAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, issueList);
            issueSpin.setAdapter(issueAdapter);
            warehouseSpin.setAdapter(warehouseAdapter);
            zoneSpin.setAdapter(zoneAdapter);
            baySpin.setAdapter(bayAdapter);
        }
    }
}
