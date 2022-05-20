package com.example.recipe;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private ArrayList<Ingredient> ingredient;

    public Recipe() {

    }

    public Recipe(ArrayList<Ingredient> ingredient, String name) {
        this.ingredient = ingredient;
        this.name = name;
    }

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }
}
