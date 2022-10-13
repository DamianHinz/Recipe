package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewRecipe extends AppCompatActivity {

    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView recipeRV;
    private ArrayList<Recipe> recipesArrayList;
    private RecipeRVAdapter recipeRVAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUid;
    private Button deleteModeBtn, randomRecipeBtn;
    ProgressBar loadingPB;

    private boolean deleteMode = false;

    public boolean getDeleteMode() { return  deleteMode; }

    private void setDeleteMode(boolean mode) {deleteMode = mode; }


    //To start ViewIngredient after a recipe has been clicked
    public void startIngredientIntent(Intent i) {
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        deleteMode = false;

        // initializing our variables.
        recipeRV = findViewById(R.id.idRVRecipes);
        loadingPB = findViewById(R.id.idProgressBar);
        deleteModeBtn = findViewById(R.id.idBtnRecipeDeleteMode);
        randomRecipeBtn = findViewById(R.id.idBtnRecipeRandom);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        // creating our new array list
        recipesArrayList = new ArrayList<>();
        recipeRV.setHasFixedSize(true);
        recipeRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // adding our array list to our recycler view adapter class.
        recipeRVAdapter = new RecipeRVAdapter(recipesArrayList, this, this);

        // setting adapter to our recycler view.
        recipeRV.setAdapter(recipeRVAdapter);


        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Recipes
        // now we will be getting the data from the same reference.
        db.collection("Users").document(currentUid).collection("Recipe").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Recipe c = d.toObject(Recipe.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                recipesArrayList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            recipeRVAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(ViewRecipe.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
                Toast.makeText(ViewRecipe.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDeleteMode();
                Toast.makeText(getApplicationContext(),"Delete mode activated", Toast.LENGTH_LONG).show();
                refreshForDeleteMode();
            }
        });

        randomRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int recipeCount = recipesArrayList.size();
                Random rand = new Random();
                int randInt = rand.nextInt(recipeCount);
                String randRecipeName = recipesArrayList.get(randInt).getName();


                //creates a Bundle with the Name of the clicked recipe
                Bundle b = new Bundle();
                b.putString("clickedRecipe", randRecipeName);
                //Starts new activity to show ingredients of clicked recipe
                Intent in = new Intent(getApplicationContext(), ViewIngredient.class);
                in.putExtras(b);
                startIngredientIntent(in);
            }
        });
    }

    private void refreshForDeleteMode() {
        RecipeRVAdapter adapter = new RecipeRVAdapter(recipesArrayList, this, this);
        recipeRV.setAdapter(adapter);
    }

    private void switchDeleteMode() {
        if (getDeleteMode()) {
            setDeleteMode(false);
        } else setDeleteMode(true);
    }

    //function to delete Recipe called in RecipeViewHolder
    public void deleteRecipe(String recipeName) {
        DocumentReference recipeDocRef = db.collection("Users").document(currentUid).collection("Recipe").document("" + recipeName);

        //Delete all Ingredient Documents
        recipeDocRef.collection("Ingredients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    int count = list.size();

                    //creates array of saved ingredient numbers to delete
                    int ingredientNumbers[] = ingredientDataNumber(list);

                    for (int i = 0;i < count;i++) {
                        recipeDocRef.collection("Ingredients").document("" + ingredientNumbers[i]).delete();
                    }
                }
            }
        });

        //Delete Recipe Document
        recipeDocRef.delete();
        refreshForDeleteMode();
    }
    public int[] ingredientDataNumber(List<DocumentSnapshot> list) {
        int count = list.size();
        int ingredientNumbers[] = new int[count];
        for (int j = 0; j < ingredientNumbers.length; j++) {
            String[] listArray = list.get(j).toString().split("/");
            ingredientNumbers[j] = Integer.valueOf(listArray[5].substring(0,1));
        }
        return ingredientNumbers;
    }
}


