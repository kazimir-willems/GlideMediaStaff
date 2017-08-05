package staff.com.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import staff.com.R;
import staff.com.consts.StateConsts;
import staff.com.db.BayDB;
import staff.com.db.IssueDB;
import staff.com.db.OtherLocationDB;
import staff.com.db.WarehouseDB;
import staff.com.db.ZoneDB;
import staff.com.fragment.StockFragment;
import staff.com.model.BayItem;
import staff.com.model.IssueItem;
import staff.com.model.OtherLocationItem;
import staff.com.model.StockItem;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;
import staff.com.util.DateUtil;
import staff.com.util.StringUtil;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private StockFragment parent;
    private List<StockItem> items = new ArrayList<>();

    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();
    private ArrayList<ZoneItem> zoneItems = new ArrayList<>();
    private ArrayList<BayItem> bayItems = new ArrayList<>();
    private ArrayList<IssueItem> issueList = new ArrayList<>();

    private boolean firstZoneSelection = true;
    private boolean firstBaySelection = true;

    private String lastWarehouseID = "";
    private String lastZoneID = "";
    private String lastBayID = "";

    private BayItem newBayItem = new BayItem();

    private boolean bLoad = true;

    private int lastWarehousePos = 0;
    private int lastZonePos = 0;
    private int lastBayPos = 0;

    private int selectedWarehousePos = 0;
    private int selectedZonePos = 0;
    private int selectedBayPos = 0;

    private int newTotal = 0;

    private IssueItem currentIssueItem = new IssueItem();

    private LinearLayoutManager mLinearLayoutManager;

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

    public StockFragment getParent() {
        return parent;
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

        firstBaySelection = true;
        firstZoneSelection = true;

        final StockItem item = items.get(position);

        holder.otherList.setHasFixedSize(true);

        mLinearLayoutManager = new LinearLayoutManager(parent.getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.otherList.setLayoutManager(mLinearLayoutManager);

        OtherLocationDB otherLocationDB = new OtherLocationDB(parent.getActivity());
        ArrayList<OtherLocationItem> otherItems = otherLocationDB.fetchOtherLocation(item);

        if(otherItems.size() > 0) {
            OtherLocationAdapter otherAdapter = new OtherLocationAdapter(StockAdapter.this);
            otherAdapter.addItems(otherItems);

            holder.otherList.setAdapter(otherAdapter);
            holder.otherLayout.setVisibility(View.VISIBLE);

            otherAdapter.notifyDataSetChanged();
        } else {
            holder.otherLayout.setVisibility(View.GONE);
        }

        if(item.getNewWarehouse().isEmpty()) {
            lastWarehouseID = item.getWarehouseID();
        } else {
            lastWarehouseID = item.getNewWarehouse();
        }

        if(item.getNewZone().isEmpty()) {
            lastZoneID = item.getZoneID();
        } else {
            lastZoneID = item.getNewZone();
        }

        if(item.getNewBay().isEmpty()) {
            lastBayID = item.getBayID();
        } else {
            lastBayID = item.getNewBay();
        }

        newBayItem.setWarehouseID(lastWarehouseID);
        newBayItem.setZoneID(lastZoneID);
        newBayItem.setBayID(lastBayID);

        holder.tvTitleID.setText(item.getTitleID());
        holder.tvStockTitle.setText(item.getTitle());
        holder.tvCustomer.setText(item.getCustomer());
        holder.tvStatus.setText(item.getStatus());

        if(item.getCompleted() == StateConsts.STATE_DEFAULT) {
            holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_delete));
            holder.btnUpdate.setBackground(parent.getResources().getDrawable(R.drawable.button_not_complete));
        } else if (item.getCompleted() == StateConsts.STATE_COMPLETED) {
            holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_complete));
            holder.btnUpdate.setBackground(parent.getResources().getDrawable(R.drawable.button_update));
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

        if(!StringUtil.isEmpty(item.getStockReceived())) {
            String strStockReceived = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(item.getStockReceived()));
            holder.tvStockReceived.setText(strStockReceived);
        } else {
            holder.tvStockReceived.setText("");
        }

        if(!StringUtil.isEmpty(item.getLastStockTakeQty())) {
            String strLastTakeQty = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(item.getLastStockTakeQty()));
            holder.tvLastStockTakeQty.setText(strLastTakeQty);
        } else {
            holder.tvLastStockTakeQty.setText("");
        }

        holder.tvBoxQty.setText(item.getQtyBox());

        holder.edtPallets.setText(item.getNewPallet());
        holder.edtBoxes.setText(item.getNewBox());
        holder.edtLoose.setText(item.getNewLoose());

        if(!StringUtil.isEmpty(item.getNewTotal())) {
            String strNewTotal = NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(item.getNewTotal()));
            holder.tvNewTotal.setText(strNewTotal);
        }

        holder.warehouseSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WarehouseItem warehouseItem = warehouseItems.get(position);
                ZoneDB zoneDB = new ZoneDB(StockAdapter.this.parent.getActivity());
                zoneItems = zoneDB.fetchZoneByWarehouseID(warehouseItem.getId());
                ArrayAdapter zoneAdapter = new ArrayAdapter(StockAdapter.this.parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, zoneItems);
                holder.zoneSpin.setAdapter(zoneAdapter);

                    for(int i = 0; i < zoneItems.size(); i++) {
                        if(item.getZoneID().equals(zoneItems.get(i).getZoneID())) {
                            holder.zoneSpin.setSelection(i);
                        }
                    }

                selectedWarehousePos = position;
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

                selectedZonePos = position;

                    for(int i = 0; i < bayItems.size(); i++) {
                        if(item.getBayID().equals(bayItems.get(i).getBayID())) {
                            holder.baySpin.setSelection(i);
                        }
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.baySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBayPos = position;
                newBayItem = bayItems.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.issueSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentIssueItem = issueList.get(position);
                if(currentIssueItem.getIssueID().equals("6")) {
                    holder.edtPallets.setText("0");
                    holder.edtBoxes.setText("0");
                    holder.edtLoose.setText("0");
                    newTotal = 0;
                    holder.tvNewTotal.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.edtBoxes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.edtLoose.getText().toString().length() > 0 && holder.edtBoxes.getText().toString().length() > 0) {
                    newTotal = Integer.valueOf(holder.edtBoxes.getText().toString()) * Integer.valueOf(item.getQtyBox()) + Integer.valueOf(holder.edtLoose.getText().toString());
                    String strNewTotal = NumberFormat.getNumberInstance(Locale.US).format(newTotal);

                    holder.tvNewTotal.setText(strNewTotal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.edtLoose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.edtLoose.getText().toString().length() > 0 && holder.edtBoxes.getText().toString().length() > 0) {
                    newTotal = Integer.valueOf(holder.edtBoxes.getText().toString()) * Integer.valueOf(item.getQtyBox()) + Integer.valueOf(holder.edtLoose.getText().toString());
                    String strNewTotal = NumberFormat.getNumberInstance(Locale.US).format(newTotal);

                    holder.tvNewTotal.setText(strNewTotal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentIssueID = Integer.valueOf(currentIssueItem.getIssueID());
                switch(currentIssueID) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if(StringUtil.isEmpty(holder.edtPallets.getText().toString()) || StringUtil.isEmpty(holder.edtBoxes.getText().toString()) || StringUtil.isEmpty(holder.edtLoose.getText().toString())) {
                            Toast.makeText(parent.getActivity(), R.string.input_qtys, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        newTotal = Integer.valueOf(holder.edtBoxes.getText().toString()) * Integer.valueOf(item.getQtyBox()) + Integer.valueOf(holder.edtLoose.getText().toString());

                        if(newTotal == 0) {
                            Toast.makeText(parent.getActivity(), R.string.qty_required, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                }
                if (lastWarehousePos == selectedWarehousePos && lastZonePos == selectedZonePos && lastBayPos == selectedBayPos) {
                    if(currentIssueItem.getIssueID().equals("5")) {
                        Toast.makeText(parent.getActivity(), R.string.select_new_location, Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    if (!(newBayItem.getWarehouseID().equals(lastWarehouseID) && newBayItem.getZoneID().equals(lastZoneID) && newBayItem.getBayID().equals(lastBayID))) {
                        item.setNewWarehouse(newBayItem.getWarehouseID());
                        item.setNewZone(newBayItem.getZoneID());
                        item.setNewBay(newBayItem.getBayID());
                    } else {
                        if (currentIssueItem.getIssueID().equals("5")) {
                            Toast.makeText(parent.getActivity(), R.string.select_new_location, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                item.setNewPallet(holder.edtPallets.getText().toString());
                item.setNewBox(holder.edtBoxes.getText().toString());
                item.setNewLoose(holder.edtLoose.getText().toString());
                item.setNewIssue(currentIssueItem.getIssueID());
                item.setNewTotal(String.valueOf(newTotal));
                item.setCompleted(StateConsts.STATE_COMPLETED);
                item.setDateTimeStamp(DateUtil.getCurDateTime());

                holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_complete));
                parent.updateStockItem(item);

                holder.btnUpdate.setBackground(parent.getResources().getDrawable(R.drawable.button_update));
            }
        });

        holder.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setCompleted(StateConsts.STATE_DEFAULT);

                holder.ivStatus.setBackground(parent.getResources().getDrawable(R.drawable.ic_delete));
                parent.updateStockItem(item);

                holder.btnUpdate.setBackground(parent.getResources().getDrawable(R.drawable.button_not_complete));
            }
        });

        ArrayAdapter warehouseAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, warehouseItems);
        ArrayAdapter zoneAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, zoneItems);
        ArrayAdapter bayAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, bayItems);

        holder.warehouseSpin.setAdapter(warehouseAdapter);
        holder.zoneSpin.setAdapter(zoneAdapter);
        holder.baySpin.setAdapter(bayAdapter);

        holder.issueSpin.setSelection(Integer.valueOf(item.getNewIssue()) - 1);

        for(int i = 0; i < warehouseItems.size(); i++) {
            if(item.getWarehouseID().equals(warehouseItems.get(i).getId())) {
                holder.warehouseSpin.setSelection(i);
            }
        }




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
        @Bind(R.id.tv_last_stocktake_date)
        TextView tvLastStocktakeDate;
        @Bind(R.id.tv_last_stocktake_qty)
        TextView tvLastStockTakeQty;
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
        @Bind(R.id.tv_stock_received)
        TextView tvStockReceived;
        @Bind(R.id.iv_status)
        ImageView ivStatus;
        @Bind(R.id.other_list)
        RecyclerView otherList;
        @Bind(R.id.other_layout)
        LinearLayout otherLayout;

        public StockViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);

            bLoad = true;
            ArrayAdapter issueAdapter = new ArrayAdapter(parent.getActivity(), android.R.layout.simple_spinner_dropdown_item, issueList);
            issueSpin.setAdapter(issueAdapter);

            edtPallets.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    edtPallets.setText("");
                    return false;
                }
            });

            edtBoxes.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    edtBoxes.setText("");
                    return false;
                }
            });

            edtLoose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    edtLoose.setText("");
                    return false;
                }
            });
        }
    }
}
