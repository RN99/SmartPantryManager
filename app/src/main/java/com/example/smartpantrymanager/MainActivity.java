package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private ImageView imageAddIngredient;
    private DataBaseHelper dbHelper;
    private List<Ingredient> ingredientList;
    private IngredientAdapter adapter;

    private TextView emptyPantryMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        emptyPantryMessage = findViewById(R.id.emptyPantryMessage);
        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        imageAddIngredient = findViewById(R.id.imageAddIngredient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageViewRecipes = findViewById(R.id.imageViewRecipes);

        // Database helper
        dbHelper = new DataBaseHelper(this);

        // RecyclerView setup
        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));
        ingredientList = new ArrayList<>();
        adapter = new IngredientAdapter(ingredientList);
        recyclerViewPantry.setAdapter(adapter);

        // ImageView → AddIngredientActivity
        imageAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });

        imageViewRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }

    private void loadIngredients() {
        ingredientList.clear();
        ingredientList.addAll(dbHelper.getAllIngredients());
        adapter.notifyDataSetChanged();

        // Show empty message if no ingredients
        if (ingredientList.isEmpty()) {
            emptyPantryMessage.setVisibility(View.VISIBLE);
            recyclerViewPantry.setVisibility(View.GONE);
        } else {
            emptyPantryMessage.setVisibility(View.GONE);
            recyclerViewPantry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.menu_suggested) {
            startActivity(new Intent(this, SuggestedRecipesActivity.class));
            return true;
        }
        else if (id == R.id.menu_add_recipe) {
            Intent intent = new Intent(this, AddRecipeActivity.class);
            startActivityForResult(intent, 200); // request code 200 for recipes
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            String recipeName = data.getStringExtra("recipeName");
            String recipeSteps = data.getStringExtra("recipeSteps");
            int ingredientCount = data.getIntExtra("ingredientCount", 0);

            //  Add current date
            String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd",
                    java.util.Locale.getDefault()).format(new java.util.Date());

            // Show confirmation
            Toast.makeText(this,
                    "Recipe saved: " + recipeName +
                            " (" + ingredientCount + " ingredients) on " + currentDate,
                    Toast.LENGTH_LONG).show();

        }
    }


}


