package com.example.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewIngredient extends AppCompatActivity{
    private RecyclerView ingredientRV;
    private ArrayList ingredientArrayList;
    private IngredientRVAdapter ingredientRVAdapter;
    private String recipeName;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    public String getRecipeName(){ return this.recipeName; }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_view_ingrdient);

        //initializing variables
        ingredientRV = findViewById(R.id.idRVIngredient);
        loadingPB = findViewById(R.id.idProgressBarIngredient);

        db = FirebaseFirestore.getInstance();

        //creating ArrayList
        ingredientArrayList = new ArrayList<>();
        ingredientRV.setHasFixedSize(true);
        ingredientRV.setLayoutManager(new LinearLayoutManager(this));

        ingredientRVAdapter = new IngredientRVAdapter(ingredientArrayList, this);

        ingredientRV.setAdapter(ingredientRVAdapter);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            setRecipeName(b.getString("clickedRecipe"));
            System.out.println("Bundle Output: (" + getRecipeName() + ")");
        }


        //getting Data from Database
        db.collection("Recipe").document("" + getRecipeName()).collection("Ingredients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
}
