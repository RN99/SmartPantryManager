package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Bind views
        TextView textRecipeName = findViewById(R.id.textRecipeName);
        TextView textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        TextView textRecipeSteps = findViewById(R.id.textRecipeSteps);

        // Get data passed from adapter
        String recipeName = getIntent().getStringExtra("name");
        String recipeIngredients = getIntent().getStringExtra("ingredients");
        String recipeSteps = getIntent().getStringExtra("steps");

        // Display data
        textRecipeName.setText(recipeName);
        textRecipeIngredients.setText(recipeIngredients);
        textRecipeSteps.setText(recipeSteps);
    }
}
