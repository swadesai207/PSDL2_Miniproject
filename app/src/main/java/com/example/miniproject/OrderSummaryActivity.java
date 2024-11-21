package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {

    private RecyclerView orderItemsRecyclerView;
    private OrderSummaryAdapter orderSummaryAdapter;
    private TextView tvTotalBill;
    private List<CartItem> cartItems = new ArrayList<>();
    private int totalBill = 0;

    // Map item names to image resources
    private HashMap<String, Integer> itemImageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        // Initialize RecyclerView and total bill TextView
        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        tvTotalBill = findViewById(R.id.tvTotalBill);

        // Initialize navigation buttons
        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        // Initialize item-to-image map
        itemImageMap = new HashMap<>();
        itemImageMap.put("Aloo Pattice", R.drawable.pattice);
        itemImageMap.put("Thick Coffee", R.drawable.coldcoffee);
        itemImageMap.put("Sandwich", R.drawable.chocolatesandwich);
        itemImageMap.put("Fresh Juice", R.drawable.watermelonjuice);
        itemImageMap.put("Garam Chai", R.drawable.chai);

        // Get cart data from the Intent
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

        // Update total bill display
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
                // Remove item and update the total bill
                cartItems.remove(position);
                totalBill = recalculateTotalBill();
                tvTotalBill.setText("Total Bill: ₹" + totalBill);

                // If no items left, show a message and return to menu
                if (cartItems.isEmpty()) {
                    Toast.makeText(OrderSummaryActivity.this, "No items in cart. Redirecting to menu...", Toast.LENGTH_SHORT).show();
                    finish();
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
                // Redirect to menu after order confirmation
                Intent intentToMenu = new Intent(OrderSummaryActivity.this, MenuActivity.class);
                intentToMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentToMenu);
                finish();
            } else {
                Toast.makeText(this, "No items to confirm.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to get item price based on name
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

    // Recalculate total bill after quantity change or item removal
    private int recalculateTotalBill() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getFoodPrice() * item.getQuantity();
        }
        return total;
    }

    // Save order to database
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
        cartItems.clear();
    }
}
