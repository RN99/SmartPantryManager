package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerView = findViewById(R.id.recyclerViewRecipes);
        emptyView = findViewById(R.id.textViewEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DataBaseHelper(this);
        List<Recipe> matchedRecipes = dbHelper.getMatchingRecipes();

        if (matchedRecipes.isEmpty()) {
            recyclerView.setVisibility(RecyclerView.GONE);
            emptyView.setVisibility(TextView.VISIBLE);
        } else {
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            emptyView.setVisibility(TextView.GONE);

            RecipeAdapter adapter = new RecipeAdapter(matchedRecipes);
            recyclerView.setAdapter(adapter);
        }
    }
}
