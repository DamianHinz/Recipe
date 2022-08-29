package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;


public class DashboardActivity extends AppCompatActivity {

    //creating variable for Button
    private Button viewRecipeBtn, toAddRecipeBtn;

    //creating variable for Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //getting instance from Firebase Firestore
        db = FirebaseFirestore.getInstance();

        //initializing buttons
        viewRecipeBtn = findViewById(R.id.idBtnViewRecipe);
        toAddRecipeBtn = findViewById(R.id.idBtnToAddRecipe);

        // adding onclick listener to view data in new activity
        viewRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(DashboardActivity.this, ViewRecipe.class);
                startActivity(i);
            }
        });


        // adding onclick listener to view data in new activity
        toAddRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity on button click
                Intent i = new Intent(DashboardActivity.this, AddRecipeActivity.class);
                startActivity(i);
            }
        });
    }

}