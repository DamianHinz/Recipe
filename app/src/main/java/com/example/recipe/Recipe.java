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
    private String description;



    public Recipe() {

    }


    public Recipe(String name, int ingredientCount, int deleteCount, String description) {
        this.name = name;
        this.ingredientCount = ingredientCount;
        this.deleteCount = deleteCount;
        this.description = description;
    }

    public int getIngredientCount() { return ingredientCount; }

    public void setIngredientCount(int ingredientCount) { this.ingredientCount = ingredientCount; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public int getDeleteCount() { return deleteCount; }

    public void setDeleteCount(int deleteCount) { this.deleteCount = deleteCount; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return getName() + "  " + getIngredientCount();
    }
}
