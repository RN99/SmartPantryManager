package com.example.smartpantrymanager;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;
    private DataBaseHelper dbHelper;

    public IngredientAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        dbHelper = new DataBaseHelper(parent.getContext());
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

        // Edit button
        holder.buttonEdit.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Edit " + ingredient.getName(), Toast.LENGTH_SHORT).show();
        });

        // Delete button
        holder.buttonDelete.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DataBaseHelper.TABLE_INGREDIENTS,
                    DataBaseHelper.COL_ID + "=?",
                    new String[]{String.valueOf(ingredient.getId())});
            db.close();

            ingredientList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(v.getContext(), ingredient.getName() + " deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textQuantity, textExpiry, textWarning;
        ImageButton buttonEdit, buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textExpiry = itemView.findViewById(R.id.textExpiry);
            textWarning = itemView.findViewById(R.id.textWarning);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
