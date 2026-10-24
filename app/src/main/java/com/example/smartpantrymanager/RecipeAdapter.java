package com.example.smartpantrymanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.textName.setText(recipe.getName());
        holder.textSteps.setText(recipe.getSteps());

        //  Show ingredients in the card if you want
        // (optional, since you already show them in detail screen)
        // holder.textIngredients.setText(recipe.getIngredients());

        // Open RecipeDetailActivity when clicking the card
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);
            intent.putExtra("name", recipe.getName());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("steps", recipe.getSteps());
            v.getContext().startActivity(intent);
        });

        // Edit button
        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditRecipeActivity.class);
            intent.putExtra("id", recipe.getId());
            intent.putExtra("name", recipe.getName());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("steps", recipe.getSteps());

            ((RecipeListActivity) v.getContext()).startActivityForResult(intent, 100);
        });


        // Delete button
        holder.buttonDelete.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(v.getContext());

            // First delete linked ingredients
            db.deleteIngredientsForRecipe(recipe.getId());

            // Then delete the recipe itself
            int rows = db.deleteRecipe(recipe.getId());

            if (rows > 0) {
                recipeList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, recipeList.size());
                Toast.makeText(v.getContext(), recipe.getName() + " deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Error deleting " + recipe.getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textSteps;
        ImageButton buttonEdit, buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textRecipeName);
            textSteps = itemView.findViewById(R.id.textRecipeSteps);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
