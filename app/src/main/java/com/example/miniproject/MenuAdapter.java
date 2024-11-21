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

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> implements android.widget.Filterable {

    private List<String> menuItems; // Original list of items
    private List<String> menuItemsFiltered; // Filtered list for search
    private List<String> foodPrices;
    private List<Integer> foodImages;
    private int[] itemQuantities; // Tracks quantities for items
    private OnAddClickListener onAddClickListener;

    // Interface for add button click
    public interface OnAddClickListener {
        void onAddClick(String itemName, String itemPrice, int quantity);
    }

    public MenuAdapter(List<String> menuItems, List<String> foodPrices, List<Integer> foodImages, OnAddClickListener onAddClickListener) {
        this.menuItems = menuItems;
        this.menuItemsFiltered = new ArrayList<>(menuItems); // Initially filtered list is the same as the original list
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
        this.onAddClickListener = onAddClickListener;
        this.itemQuantities = new int[menuItems.size()];
        for (int i = 0; i < menuItems.size(); i++) {
            itemQuantities[i] = 0; // Initialize quantities to 0
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
        return menuItemsFiltered.size(); // Use filtered list for item count
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
            // Get the original position of the filtered item
            String itemName = menuItemsFiltered.get(position);
            int originalPosition = menuItems.indexOf(itemName);

            foodName.setText(itemName);
            foodPrice.setText(foodPrices.get(originalPosition));
            foodImage.setImageResource(foodImages.get(originalPosition));
            quantityText.setText(String.valueOf(itemQuantities[originalPosition]));

            // Increase button
            increaseButton.setOnClickListener(v -> {
                itemQuantities[originalPosition]++;
                quantityText.setText(String.valueOf(itemQuantities[originalPosition]));
            });

            // Decrease button
            decreaseButton.setOnClickListener(v -> {
                if (itemQuantities[originalPosition] > 0) {
                    itemQuantities[originalPosition]--;
                    quantityText.setText(String.valueOf(itemQuantities[originalPosition]));
                }
            });

            // Add button
            addButton.setOnClickListener(v -> {
                if (itemQuantities[originalPosition] > 0) {
                    onAddClickListener.onAddClick(itemName, foodPrices.get(originalPosition), itemQuantities[originalPosition]);
                } else {
                    Toast.makeText(itemView.getContext(), "Please increase the quantity before adding!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Filterable implementation for search functionality
    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(menuItems); // If no query, show all items
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    // Filter items based on the query
                    for (String item : menuItems) {
                        if (item.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                menuItemsFiltered.clear();
                if (results.values != null) {
                    menuItemsFiltered.addAll((List) results.values);
                }
                notifyDataSetChanged(); // Notify adapter of changes
            }
        };
    }
}
