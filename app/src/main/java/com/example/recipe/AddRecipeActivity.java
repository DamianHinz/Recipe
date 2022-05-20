package com.example.recipe;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText recipeIngredientEdt, recipeNameEdt;

    private Button submitRecipeBtn;

    private String recipeIngredient, recipeName;

    private FirebaseFirestore db;

    private void addDataToFirestore(String recipeIngredient) {

        //creating collection Reference for database
        CollectionReference dbRecipe = db.collection("Recipe");

        //adding data to recipe object class
        Recipe recipe = new Recipe(recipeIngredient, recipeName);

        DocumentReference docIdRef = db.collection("Recipe").document(recipeName);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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

        db = FirebaseFirestore.getInstance();

        recipeIngredientEdt = findViewById(R.id.idEdtRecipeIngredient);
        recipeNameEdt = findViewById(R.id.idEdtRecipeName);
        submitRecipeBtn = findViewById(R.id.idBtnSubmitRecipe);


        //adding on click listener for button
        submitRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting Data from edittext field
                recipeIngredient = recipeIngredientEdt.getText().toString();
                recipeName = recipeNameEdt.getText().toString();

                //check if text field is empty
                if (TextUtils.isEmpty(recipeIngredient)) {
                    recipeIngredientEdt.setError("Please enter Ingredient.");
                } else if (TextUtils.isEmpty(recipeName)) {
                    recipeNameEdt.setError("Please enter recipe Name");
                } else {
                    //calling function to add data to Firebase Firestore
                    addDataToFirestore(recipeIngredient);
                }
            }
        });
    }
}
