package com.ondwela.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ondwela.smartpantry.R;
import com.ondwela.smartpantry.logic.ExpiryUtil;
import com.ondwela.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/** Shows the pantry items from the database in the Pantry list screen. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ItemHolder> {

    /** Callbacks so the screen, not the adapter, decides what edit and delete do. */
    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean highlightExpiry = true;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems, boolean highlightExpiry) {
        items.clear();
        items.addAll(newItems);
        this.highlightExpiry = highlightExpiry;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder h, int position) {
        final PantryItem item = items.get(position);
        h.name.setText(item.getName());
        h.quantity.setText(formatQuantity(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            h.expiry.setVisibility(View.GONE);
        } else {
            h.expiry.setVisibility(View.VISIBLE);
            int grey = h.itemView.getContext().getColor(R.color.text_secondary);
            int red = h.itemView.getContext().getColor(R.color.red_alert);
            int orange = h.itemView.getContext().getColor(R.color.orange_accent);
            if (highlightExpiry && ExpiryUtil.isExpired(expiry)) {
                h.expiry.setText(h.itemView.getContext().getString(R.string.expired, expiry));
                h.expiry.setTextColor(red);
            } else if (highlightExpiry && ExpiryUtil.isExpiringSoon(expiry)) {
                h.expiry.setText(h.itemView.getContext().getString(R.string.expiring_soon, expiry));
                h.expiry.setTextColor(orange);
            } else {
                h.expiry.setText(h.itemView.getContext().getString(R.string.expires_on, expiry));
                h.expiry.setTextColor(grey);
            }
        }

        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.delete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Drops the trailing ".0" so 3.0 shows as 3 but 2.5 stays 2.5. */
    static String formatQuantity(double q) {
        return q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView quantity;
        final TextView expiry;
        final ImageButton delete;

        ItemHolder(View v) {
            super(v);
            name = v.findViewById(R.id.textItemName);
            quantity = v.findViewById(R.id.textItemQuantity);
            expiry = v.findViewById(R.id.textItemExpiry);
            delete = v.findViewById(R.id.buttonDeleteItem);
        }
    }
}
