package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
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
        return super.onOptionsItemSelected(item);
    }

}


