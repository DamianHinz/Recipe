package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeRVAdapter extends RecyclerView.Adapter<RecipeRVAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<Recipe> recipesArrayList;
    private Context context;

    // creating constructor for our adapter class
    public RecipeRVAdapter(ArrayList<Recipe> recipesArrayList, Context context) {
        this.recipesArrayList = recipesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Recipe recipes = recipesArrayList.get(position);
        holder.ingredientTV.setText("Ingredients: " + recipes.getIngredient());
        holder.recipeNameTV.setText("Name: " + recipes.getName());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return recipesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView ingredientTV;
        private final TextView recipeNameTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            ingredientTV = itemView.findViewById(R.id.idTVIngredient);
            recipeNameTV = itemView.findViewById(R.id.idTVRecipeName);

        }
    }
}
