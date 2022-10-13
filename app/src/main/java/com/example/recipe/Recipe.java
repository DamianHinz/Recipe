package com.example.recipe;

import java.util.ArrayList;

/**
 * ingredientCount: number of ingredients in recipe
 * deleteCount: number of deleted ingredients to ensure correct save of new ingredients after deletion
 */
public class Recipe {
    private String name;
    private int ingredientCount;
    private int deleteCount;



    public Recipe() {

    }


    public Recipe(String name, int ingredientCount, int deleteCount) {
        this.name = name;
        this.ingredientCount = ingredientCount;
        this.deleteCount = deleteCount;
    }

    public int getIngredientCount() { return ingredientCount; }

    public void setIngredientCount(int ingredientCount) { this.ingredientCount = ingredientCount; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public int getDeleteCount() { return deleteCount; }

    public void setDeleteCount(int deleteCount) { this.deleteCount = deleteCount; }

    @Override
    public String toString() {
        return getName() + "  " + getIngredientCount();
    }
}
