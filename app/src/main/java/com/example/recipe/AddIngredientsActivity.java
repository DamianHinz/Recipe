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

    private String ingredientName, ingredientUnit;

    private double ingredientAmount;

    private FirebaseFirestore db;

    private void addIngredientToFirestore() {
        return;
    }


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.add_ingredients);

        db = FirebaseFirestore.getInstance();

        ingredientNameEdt = findViewById(R.id.idEdtIngredientName);
        ingredientAmountEdt = findViewById(R.id.idEdtIngredientAmount);
        ingredientUnitEdt = findViewById(R.id.idEdtIngredientUnit);
        submitIngredientBtn = findViewById(R.id.idBtnSubmitIngredient);

        submitIngredientBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ingredientName = ingredientNameEdt.getText().toString();
                ingredientAmount = Double.parseDouble(ingredientAmountEdt.getText().toString());
                ingredientUnit = ingredientUnitEdt.getText().toString();

                //to check if some fields are empty
                boolean error = false;

                if (TextUtils.isEmpty(ingredientName)) {
                    ingredientNameEdt.setError("Please enter recipe unit");
                    error = true;
                } if (TextUtils.isEmpty(ingredientUnit)) {
                    ingredientUnitEdt.setError("Please enter ingredient unit");
                    error = true;
                } if (!error) {
                    addIngredientToFirestore();
                }
            }
        });
    }
}
