package com.example.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private List<CartItem> cartItems; // List to store cart items
    private OnQuantityChangeListener onQuantityChangeListener; // Listener for quantity changes and item removal

    // Interface for quantity changes and item removal
    public interface OnQuantityChangeListener {
        void onQuantityChanged(int position, int newQuantity, int itemPrice);
        void onItemRemoved(int position);
    }

    // Constructor for adapter
    public OrderSummaryAdapter(List<CartItem> cartItems, OnQuantityChangeListener onQuantityChangeListener) {
        this.cartItems = cartItems;
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @Override
    public OrderSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_summary, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderSummaryViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position); // Get cart item at current position

        // Set item details to the views
        holder.foodName.setText(cartItem.getFoodName());
        holder.foodPrice.setText("₹" + cartItem.getFoodPrice());
        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        // Set onClickListener for the "+" button to increase quantity
        holder.buttonPlus.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1; // Increase quantity
            cartItem.setQuantity(newQuantity); // Update cart item
            holder.quantityText.setText(String.valueOf(newQuantity)); // Update UI
            onQuantityChangeListener.onQuantityChanged(position, newQuantity, cartItem.getFoodPrice()); // Notify activity
        });

        // Set onClickListener for the "-" button to decrease quantity
        holder.buttonMinus.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1; // Decrease quantity
                cartItem.setQuantity(newQuantity); // Update cart item
                holder.quantityText.setText(String.valueOf(newQuantity)); // Update UI
                onQuantityChangeListener.onQuantityChanged(position, newQuantity, cartItem.getFoodPrice()); // Notify activity
            }
        });

        // Set onClickListener for the "Remove" button to remove item from cart
        holder.buttonRemove.setOnClickListener(v -> {
            onQuantityChangeListener.onItemRemoved(position); // Notify activity to remove item
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size(); // Return total number of items in the cart
    }

    // ViewHolder class to represent each item in the RecyclerView
    public class OrderSummaryViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodPrice, quantityText;
        Button buttonPlus, buttonMinus, buttonRemove;

        public OrderSummaryViewHolder(View itemView) {
            super(itemView);
            // Initialize the views
            foodName = itemView.findViewById(R.id.textView2);
            foodPrice = itemView.findViewById(R.id.cartitemprice);
            quantityText = itemView.findViewById(R.id.quantityText);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
