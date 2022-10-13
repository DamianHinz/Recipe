package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRecipeDescriptionActivity extends AppCompatActivity{
    private EditText recipeDescriptionEdt;
    private Button saveEditBtn;
    private String recipeName;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit_recipe_description);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String currentUid = mAuth.getCurrentUser().getUid();

        recipeDescriptionEdt = findViewById(R.id.idEdtEditRecipeDescription);
        saveEditBtn = findViewById(R.id.idBtnSaveEditRecipeDescription);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            recipeName = b.getString("clickedRecipe");
        }

        DocumentReference docRef = db.collection("Users").document(currentUid).collection("Recipe").document(recipeName);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recipeDescriptionEdt.setText(documentSnapshot.toObject(Recipe.class).getDescription());
            }
        });


        saveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Recipe recipe = documentSnapshot.toObject(Recipe.class);
                        recipe.setDescription(recipeDescriptionEdt.getText().toString());
                        docRef.set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                            }
                        });
                    }
                });
            }
        });

    }
}
