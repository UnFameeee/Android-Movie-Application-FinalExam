package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieAdapter;
import com.android.movie_application.adapters.MovieTableAdapter;
import com.android.movie_application.adapters.SelectTableItemListener;
import com.android.movie_application.models.Chapter;
import com.android.movie_application.models.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ViewMovieItems extends AppCompatActivity implements SelectTableItemListener
{
    List<Movie> lstMovieData = new ArrayList<>();
    List<Movie> lstMovieData2 = new ArrayList<>();
    RecyclerView recyclerView;
    MovieTableAdapter movieTableAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie_items);
        getAllMovies(lstMovieData);
    }


    private void getAllMovies(List<Movie> lstMovie)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Database").child("Category");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshotCategoryId : snapshot.getChildren())
                {
                    String keyCategory = dataSnapshotCategoryId.getKey();
                    for (DataSnapshot dataSnapshotCategoryChild : snapshot.child(keyCategory).getChildren())
                    {
                        String keyCategoryChild = dataSnapshotCategoryChild.getKey();
                        if(Objects.equals(keyCategoryChild, "title"))
                        {
                            String category = dataSnapshotCategoryChild.getValue(String.class);
                            for (DataSnapshot dataSnapshotMovieId : snapshot.child(keyCategory).child("movies").getChildren())
                            {
                                String keyMovie = dataSnapshotMovieId.getKey();
                                String title = "", createdDate = "", coverPhoto ="", description ="", thumbnail ="", streamingLink ="";
                                for (DataSnapshot dataSnapshotMovieInformation : snapshot.child(keyCategory).child("movies").child(keyMovie).getChildren())
                                {
                                    if((Objects.equals(dataSnapshotMovieInformation.getKey(), "createdDate")))
                                    {
                                        createdDate = dataSnapshotMovieInformation.getValue(String.class);
                                    }
                                    else if((Objects.equals(dataSnapshotMovieInformation.getKey(), "title")))
                                    {
                                        title = dataSnapshotMovieInformation.getValue(String.class);
                                    }
                                    else if((Objects.equals(dataSnapshotMovieInformation.getKey(), "coverPhoto")))
                                    {
                                        coverPhoto = dataSnapshotMovieInformation.getValue(String.class);
                                    }
                                    else if((Objects.equals(dataSnapshotMovieInformation.getKey(), "description")))
                                    {
                                        description = dataSnapshotMovieInformation.getValue(String.class);
                                    }
                                    else if((Objects.equals(dataSnapshotMovieInformation.getKey(), "thumbnail")))
                                    {
                                        thumbnail = dataSnapshotMovieInformation.getValue(String.class);
                                    }
                                    else if((Objects.equals(dataSnapshotMovieInformation.getKey(), "streamingLink")))
                                    {
                                        streamingLink = dataSnapshotMovieInformation.getValue(String.class);
                                    }
                                    if (!title.isEmpty() && !createdDate.isEmpty() && !coverPhoto.isEmpty() && !description.isEmpty() && !thumbnail.isEmpty())
                                    {
//                                        Movie movie = new Movie(title, category, createdDate);
//                                        lstMovieData.add(movie);
                                        Movie movie = new Movie(title, category, description, coverPhoto, thumbnail, streamingLink, createdDate);
                                        lstMovieData.add(movie);
                                    }
                                }
                            }
                        }


                    }

                }

//                Log.d("my list of movies: ", String.valueOf(lstMovie));
                fillTableWithListMovies(lstMovie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void fillTableWithListMovies(List<Movie> lstMovie)
    {
        recyclerView = findViewById(R.id.recyclerViewListMovies);
        setRecylerView(recyclerView);
    }

    private void setRecylerView(RecyclerView recyclerView)
    {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieTableAdapter = new MovieTableAdapter(this, lstMovieData, this);
        movieTableAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(movieTableAdapter);
    }

    @Override
    public void onItemClick(Movie movie)
    {
//        Toast.makeText(this, movie.getTitle(), Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(ViewMovieItems.this,MovieManagement.class);
          intent.putExtra("myMovie", movie);
          startActivity(intent);
    }
}



