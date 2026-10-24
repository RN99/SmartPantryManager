package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText editTextRecipeName, editTextRecipeSteps;
    private RecyclerView recyclerViewIngredients;
    private Button buttonAddIngredient, buttonSaveRecipe;

    private List<Ingredient> ingredientList;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Link XML views
        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextRecipeSteps = findViewById(R.id.editTextRecipeSteps);
        recyclerViewIngredients = findViewById(R.id.recyclerViewRecipeIngredients);
        buttonAddIngredient = findViewById(R.id.buttonAddIngredientToRecipe);
        buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);

        // Setup RecyclerView
        ingredientList = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(ingredientList);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        // Add Ingredient button
        buttonAddIngredient.setOnClickListener(v -> {
            AddIngredientDialog dialog = new AddIngredientDialog();
            dialog.setAddIngredientListener(ingredient -> {
                ingredientAdapter.addIngredient(ingredient);
                recyclerViewIngredients.scrollToPosition(ingredientAdapter.getItemCount() - 1);
            });
            dialog.show(getSupportFragmentManager(), "AddIngredientDialog");
        });

        buttonSaveRecipe.setOnClickListener(v -> {
            String recipeName = editTextRecipeName.getText().toString().trim();
            String recipeSteps = editTextRecipeSteps.getText().toString().trim();

            if (recipeName.isEmpty() || recipeSteps.isEmpty()) {
                Toast.makeText(this, "Please fill in recipe name and steps", Toast.LENGTH_SHORT).show();
            } else if (ingredientAdapter.getItemCount() == 0) {
                Toast.makeText(this, "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
            } else {
                // ✅ Save recipe into DB
                DataBaseHelper db = new DataBaseHelper(this);

                // Build ingredients string for display
                StringBuilder ingredientsBuilder = new StringBuilder();
                for (Ingredient ing : ingredientList) {
                    ingredientsBuilder.append(ing.getName())
                            .append(" (")
                            .append(ing.getQuantity())
                            .append(" ")
                            .append(ing.getUnit())
                            .append("), ");
                }
                String ingredients = ingredientsBuilder.toString().trim();

                // Insert recipe into recipes table
                Recipe recipe = new Recipe(recipeName, ingredients, recipeSteps);
                long recipeId = db.insertRecipe(recipe);

                if (recipeId != -1) {
                    // Save linked ingredients into recipe_ingredients table
                    for (Ingredient ing : ingredientList) {
                        db.insertRecipeIngredient((int) recipeId,
                                ing.getName(),
                                ing.getQuantity(),
                                ing.getUnit());
                    }

                    Toast.makeText(this, "Recipe saved!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error saving recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
