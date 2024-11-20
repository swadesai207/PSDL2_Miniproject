package com.example.miniproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShowMenuActivity extends AppCompatActivity implements MenuAdapter.OnAddClickListener {

    private List<String> menuItems = new ArrayList<>();
    private List<String> foodPrices = new ArrayList<>();
    private List<Integer> foodImages = new ArrayList<>();
    private List<String> cartItems = new ArrayList<>();
    private List<Integer> cartQuantities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu);

        // Initialize the menu data
        initializeMenuData();

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        MenuAdapter adapter = new MenuAdapter(menuItems, foodPrices, foodImages, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Button to move to cart (OrderSummaryActivity)
        Button moveToCartButton = findViewById(R.id.button);
        moveToCartButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                // Pass the cart items to the OrderSummaryActivity
                Intent intent = new Intent(ShowMenuActivity.this, OrderSummaryActivity.class);
                intent.putStringArrayListExtra("cartItems", new ArrayList<>(cartItems));
                intent.putIntegerArrayListExtra("cartQuantities", new ArrayList<>(cartQuantities));
                startActivity(intent);
            }
        });
    }

    private void initializeMenuData() {
        // You can populate this data dynamically from your database, API, or other sources
        menuItems.add("Aloo Pattice");
        menuItems.add("Thick Coffee");
        menuItems.add("Sandwich");
        menuItems.add("Fresh Juice");
        menuItems.add("Garam Chai");

        foodPrices.add("₹18");
        foodPrices.add("₹30");
        foodPrices.add("₹40");
        foodPrices.add("₹30");
        foodPrices.add("₹12");

        foodImages.add(R.drawable.pattice);  // Replace with actual image resources
        foodImages.add(R.drawable.coldcoffee);
        foodImages.add(R.drawable.chocolatesandwich);
        foodImages.add(R.drawable.watermelonjuice);
        foodImages.add(R.drawable.chai);
    }

    @Override
    public void onAddClick(String itemName, String itemPrice, int quantity) {
        if (quantity > 0) {
            // Add item to the cart
            cartItems.add(itemName);
            cartQuantities.add(quantity);
            Toast.makeText(this, itemName + " added to cart!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please increase the quantity before adding!", Toast.LENGTH_SHORT).show();
        }
    }
}
