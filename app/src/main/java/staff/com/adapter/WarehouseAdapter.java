package staff.com.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import staff.com.R;
import staff.com.consts.StateConsts;
import staff.com.fragment.ZoneFragment;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;
import staff.com.ui.ZoneActivity;

/**
 * Created by Kazimir on 6/20/2017.
 */

public class WarehouseAdapter extends BaseExpandableListAdapter {
    private ZoneFragment ctx;

    private HashMap<String, ArrayList<ZoneItem>> zoneItems = new HashMap<>();
    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();

    public WarehouseAdapter(ZoneFragment parent) {
        this.ctx = parent;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.zoneItems.get(this.warehouseItems.get(groupPosition).getId())
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

        final ZoneItem item = (ZoneItem) getChild(groupPosition, childPosition);
        final WarehouseItem warehouseItem = (WarehouseItem) getGroup(groupPosition);
        final String zone = item.getZone();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) ctx.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_zone, null);
        }

        TextView tvZone = (TextView) convertView.findViewById(R.id.tv_zone);
        tvZone.setText(zone);
        LinearLayout zoneLayout = (LinearLayout) convertView.findViewById(R.id.zone_layout);
        zoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ctx.startStockActivity(item, warehouseItem);
            }
        });

        ImageView ivStatus = (ImageView) convertView.findViewById(R.id.iv_status);
        if(item.getCompleted() == StateConsts.STATE_COMPLETED)
            ivStatus.setBackground(ctx.getResources().getDrawable(R.drawable.ic_complete));
        else
            ivStatus.setBackground(ctx.getResources().getDrawable(R.drawable.ic_delete));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.zoneItems.get(this.warehouseItems.get(groupPosition).getId())
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.warehouseItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.warehouseItems.size();
    }

    public void addItems(final ArrayList<WarehouseItem> listDataHeader,
                         final HashMap<String, ArrayList<ZoneItem>> listChildData) {
        this.zoneItems = listChildData;
        warehouseItems = listDataHeader;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = ((WarehouseItem) getGroup(groupPosition)).getAddress();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_warehouse, null);
        }

        TextView tvWarehouse = (TextView) convertView
                .findViewById(R.id.tv_warehouse);
        tvWarehouse.setTypeface(null, Typeface.BOLD);
        tvWarehouse.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
