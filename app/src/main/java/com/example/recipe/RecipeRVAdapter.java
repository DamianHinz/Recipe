package com.example.recipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        holder.recipeNameTV.setText("Name: " + recipes.getName());
        holder.deleteBtn.setText("DELETE");
        holder.editBtn.setText("EDIT");
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return recipesArrayList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView recipeNameTV;
        private Button deleteBtn, editBtn;

        public String convertRecipeNameTV (TextView nameTV) {
            String erg = nameTV.getText().toString();
            return erg.substring(6);
        }

        private void DeleteButtonAlert_OnClick(View view, TextView toDeleteTV) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setTitle("Delete");
            alert.setMessage("Are you sure?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    upper.deleteRecipe(convertRecipeNameTV(toDeleteTV));
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        }

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            recipeNameTV = itemView.findViewById(R.id.idTVRecipeName);
            deleteBtn = itemView.findViewById(R.id.idBtnRecipeDelete);
            editBtn = itemView.findViewById(R.id.idBtnRecipeEdit);

            if (upper.getDeleteMode()) {
                deleteBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.VISIBLE);
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



            //calls function to delete Recipe in ViewRecipe class
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteButtonAlert_OnClick(view, recipeNameTV);
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putString("clickedRecipe", convertRecipeNameTV(recipeNameTV));
                    Intent in = new Intent(context, EditRecipeDescriptionActivity.class);
                    in.putExtras(b);
                    upper.startIngredientIntent(in);
                }
            });
        }

    }
}
