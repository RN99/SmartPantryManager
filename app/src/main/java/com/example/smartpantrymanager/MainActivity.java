package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewPantry;
    FloatingActionButton fabAddIngredient;
    DataBaseHelper dbHelper;
    List<Ingredient> ingredientList;
    IngredientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        fabAddIngredient = findViewById(R.id.fabAddIngredient);
        dbHelper = new DataBaseHelper(this);

        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));
        ingredientList = new ArrayList<>();
        adapter = new IngredientAdapter(ingredientList);
        recyclerViewPantry.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }

    private void loadIngredients() {
        ingredientList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DataBaseHelper.TABLE_INGREDIENTS,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COL_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(DataBaseHelper.COL_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COL_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COL_EXPIRY));

                ingredientList.add(new Ingredient(id, name, quantity, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
