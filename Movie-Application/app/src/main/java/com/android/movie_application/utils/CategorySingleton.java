package com.android.movie_application.utils;

import androidx.annotation.NonNull;

import com.android.movie_application.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class CategorySingleton {
    public List<String> lstCateSingleton = new ArrayList<>();

    private CategorySingleton(){
        getAllCategory();
    }

    public static CategorySingleton getInstance(){
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper{
        private static final CategorySingleton INSTANCE = new CategorySingleton();
    }

    public void getAllCategory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Database")
                .child("Category");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot idSnap : dataSnapshot.getChildren()) {
                    String key = idSnap.getKey();
                    assert key != null;

                    String title;

                    for (DataSnapshot cateDS : dataSnapshot.child(key).getChildren()) {
                            if ((Objects.equals(cateDS.getKey(), "title"))) {
                            title = cateDS.getValue(String.class);
                            lstCateSingleton.add(title);
                            Collections.shuffle(lstCateSingleton, new Random());
//                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
