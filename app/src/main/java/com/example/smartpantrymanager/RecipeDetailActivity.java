package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView textRecipeName, textRecipeIngredients, textRecipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textRecipeName = findViewById(R.id.textRecipeName);
        textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        textRecipeSteps = findViewById(R.id.textRecipeSteps);

        // Get recipe details from Intent
        String name = getIntent().getStringExtra("name");
        String ingredients = getIntent().getStringExtra("ingredients");
        String steps = getIntent().getStringExtra("steps");

        textRecipeName.setText(name);

        // Format ingredients as bullet list
        String[] ingredientArray = ingredients.split(","); // assuming ingredients are comma-separated
        StringBuilder bulletList = new StringBuilder("Ingredients:\n");
        for (String ing : ingredientArray) {
            bulletList.append("• ").append(ing.trim()).append("\n");
        }

        // Apply styling
        SpannableString styledText = new SpannableString(bulletList.toString());

        int start = 0;
        for (String ing : ingredientArray) {
            int end = start + ("• " + ing.trim() + "\n").length();

            // Example check: mark expired if ingredient contains "expired"
            if (ing.toLowerCase().contains("expired")) {
                styledText.setSpan(
                        new ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_red_dark)),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            start = end;
        }

        textRecipeIngredients.setText(styledText);
        textRecipeSteps.setText("Steps:\n" + steps);
    }
}
