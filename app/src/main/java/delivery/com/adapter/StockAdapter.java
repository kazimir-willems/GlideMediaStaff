package delivery.com.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import delivery.com.R;
import delivery.com.consts.StateConsts;
import delivery.com.fragment.StockFragment;
import delivery.com.model.OutletItem;
import delivery.com.model.StockItem;
import delivery.com.ui.OutletActivity;
import delivery.com.ui.StockActivity;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private StockFragment parent;
    private List<StockItem> items = new ArrayList<>();

    public StockAdapter(StockFragment parent) {
        this.parent = parent;
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
            holder.totalLayout.setBackgroundColor(parent.getResources().getColor(R.color.colorWhite));
        }
        final StockItem item = items.get(position);
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

        public StockViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
