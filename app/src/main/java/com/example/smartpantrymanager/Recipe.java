package com.example.smartpantrymanager;

public class Recipe {
    private int id;
    private String name;
    private String ingredients; // stored as comma-separated text
    private String steps;

    // Constructor for new recipes (no ID yet)
    public Recipe(String name, String ingredients, String steps) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    // Constructor for recipes loaded from DB (with ID)
    public Recipe(int id, String name, String ingredients, String steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }
}
