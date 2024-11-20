package com.example.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {

    private List<CartItem> cartItems;
    private OnItemDeletedListener onItemDeletedListener;

    public interface OnItemDeletedListener {
        void onItemDeleted(int position, int itemPrice);
    }

    public OrderSummaryAdapter(List<CartItem> cartItems, OnItemDeletedListener onItemDeletedListener) {
        this.cartItems = cartItems;
        this.onItemDeletedListener = onItemDeletedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.foodName.setText(item.getFoodName());
        holder.foodPrice.setText("Price: " + item.getFoodPrice());
        holder.foodQuantity.setText("Quantity: " + item.getQuantity());

        holder.deleteButton.setOnClickListener(v -> {
            // Remove the item from the list and notify the adapter
            cartItems.remove(position);
            onItemDeletedListener.onItemDeleted(position, item.getFoodPrice() * item.getQuantity());
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodQuantity;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
