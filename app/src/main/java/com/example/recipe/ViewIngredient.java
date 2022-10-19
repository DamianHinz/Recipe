package com.example.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewIngredient extends AppCompatActivity{
    private RecyclerView ingredientRV;
    private TextView recipeDescriptionTV;
    private ArrayList ingredientArrayList;
    private IngredientRVAdapter ingredientRVAdapter;
    private String recipeName, currentUid;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button toAddIngredientBtn;
    ProgressBar loadingPB;

    private Boolean deleteMode = false;

    public Boolean getDeleteMode() { return deleteMode; }

    public void setDeleteMode(Boolean deleteMode) { this.deleteMode = deleteMode; }

    public String getRecipeName(){ return this.recipeName; }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_view_ingrdient);
        deleteMode = false;

        //initializing variables
        ingredientRV = findViewById(R.id.idRVIngredient);
        loadingPB = findViewById(R.id.idProgressBarIngredient);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        //creating ArrayList
        ingredientArrayList = new ArrayList<>();
        ingredientRV.setHasFixedSize(true);
        ingredientRV.setLayoutManager(new LinearLayoutManager(this));

        ingredientRVAdapter = new IngredientRVAdapter(ingredientArrayList, this, this);

        ingredientRV.setAdapter(ingredientRVAdapter);

        //Checks if the received recipe exists
        Bundle b = getIntent().getExtras();
        if(b != null) {
            setRecipeName(b.getString("clickedRecipe"));
        }

        //getting Data from Database
        initializeArrayList();

        toAddIngredientBtn = findViewById(R.id.idBtnToAddIngredient);

        toAddIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                //Creates Bundle with clicked recipe name.
                Bundle b = new Bundle();
                b.putString("clickedRecipe", getRecipeName());

                Intent i = new Intent(ViewIngredient.this, AddIngredientsActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }


    private void initializeArrayList() {
        db.collection("Users").document(currentUid).collection("Recipe").document("" + getRecipeName()).collection("Ingredients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //check if query snapshot is empty or not
                if(!queryDocumentSnapshots.isEmpty()) {
                    //not empty. Hide PB and add data to list
                    loadingPB.setVisibility(View.GONE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        //passing list to object class
                        Ingredient c = d.toObject(Ingredient.class);
                        //Add object to array list
                        ingredientArrayList.add(c);
                    }
                    //notify that data has been changed
                    ingredientRVAdapter.notifyDataSetChanged();
                } else {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(ViewIngredient.this, "No Data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //if we dont get any Data or an error
                Toast.makeText(ViewIngredient.this, "Fail to get Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void switchDeleteMode() {
        if (getDeleteMode()) {
            setDeleteMode(false);
        } else setDeleteMode(true);
        Toast.makeText(getApplicationContext(), "Delete mode activated", Toast.LENGTH_SHORT).show();
        refreshForDeleteMode();
    }

    private void refreshForDeleteMode() {
        IngredientRVAdapter adapter = new IngredientRVAdapter(ingredientArrayList, this, this);
        ingredientRV.setAdapter(adapter);
    }

    public void deleteIngredient(String ingredientDataNumber) {
        //deletes selected ingredient;
        db.collection("Users").document(currentUid).collection("Recipe").document("" + getRecipeName()).collection("Ingredients").document("" + ingredientDataNumber).delete();

        //updates ingredient count;
        db.collection("Users").document(currentUid).collection("Recipe").document("" + getRecipeName()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Recipe recipe = documentSnapshot.toObject(Recipe.class);
                int oldCount = recipe.getIngredientCount();
                int oldDeleteCount = recipe.getDeleteCount();
                if (oldCount <= 0) return;
                else recipe.setIngredientCount(oldCount - 1);
                recipe.setDeleteCount(oldDeleteCount + 1);
                db.collection("Users").document(currentUid).collection("Recipe").document("" + getRecipeName()).set(recipe);
            }
        });
        refreshForDeleteMode();
    }
}
