package com.example.miniproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class OrderSummaryActivity extends AppCompatActivity {

    private RecyclerView orderItemsRecyclerView;
    private OrderSummaryAdapter orderSummaryAdapter;
    private TextView tvTotalBill;
    private List<CartItem> cartItems = new ArrayList<>();
    private int totalBill = 0;

    // Create a HashMap to map item names to image resources
    private HashMap<String, Integer> itemImageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        // Initialize RecyclerView and TextView for total bill
        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        tvTotalBill = findViewById(R.id.tvTotalBill);

        // Initialize buttons for navigation and order confirmation
        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        // Initialize the item-to-image map
        itemImageMap = new HashMap<>();
        itemImageMap.put("Aloo Pattice", R.drawable.pattice);  // Assuming the image name is aloo_pattice.png
        itemImageMap.put("Thick Coffee", R.drawable.coldcoffee);
        itemImageMap.put("Sandwich", R.drawable.chocolatesandwich);
        itemImageMap.put("Fresh Juice", R.drawable.watermelonjuice);
        itemImageMap.put("Garam Chai", R.drawable.chai);

        // Get cart data from the intent
        Intent intent = getIntent();
        ArrayList<String> cartItemNames = intent.getStringArrayListExtra("cartItems");
        ArrayList<Integer> cartItemQuantities = intent.getIntegerArrayListExtra("cartQuantities");

        // Convert to CartItem objects and calculate the total bill
        for (int i = 0; i < cartItemNames.size(); i++) {
            String itemName = cartItemNames.get(i);
            int quantity = cartItemQuantities.get(i);
            int itemPrice = getItemPrice(itemName);
            cartItems.add(new CartItem(itemName, itemPrice, quantity));
            totalBill += itemPrice * quantity;
        }

        // Update the total bill display
        tvTotalBill.setText("Total Bill: ₹" + totalBill);

        // Set up RecyclerView with OrderSummaryAdapter
        orderSummaryAdapter = new OrderSummaryAdapter(cartItems, itemImageMap, new OrderSummaryAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int position, int newQuantity, int itemPrice) {
                // Update quantity and recalculate total bill
                CartItem updatedItem = cartItems.get(position);
                updatedItem.setQuantity(newQuantity);
                totalBill = recalculateTotalBill();
                tvTotalBill.setText("Total Bill: ₹" + totalBill);
            }

            @Override
            public void onItemRemoved(int position) {
                // Remove the item from the list and update total bill
                cartItems.remove(position);
                totalBill = recalculateTotalBill();
                tvTotalBill.setText("Total Bill: ₹" + totalBill);

                // If no items left, show message and go back to menu
                if (cartItems.isEmpty()) {
                    Toast.makeText(OrderSummaryActivity.this, "No items in cart. Redirecting to menu...", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the menu
                }
            }
        });

        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.setAdapter(orderSummaryAdapter);

        // Back to menu button
        btnBackToMenu.setOnClickListener(v -> finish());

        // Confirm order button
        btnConfirm.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                saveOrderToDatabase();
            } else {
                Toast.makeText(this, "No items to confirm.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to get item price based on its name
    private int getItemPrice(String itemName) {
        switch (itemName) {
            case "Aloo Pattice": return 18;
            case "Thick Coffee": return 30;
            case "Sandwich": return 40;
            case "Fresh Juice": return 30;
            case "Garam Chai": return 12;
            default: return 0;
        }
    }

    // Method to recalculate the total bill after quantity change or item removal
    private int recalculateTotalBill() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getFoodPrice() * item.getQuantity();
        }
        return total;
    }

    // Method to save the order to the database
    private void saveOrderToDatabase() {
        StringBuilder orderDetails = new StringBuilder();
        for (CartItem item : cartItems) {
            orderDetails.append(item.getFoodName())
                    .append(" x").append(item.getQuantity())
                    .append(", ");
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertOrder(orderDetails.toString(), totalBill, timestamp);

        Toast.makeText(this, "Ordered successfully!", Toast.LENGTH_SHORT).show();
        cartItems.clear(); // Clear the cart after successful order
        finish(); // Go back to the menu
    }
}
