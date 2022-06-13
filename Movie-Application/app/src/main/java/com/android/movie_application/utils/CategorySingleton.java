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

public class CategorySingleton {

    private CategorySingleton(){
    }

    public static CategorySingleton getInstance(){
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper{
        private static final CategorySingleton INSTANCE = new CategorySingleton();
    }

    public List<Category> getAllCategory(){
        List<Category> lstCateSingleton = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Database")
                .child("Category");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot idSnap : dataSnapshot.getChildren()) {
                    String key = idSnap.getKey();
                    assert key != null;

                    String thumbnail = "", title;

                    for(DataSnapshot cateDS : dataSnapshot.child(key).getChildren()) {
                        if((Objects.equals(cateDS.getKey(), "thumbnail"))){
                            thumbnail = cateDS.getValue(String.class);
                        } else if((Objects.equals(cateDS.getKey(), "title"))){
                            title = cateDS.getValue(String.class);
                            Category category = new Category(title, thumbnail);
                            lstCateSingleton.add(category);
                            Collections.shuffle(lstCateSingleton, new Random());
//                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lstCateSingleton;
    }
}
