package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Ingredient table
    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    // Recipe table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "_id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    // Recipe Ingredients linking table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RECIPE_REF_ID = "recipe_id";
    public static final String COL_REQUIRED_NAME = "ingredient_name";
    public static final String COL_REQUIRED_QTY = "required_qty";
    public static final String COL_REQUIRED_UNIT = "unit";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Enforce foreign key constraints
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Pantry Table
        String createIngredientsTable = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY + " TEXT)";
        db.execSQL(createIngredientsTable);

        // Recipe Table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)";
        db.execSQL(createRecipesTable);

        // Recipe Ingredients Linking Table
        String createRecipeIngredientsTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RECIPE_REF_ID + " INTEGER NOT NULL, " +
                COL_REQUIRED_NAME + " TEXT NOT NULL, " +
                COL_REQUIRED_QTY + " REAL NOT NULL, " +
                COL_REQUIRED_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_RECIPE_REF_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))";
        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // -------------------- INGREDIENT CRUD --------------------

    public long insertIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, ingredient.getName());
        values.put(COL_QUANTITY, ingredient.getQuantity());
        values.put(COL_UNIT, ingredient.getUnit());
        values.put(COL_EXPIRY, ingredient.getExpiryDate());
        long result = db.insert(TABLE_INGREDIENTS, null, values);
        db.close();
        return result;
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY))
                );
                ingredientList.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ingredientList;
    }

    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, ingredient.getName());
        values.put(COL_QUANTITY, ingredient.getQuantity());
        values.put(COL_UNIT, ingredient.getUnit());
        values.put(COL_EXPIRY, ingredient.getExpiryDate());
        int rows = db.update(TABLE_INGREDIENTS, values, COL_ID + "=?",
                new String[]{String.valueOf(ingredient.getId())});
        db.close();
        return rows;
    }

    public int deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_INGREDIENTS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // -------------------- RECIPE CRUD --------------------

    public long insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, recipe.getName());
        values.put(COL_RECIPE_STEPS, recipe.getSteps());
        long result = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return result;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS));

                //  Fetch linked ingredients for this recipe
                List<RecipeIngredient> linkedIngredients = getIngredientsForRecipe(id);
                StringBuilder ingredientsBuilder = new StringBuilder();
                for (RecipeIngredient ing : linkedIngredients) {
                    ingredientsBuilder.append(ing.getName())
                            .append(" (")
                            .append(ing.getQuantity())
                            .append(" ")
                            .append(ing.getUnit())
                            .append(")\n ");
                }
                String ingredients = ingredientsBuilder.length() > 0
                        ? ingredientsBuilder.substring(0, ingredientsBuilder.length() - 2)
                        : "";

                //  Use full constructor
                Recipe recipe = new Recipe(id, name, ingredients, steps);
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeList;
    }


    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, recipe.getName());
        values.put(COL_RECIPE_STEPS, recipe.getSteps());
        int rows = db.update(TABLE_RECIPES, values, COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipe.getId())});
        db.close();
        return rows;
    }

    public int deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_RECIPES, COL_RECIPE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // -------------------- RECIPE INGREDIENTS CRUD --------------------

    public long insertRecipeIngredient(int recipeId, String name, double qty, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_REF_ID, recipeId);
        values.put(COL_REQUIRED_NAME, name);
        values.put(COL_REQUIRED_QTY, qty);
        values.put(COL_REQUIRED_UNIT, unit);
        long result = db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
        db.close();
        return result;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS,
                null,
                COL_RECIPE_REF_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                RecipeIngredient ingredient = new RecipeIngredient(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_REQUIRED_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_REQUIRED_QTY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_REQUIRED_UNIT))
                );
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ingredients;
    }

    public int deleteIngredientsForRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_RECIPE_INGREDIENTS, COL_RECIPE_REF_ID + "=?", new String[]{String.valueOf(recipeId)});
        db.close();
        return rows;
    }

    // -------------------- MATCHING ENGINE --------------------
    public List<Recipe> getMatchingRecipes() {
        List<Recipe> matchedRecipes = new ArrayList<>();

        // Get all recipes
        List<Recipe> allRecipes = getAllRecipes();

        // Get pantry ingredients
        List<Ingredient> pantry = getAllIngredients();

        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> requiredIngredients = getIngredientsForRecipe(recipe.getId());
            boolean allMatch = true;

            for (RecipeIngredient req : requiredIngredients) {
                boolean found = false;
                for (Ingredient pantryItem : pantry) {
                    String pantryName = pantryItem.getName().toLowerCase().trim();
                    String reqName = req.getName().toLowerCase().trim();

                    if (pantryName.equals(reqName) || pantryName.equals(reqName + "s")) {
                        if (pantryItem.getQuantity() >= req.getQuantity()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    allMatch = false;
                    break;
                }
            }

            if (allMatch) {
                matchedRecipes.add(recipe);
            }
        }

        return matchedRecipes;
    }

}
