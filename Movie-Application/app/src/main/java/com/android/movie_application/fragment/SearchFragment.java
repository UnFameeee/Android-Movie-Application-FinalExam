package com.android.movie_application.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.CategoryItemClickListener;
import com.android.movie_application.models.Category;
import com.android.movie_application.ui.MovieDetailActivity;
import com.android.movie_application.ui.SearchedMovieActivity;
import com.android.movie_application.adapters.CategoryAdapter;
import com.android.movie_application.models.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements CategoryItemClickListener {

    private RecyclerView rv_movie_search;
    CategoryAdapter categoryAdapter;
    List<Category> lstMovie = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initiateRV(view);

        final EditText searchText = (EditText) view.findViewById(R.id.search_input);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                //If the event is a key-down event on the "enter" button
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        //perform action
                        Intent intent = new Intent(getActivity(), SearchedMovieActivity.class);
                        intent.putExtra("searchValue", searchText.getText().toString());
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void initiateRV(View view) {
        //Recyclerview Setup
        rv_movie_search = view.findViewById(R.id.rv_searched_movie);

        getAllCategory();

        categoryAdapter = new CategoryAdapter(getActivity(), lstMovie, SearchFragment.this);
        rv_movie_search.setAdapter(categoryAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        rv_movie_search.setLayoutManager(layoutManager);
    }

    @Override
    public void onCateClick(Category category, ImageView cateImageView){
        //perform action
        Intent intent = new Intent(getActivity(), SearchedMovieActivity.class);
        intent.putExtra("searchValue", category.getTitle());
        startActivity(intent);
    }

    private void getAllCategory(){
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
                            lstMovie.add(category);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}