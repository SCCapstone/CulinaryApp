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

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }
            }
        });

    }

    private void setAdapter(String s){


        databaseReference.child("CATEGORIES").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                categories.clear();
                categoriesImages.clear();
                recycler_view.removeAllViews();

                int counter = 0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String CID = snapshot1.getKey();
                    String categoryName = snapshot1.child("CATEGORIES").getValue(String.class);
                    if(categoryName.contains(s)){
                        categories.add(categoryName);
                        categoriesImages.add(categoryName);
                        counter++;
                    }

                    if(counter == 15){
                        break;
                    }
                }

                searchAdapter = new SearchAdapter(SearchableActivity.this, categories, categoriesImages);
                recycler_view.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
