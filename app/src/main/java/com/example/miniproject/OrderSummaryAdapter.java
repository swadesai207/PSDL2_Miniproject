package com.example.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private List<CartItem> cartItems;
    private HashMap<String, Integer> itemImageMap;
    private OnQuantityChangeListener onQuantityChangeListener;

    public OrderSummaryAdapter(List<CartItem> cartItems, HashMap<String, Integer> itemImageMap, OnQuantityChangeListener listener) {
        this.cartItems = cartItems;
        this.itemImageMap = itemImageMap;
        this.onQuantityChangeListener = listener;
    }

    @Override
    public OrderSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_summary, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderSummaryViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.itemName.setText(cartItem.getFoodName());
        holder.itemPrice.setText("₹" + cartItem.getFoodPrice());
        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        // Set the image based on the item name using the HashMap
        Integer imageResId = itemImageMap.get(cartItem.getFoodName());
        if (imageResId != null) {
            holder.itemImage.setImageResource(imageResId);
        }

        // Update Quantity Logic
        holder.buttonPlus.setOnClickListener(v -> {
            int currentQuantity = cartItem.getQuantity();
            cartItem.setQuantity(currentQuantity + 1);
            onQuantityChangeListener.onQuantityChanged(position, cartItem.getQuantity(), cartItem.getFoodPrice());
            notifyItemChanged(position);
        });

        holder.buttonMinus.setOnClickListener(v -> {
            int currentQuantity = cartItem.getQuantity();
            if (currentQuantity > 1) {
                cartItem.setQuantity(currentQuantity - 1);
                onQuantityChangeListener.onQuantityChanged(position, cartItem.getQuantity(), cartItem.getFoodPrice());
                notifyItemChanged(position);
            }
        });

        // Remove Button Logic
        holder.buttonRemove.setOnClickListener(v -> {
            onQuantityChangeListener.onItemRemoved(position);
            notifyItemRemoved(position); // Remove the item from the list
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int position, int newQuantity, int itemPrice);
        void onItemRemoved(int position);
    }

    // ViewHolder Class
    public static class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, quantityText;
        Button buttonPlus, buttonMinus, buttonRemove;
        ImageView itemImage;

        public OrderSummaryViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textView2);
            itemPrice = itemView.findViewById(R.id.cartitemprice);
            quantityText = itemView.findViewById(R.id.quantityText);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
            itemImage = itemView.findViewById(R.id.imageView3);
        }
    }
}
