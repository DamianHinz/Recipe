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


public class AddIngredientsActivity extends AppCompatActivity{

    private EditText ingredientNameEdt, ingredientAmountEdt, ingredientUnitEdt;

    private Button submitIngredientBtn;

    private String ingredientName, ingredientUnit, recipeName, ingredientAmountStr;

    private double ingredientAmount;

    private FirebaseFirestore db;

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeName() {
        return recipeName;
    }


    //gets clicked Reciped to access ingredient count
    private void addIngredientToFirestore(Recipe recipe) {

        Ingredient ingredient = new Ingredient(ingredientName, ingredientUnit, ingredientAmount);

        CollectionReference dbRecipeIngredients = db.collection("Recipe").document("" + getRecipeName()).collection("Ingredients");

        String nextIngredientNum = String.valueOf(recipe.getIngredientCount() + 1);
        DocumentReference docIdRef = dbRecipeIngredients.document("" + nextIngredientNum);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) { //if Document already exists
                        Toast.makeText(AddIngredientsActivity.this, "", Toast.LENGTH_SHORT).show();
                    } else {
                        dbRecipeIngredients.document(nextIngredientNum).set(ingredient).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //increment ingredientCount in recipe
                                incrementIngredientCount(recipe);

                                //after data adding is successful display success massage
                                Toast.makeText(AddIngredientsActivity.this, "Ingredient has been added to Database", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //displaying massage when data adding is failed
                                Toast.makeText(AddIngredientsActivity.this, "Failed to add Ingredient to Database", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Log.d("Tag", "Failed with:", task.getException());
                }
            }
        });
    }

    public void incrementIngredientCount(Recipe recipe) {
        recipe.setIngredientCount(recipe.getIngredientCount() + 1);
        DocumentReference docRef = db.collection("Recipe").document("" + getRecipeName());
        docRef.set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_ingredients);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            setRecipeName(b.getString("clickedRecipe"));
        }

        db = FirebaseFirestore.getInstance();

        ingredientNameEdt = findViewById(R.id.idEdtIngredientName);
        ingredientAmountEdt = findViewById(R.id.idEdtIngredientAmount);
        ingredientUnitEdt = findViewById(R.id.idEdtIngredientUnit);
        submitIngredientBtn = findViewById(R.id.idBtnSubmitIngredient);



        submitIngredientBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ingredientName = ingredientNameEdt.getText().toString();
                ingredientAmountStr = ingredientAmountEdt.getText().toString();
                //ingredientAmount = Double.parseDouble(ingredientAmountEdt.getText().toString());
                ingredientUnit = ingredientUnitEdt.getText().toString();

                //to check if some fields are empty
                boolean error = false;

                if (TextUtils.isEmpty(ingredientName)) {
                    ingredientNameEdt.setError("Please enter ingredient name");
                    error = true;
                } if (TextUtils.isEmpty(ingredientAmountStr)) {
                    ingredientAmountEdt.setError("Please enter ingredient amount");
                    error = true;
                }
                if (TextUtils.isEmpty(ingredientUnit)) {
                    ingredientUnitEdt.setError("Please enter ingredient unit");
                    error = true;
                } if (!error) {
                    Recipe recipe;
                    ingredientAmount = Double.parseDouble(ingredientAmountStr); //convert number string to double
                    DocumentReference docRef = db.collection("Recipe").document("" + getRecipeName());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            addIngredientToFirestore(documentSnapshot.toObject(Recipe.class));
                        }
                    });

                }
            }
        });
    }
}
