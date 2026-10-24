package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {
    private int recipeId;
    private EditText editName, editIngredients, editSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        editName = findViewById(R.id.editRecipeName);
        editIngredients = findViewById(R.id.editRecipeIngredients);
        editSteps = findViewById(R.id.editRecipeSteps);
        Button buttonSave = findViewById(R.id.buttonSaveRecipe);

        // Get data from intent
        recipeId = getIntent().getIntExtra("id", -1);
        editName.setText(getIntent().getStringExtra("name"));
        editIngredients.setText(getIntent().getStringExtra("ingredients"));
        editSteps.setText(getIntent().getStringExtra("steps"));

        // Save changes
        buttonSave.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(this);
            Recipe updated = new Recipe(recipeId,
                    editName.getText().toString(),
                    editIngredients.getText().toString(),
                    editSteps.getText().toString());

            int rows = db.updateRecipe(updated);
            if (rows > 0) {
                Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show();

                // Send result back
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Error updating recipe", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

