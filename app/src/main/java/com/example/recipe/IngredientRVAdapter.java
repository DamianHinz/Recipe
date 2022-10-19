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
        return (position == ingredientArrayList.size()) ? R.layout.button_item : R.layout.ingredient_item;
    }

    @NonNull
    @Override
    public IngredientRVAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        View itemView;
        if ( viewType == R.layout.ingredient_item ) {
            itemView = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.button_item, parent, false);
        }
        return new IngredientRVAdapter.IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientRVAdapter.IngredientViewHolder holderIn, int position) {

        if (position == ingredientArrayList.size()) {
            holderIn.editModeBtn.setText("Edit Mode");
            holderIn.editModeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upper.switchDeleteMode();
                }
            });
        } else {
            Ingredient ingredients = ingredientArrayList.get(position);
            holderIn.ingredientTV.setText((position + 1) + ": " + ingredients.getName() + " " + ingredients.getAmount() + " " + ingredients.getUnit());
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
        return ingredientArrayList.size() + 1;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView ingredientTV;
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
        }
    }
}
