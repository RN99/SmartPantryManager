package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;

    public IngredientAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);

        holder.textName.setText(ingredient.getName());
        holder.textQuantity.setText(String.format("%.2f %s", ingredient.getQuantity(), ingredient.getUnit()));

        // Expiry logic
        if (ingredient.isExpired()) {
            holder.textWarning.setVisibility(View.VISIBLE);
            holder.textExpiry.setText("Expired!");
        } else if (ingredient.isExpiringSoon()) {
            holder.textWarning.setVisibility(View.GONE);
            holder.textExpiry.setText("Expiring soon: " + ingredient.getExpiryDate());
        } else {
            holder.textWarning.setVisibility(View.GONE);
            holder.textExpiry.setText("Expiry: " + ingredient.getExpiryDate());
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredientList.add(ingredient);
        notifyItemInserted(ingredientList.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textQuantity, textExpiry, textWarning;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textExpiry = itemView.findViewById(R.id.textExpiry);
            textWarning = itemView.findViewById(R.id.textWarning);
        }
    }
}
