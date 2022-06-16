package com.example.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class IngredientRVAdapter extends RecyclerView.Adapter<IngredientRVAdapter.IngredientViewHolder>{
    private ArrayList<Ingredient> ingredientArrayList;
    private Context context;

    public IngredientRVAdapter(ArrayList<Ingredient> ingredientArrayList, Context context) {
        this.ingredientArrayList = ingredientArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientRVAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new IngredientRVAdapter.IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientRVAdapter.IngredientViewHolder holderIn, int position) {
        Ingredient ingredients = ingredientArrayList.get(position);
        holderIn.ingredientTV.setText((position + 1) + ": " + ingredients.getName() + " " + ingredients.getAmount() + " " + ingredients.getUnit());
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView ingredientTV;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            ingredientTV = itemView.findViewById(R.id.idTVIngredientItem);
        }
    }
}
