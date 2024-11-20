package com.example.miniproject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    private Button btnShowMenu, btnPreviousOrders;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        btnShowMenu = findViewById(R.id.ShowMenu);
        btnPreviousOrders = findViewById(R.id.PreviousOrders);
        btnShowMenu.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, ShowMenuActivity.class)));
        btnPreviousOrders.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, PreviousOrdersActivity.class)));
    }
}
