package com.example.miniproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PreviousOrdersActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lvPreviousOrders;
    private ArrayList<String> previousOrdersList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        lvPreviousOrders = findViewById(R.id.lvPreviousOrders);

        // Fetch previous orders
        previousOrdersList = dbHelper.getAllOrders();

        // Display orders in ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, previousOrdersList);
        lvPreviousOrders.setAdapter(adapter);
    }
}
