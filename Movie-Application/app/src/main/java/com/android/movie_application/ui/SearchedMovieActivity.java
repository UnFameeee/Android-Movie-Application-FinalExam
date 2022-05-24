package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieItemClickListener;
import com.android.movie_application.adapters.SearchedMovieAdapter;
import com.android.movie_application.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchedMovieActivity extends AppCompatActivity implements MovieItemClickListener {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    //var for get movie view
    List<Movie> lstMovie = new ArrayList<>();
    SearchedMovieAdapter searchedMovieAdapter;
    List<Movie> lstMovieShow = new ArrayList<>();
    String transferredData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_movie);

        //var from getting search value
        Intent intent = getIntent();
        transferredData = intent.getExtras().getString("searchValue");

        //Recyclerview Setup
        //initiate var
        RecyclerView rv_searched_movie = findViewById(R.id.rv_searched_movie);

        //Get data up from firebase and start searching for category
        getAllMovies();
        searchedMovieAdapter = new SearchedMovieAdapter(this, lstMovieShow, this);
        rv_searched_movie.setAdapter(searchedMovieAdapter);
        rv_searched_movie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        //Here we send movie information to detail activity
        //also we ll create the transition animation between the two activity
        Log.d("movie cover photo: ", movie.getCoverPhoto());
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("thumbnail", movie.getThumbnail());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, movieImageView, "sharedName");

        startActivity(intent, options.toBundle());

//        Toast.makeText(getActivity(), "item clicked" + movie.getTitle(), Toast.LENGTH_LONG).show();
    }

    private void getAllMovies()
    {
        //Get reference for the Movie node
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Movie");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if(Objects.equals(movie.getCategory(), transferredData))
                    lstMovieShow.add(movie);
                searchedMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}