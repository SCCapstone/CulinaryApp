package com.github.CulinaryApp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.CulinaryApp.R;

public class RecipeInstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.recipeName);
        textView.setText("Alfredo Shrimp Pasta");
        TextView directions = findViewById(R.id.directionstextViewID);
        directions.setText("Step 1:\nStep 2:\nStep 3:\nStep 4");
        TextView ingredients = findViewById(R.id.textViewIngredients);
        ingredients.setText("Ingredient 1:\nIngredient 2:\nIngredient 3:\nIngredient 4");

    }
}