package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class MainActivity extends AppCompatActivity {

    //creating variable for Button
    private Button viewRecipeBtn, toAddRecipeBtn;



    //creating variable for Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting instance from Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //initializing our edittext and button
        viewRecipeBtn = findViewById(R.id.idBtnViewRecipe);
        toAddRecipeBtn = findViewById(R.id.idBtnToAddRecipe);

        // adding onclick listener to view data in new activity
        viewRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this,ViewRecipe.class);
                startActivity(i);
            }
        });


        // adding onclick listener to view data in new activity
        toAddRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(i);
            }
        });
    }

}
