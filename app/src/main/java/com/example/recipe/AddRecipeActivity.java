package com.example.recipe;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText  recipeNameEdt;

    private Button submitRecipeBtn;

    private String recipeName;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    private void addDataToFirestore() {


        String currentUid = mAuth.getCurrentUser().getUid();

        //creating collection Reference for database
        CollectionReference dbRecipe = db.collection("Users").document(currentUid).collection("Recipe");

        //adding data to recipe object class
        Recipe recipe = new Recipe(recipeName, 0);

        DocumentReference docIdRef = db.collection("Users").document(currentUid).collection("Recipe").document(recipeName);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) { //If Recipe already exists
                        Toast.makeText(AddRecipeActivity.this, "Recipe already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        dbRecipe.document(recipeName).set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //after data adding is successful display success massage
                                Toast.makeText(AddRecipeActivity.this, "Your Recipe has been added to Database", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //displaying massage when data adding is failed
                                Toast.makeText(AddRecipeActivity.this, "Failed to add Recipe to Database", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Log.d("TAG", "Failed with: ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recipeNameEdt = findViewById(R.id.idEdtRecipeName);
        submitRecipeBtn = findViewById(R.id.idBtnSubmitRecipe);


        //adding on click listener for button
        submitRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting Data from edittext field
                recipeName = recipeNameEdt.getText().toString();

                //check if text field is empty
                if (TextUtils.isEmpty(recipeName)) {
                    recipeNameEdt.setError("Please enter recipe Name");
                } else {
                    //calling function to add data to Firebase Firestore
                    addDataToFirestore();

                    //Put Ingredients in new Recipe
                    Bundle b = new Bundle();
                    b.putString("clickedRecipe", recipeName);
                    Intent i = new Intent(AddRecipeActivity.this, AddIngredientsActivity.class); //To add new ingredients
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });
    }
}
