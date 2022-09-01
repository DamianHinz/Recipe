package com.example.recipe;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeRVAdapter extends RecyclerView.Adapter<RecipeRVAdapter.RecipeViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<Recipe> recipesArrayList;
    private Context context;
    private ViewRecipe upper;

    // creating constructor for our adapter class
    public RecipeRVAdapter(ArrayList<Recipe> recipesArrayList, Context context, ViewRecipe upper) {
        this.recipesArrayList = recipesArrayList;
        this.context = context;
        this.upper = upper;
    }

    @NonNull
    @Override
    public RecipeRVAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRVAdapter.RecipeViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Recipe recipes = recipesArrayList.get(position);
        holder.ingredientTV.setText("");
        holder.recipeNameTV.setText("Name: " + recipes.getName());
        holder.deleteBtn.setText("DELETE RECIPE");
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return recipesArrayList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView ingredientTV;
        private final TextView recipeNameTV;
        private Button deleteBtn;

        public String convertRecipeNameTV (TextView nameTV) {
            String erg = nameTV.getText().toString();
            return erg.substring(6);
        }

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            ingredientTV = itemView.findViewById(R.id.idTVIngredient);
            recipeNameTV = itemView.findViewById(R.id.idTVRecipeName);
            deleteBtn = itemView.findViewById(R.id.idBtnRecipeDelete);
            if (upper.getDeleteMode()) {
                deleteBtn.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creates a Bundle with the Name of the clicked recipe
                    Bundle b = new Bundle();
                    b.putString("clickedRecipe", convertRecipeNameTV(recipeNameTV));
                    //Starts new activity to show ingredients of clicked recipe
                    Intent in = new Intent(context, ViewIngredient.class);
                    in.putExtras(b);
                    upper.startIngredientIntent(in);
                }
            });
        }

    }
}
