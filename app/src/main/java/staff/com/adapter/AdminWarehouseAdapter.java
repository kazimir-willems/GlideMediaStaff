package staff.com.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import staff.com.R;
import staff.com.fragment.DeleteZoneFragment;
import staff.com.model.WarehouseItem;
import staff.com.model.ZoneItem;

/**
 * Created by Kazimir on 6/20/2017.
 */

public class AdminWarehouseAdapter extends BaseExpandableListAdapter {
    private DeleteZoneFragment ctx;

    private HashMap<String, ArrayList<ZoneItem>> zoneItems = new HashMap<>();
    private ArrayList<WarehouseItem> warehouseItems = new ArrayList<>();

    public AdminWarehouseAdapter(DeleteZoneFragment parent) {
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

        final WarehouseItem warehouseItem = (WarehouseItem) getGroup(groupPosition);
        final ZoneItem zoneItem = (ZoneItem) getChild(groupPosition, childPosition);
        final String zone = zoneItem.getZone();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) ctx.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_admin_zone, null);
        }

        TextView tvZone = (TextView) convertView.findViewById(R.id.tv_zone);
        tvZone.setText(zone);
        LinearLayout zoneLayout = (LinearLayout) convertView.findViewById(R.id.zone_layout);
        zoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        Button btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.deleteZoneItem(warehouseItem.getId(), zoneItem);
            }
        });

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

    public void addItems(final ArrayList<WarehouseItem> listDataHeader,
                         final HashMap<String, ArrayList<ZoneItem>> listChildData) {
        this.zoneItems = listChildData;
        warehouseItems = listDataHeader;
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
