package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieItemClickListener;
import com.android.movie_application.adapters.SearchedMovieAdapter;
import com.android.movie_application.models.Chapter;
import com.android.movie_application.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
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

public class SearchedMovieActivity extends AppCompatActivity implements MovieItemClickListener {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    //var for get movie view
    SearchedMovieAdapter searchedMovieAdapter;
    List<Movie> lstMovieShow = new ArrayList<>();
    String transferredData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_movie);
        getSupportActionBar().hide();

        //var from getting search value
        Intent intent = getIntent();
        transferredData = intent.getExtras().getString("searchValue");

        //Recyclerview Setup
        //initiate var
        RecyclerView rv_searched_movie = findViewById(R.id.rv_chapter);

        //Change title
        TextView title = findViewById(R.id.search_title);
        title.setText(transferredData);
        transferredData = transferredData.substring(0,1).toUpperCase() + transferredData.substring(1).toLowerCase();


        //Get data up from firebase and start searching for category
        getAllMovies(transferredData);
        searchedMovieAdapter = new SearchedMovieAdapter(this, lstMovieShow, this);
        rv_searched_movie.setAdapter(searchedMovieAdapter);
        rv_searched_movie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        //Here we send movie information to detail activity
        //also we ll create the transition animation between the two activity
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("category", movie.getCategory());
        intent.putExtra("thumbnail", movie.getThumbnail());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        intent.putExtra("description", movie.getDescription());
        ArrayList<Chapter> chapList = movie.getChapter();
        intent.putExtra("chapterList", chapList);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, movieImageView, "sharedName");
        startActivity(intent, options.toBundle());
    }

    private void getAllMovies(String category){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Database")
                .child("Category");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot idSnap : dataSnapshot.getChildren()) {
                    String key = idSnap.getKey();
                    assert key != null;
                    for(DataSnapshot cateDS : dataSnapshot.child(key).getChildren()) {
                        if(Objects.equals(cateDS.getKey(), "title") && Objects.equals(cateDS.getValue(String.class), category)){
                            String category = cateDS.getValue(String.class);
                            for(DataSnapshot movieId : dataSnapshot.child(key).child("movies").getChildren()) {
                                String movieKey = movieId.getKey();
                                assert movieKey != null;

                                ArrayList<Chapter> chapterList = new ArrayList<>();
                                String coverPhoto = "", description = "", thumbnail = "", title;

                                for(DataSnapshot movieDetail : dataSnapshot.child(key).child("movies").child(movieKey).getChildren()) {

                                    if((Objects.equals(movieDetail.getKey(), "chapter"))){
                                        for(DataSnapshot movieChapter : dataSnapshot.child(key).child("movies").child(movieKey).child("chapter").getChildren()) {
                                            String chapterKey = movieChapter.getKey();
                                            assert chapterKey != null;

                                            String chapterTitle = "", chapterThumbnail = "", chapterData = "";
                                            for(DataSnapshot chapterDetail : dataSnapshot.child(key).child("movies").child(movieKey).child("chapter").child(chapterKey).getChildren()) {

                                                if((Objects.equals(chapterDetail.getKey(), "data"))){
                                                    chapterData = chapterDetail.getValue(String.class);
                                                }else if((Objects.equals(chapterDetail.getKey(), "thumbnail"))) {
                                                    chapterThumbnail = chapterDetail.getValue(String.class);
                                                }else if((Objects.equals(chapterDetail.getKey(), "title"))) {
                                                    chapterTitle = chapterDetail.getValue(String.class);
                                                }
                                            }
                                            chapterList.add(new Chapter(chapterTitle, chapterThumbnail, chapterData));
                                        }
                                    } else if((Objects.equals(movieDetail.getKey(), "coverPhoto"))){
                                        coverPhoto = movieDetail.getValue(String.class);
                                    }else if((Objects.equals(movieDetail.getKey(), "description"))) {
                                        description = movieDetail.getValue(String.class);
                                    }else if((Objects.equals(movieDetail.getKey(), "thumbnail"))){
                                        thumbnail = movieDetail.getValue(String.class);
                                    } else if((Objects.equals(movieDetail.getKey(), "title"))){
                                        title = movieDetail.getValue(String.class);
                                        Movie movie = new Movie(title, category, thumbnail, coverPhoto, description, chapterList);
                                        lstMovieShow.add(movie);
                                        Collections.shuffle(lstMovieShow, new Random());
                                        searchedMovieAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Back button
    public void back(View view){
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }
}