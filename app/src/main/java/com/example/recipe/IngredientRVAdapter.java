package com.example.recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class IngredientRVAdapter extends RecyclerView.Adapter<IngredientRVAdapter.IngredientViewHolder>{
    private ArrayList<Ingredient> ingredientArrayList;
    private Context context;
    private ViewIngredient upper;

    public IngredientRVAdapter(ArrayList<Ingredient> ingredientArrayList, Context context, ViewIngredient upper) {
        this.ingredientArrayList = ingredientArrayList;
        this.context = context;
        this.upper = upper;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.description_item;
        } else if (position <= ingredientArrayList.size()) return R.layout.ingredient_item;
        else return R.layout.button_item;
    }

    @NonNull
    @Override
    public IngredientRVAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        View itemView;
        if ( viewType == R.layout.ingredient_item ) {
            itemView = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        } else if (viewType == R.layout.button_item){
            itemView = LayoutInflater.from(context).inflate(R.layout.button_item, parent, false);
        } else itemView = LayoutInflater.from(context).inflate(R.layout.description_item, parent, false);
        return new IngredientRVAdapter.IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientRVAdapter.IngredientViewHolder holderIn, int position) {
        if (position == 0) {
            holderIn.descriptionTV.setText(upper.getRecipeDescription());
        }
        else if (position == ingredientArrayList.size() + 1) {
            holderIn.editModeBtn.setText("Edit Mode");
            holderIn.editModeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upper.switchDeleteMode();
                }
            });
        } else if (position == ingredientArrayList.size() + 2) {
            holderIn.editModeBtn.setText("Add Ingredient");
            holderIn.editModeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upper.addIngredientBtnClicked();
                }
            });
        } else {
            Ingredient ingredients = ingredientArrayList.get(position - 1);
            holderIn.ingredientTV.setText((position) + ": " + ingredients.getName() + " " + ingredients.getAmount() + " " + ingredients.getUnit());
            holderIn.ingredientDataNumber = ingredients.getDataNumber();
            if (upper.getDeleteMode()) {
                holderIn.deleteBtn.setVisibility(View.VISIBLE);
            }
            holderIn.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holderIn.DeleteButtonAlert_OnClick(view, holderIn.ingredientDataNumber);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size() + 3;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView ingredientTV, descriptionTV;
        private Button deleteBtn, editModeBtn;
        int ingredientDataNumber;


        private void DeleteButtonAlert_OnClick(View view, int ingredientDataNumber) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setTitle("Delete");
            alert.setMessage("Are you sure?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    upper.deleteIngredient(String.valueOf(ingredientDataNumber));
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

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            ingredientTV = itemView.findViewById(R.id.idTVIngredientItem);
            deleteBtn = itemView.findViewById(R.id.idBtnIngredientDelete);
            editModeBtn = itemView.findViewById(R.id.idBtnForRecycler);
            descriptionTV = itemView.findViewById(R.id.idTVDescription);

        }
    }
}
