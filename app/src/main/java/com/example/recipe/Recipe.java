package com.example.recipe;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private String ingredient;

    public Recipe() {

    }

    public Recipe(String ingredient, String name) {
        this.ingredient = ingredient;
        this.name = name;
    }

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
