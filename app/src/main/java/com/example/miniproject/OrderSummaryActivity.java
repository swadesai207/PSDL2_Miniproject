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
import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {

    private RecyclerView orderItemsRecyclerView;
    private OrderSummaryAdapter orderSummaryAdapter;
    private TextView tvTotalBill;
    private List<CartItem> cartItems = new ArrayList<>();
    private int totalBill = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);
        tvTotalBill = findViewById(R.id.tvTotalBill);

        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);
        Button btnConfirm = findViewById(R.id.btnconfirm);

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

        // Set up RecyclerView
        orderSummaryAdapter = new OrderSummaryAdapter(cartItems, (position, itemPrice) -> {
            totalBill -= itemPrice;
            tvTotalBill.setText("Total Bill: ₹" + totalBill);
        });

        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.setAdapter(orderSummaryAdapter);

        // Update the total bill
        tvTotalBill.setText("Total Bill: ₹" + totalBill);

        // Back to menu button
        btnBackToMenu.setOnClickListener(v -> finish());

        // Confirm order
        btnConfirm.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                saveOrderToDatabase();
            } else {
                Toast.makeText(this, "No items to confirm.", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    private void saveOrderToDatabase() {
        StringBuilder orderDetails = new StringBuilder();
        for (CartItem item : cartItems) {
            orderDetails.append(item.getFoodName())
                    .append(" x").append(item.getQuantity())
                    .append(", ");
        }

        String timestamp = String.valueOf(System.currentTimeMillis());

        // Instantiate DatabaseHelper and save order
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.insertOrder(orderDetails.toString(), totalBill, timestamp);

        Toast.makeText(this, "Order saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
