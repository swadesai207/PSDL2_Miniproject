package com.example.miniproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<String> menuItems;
    private List<String> foodPrices;
    private List<Integer> foodImages;
    private int[] itemQuantities;
    private OnAddClickListener onAddClickListener;

    public interface OnAddClickListener {
        void onAddClick(String itemName, String itemPrice, int quantity);
    }

    public MenuAdapter(List<String> menuItems, List<String> foodPrices, List<Integer> foodImages, OnAddClickListener onAddClickListener) {
        this.menuItems = menuItems;
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
        this.onAddClickListener = onAddClickListener;
        this.itemQuantities = new int[menuItems.size()];
        for (int i = 0; i < menuItems.size(); i++) {
            itemQuantities[i] = 0;
        }
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodPrice, quantityText;
        private ImageView foodImage;
        private Button addButton, increaseButton, decreaseButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.textView2);
            foodPrice = itemView.findViewById(R.id.cartitemprice);
            foodImage = itemView.findViewById(R.id.imageView3);
            quantityText = itemView.findViewById(R.id.quantityText);
            addButton = itemView.findViewById(R.id.buttonRemove);
            increaseButton = itemView.findViewById(R.id.buttonPlus);
            decreaseButton = itemView.findViewById(R.id.buttonMinus);
        }

        public void bind(int position) {
            foodName.setText(menuItems.get(position));
            foodPrice.setText(foodPrices.get(position));
            foodImage.setImageResource(foodImages.get(position));
            quantityText.setText(String.valueOf(itemQuantities[position]));

            increaseButton.setOnClickListener(v -> {
                itemQuantities[position]++;
                quantityText.setText(String.valueOf(itemQuantities[position]));
            });

            decreaseButton.setOnClickListener(v -> {
                if (itemQuantities[position] > 0) {
                    itemQuantities[position]--;
                    quantityText.setText(String.valueOf(itemQuantities[position]));
                }
            });

            addButton.setOnClickListener(v -> {
                if (itemQuantities[position] > 0) {
                    onAddClickListener.onAddClick(menuItems.get(position), foodPrices.get(position), itemQuantities[position]);
                } else {
                    Toast.makeText(itemView.getContext(), "Please increase the quantity before adding!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
