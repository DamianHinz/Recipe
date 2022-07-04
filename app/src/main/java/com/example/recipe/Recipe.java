package com.example.recipe;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private int ingredientCount;



    public Recipe() {

    }

    public Recipe(String name, int ingredientCount) {
        this.name = name;
        this.ingredientCount = ingredientCount;
    }

    public int getIngredientCount() { return ingredientCount; }

    public void setIngredientCount(int ingredientCount) { this.ingredientCount = ingredientCount; }

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() + "  " + getIngredientCount();
    }
}
