package com.github.CulinaryApp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {
    EditText search_edit_text;
    RecyclerView recycler_view;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> categories;
    ArrayList<String> categoriesImages;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category);

        search_edit_text = findViewById(R.id.search_edit_text);
        recycler_view = findViewById(R.id.recycler_view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        categories = new ArrayList<>();
        categoriesImages = new ArrayList<>();

        //Wait for text to be changed in the search bar
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //set the adapter in search adapter to be the new string to load new recycler view
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }
            }
        });

    }

    private void setAdapter(String s){


        //Look for search within categories
        databaseReference.child("CATEGORIES").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //reset search criterea each time
                categories.clear();
                categoriesImages.clear();
                recycler_view.removeAllViews();

                //counter to only allow 16 recipes to be loaded at a time
                int counter = 0;
                //loop through categories
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String CID = snapshot1.getKey();
                    String categoryName = snapshot1.child("CATEGORIES").getValue(String.class);
                    //check if there is a category with the string as its name
                    if(categoryName.contains(s)){
                        //add the category name to a list to be pulled from for loading
                        categories.add(categoryName);
                        categoriesImages.add(categoryName);
                        counter++;
                    }

                    if(counter == 16){
                        break;
                    }
                }

                //make the new recycler view
                searchAdapter = new SearchAdapter(SearchableActivity.this, categories, categoriesImages);
                recycler_view.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
