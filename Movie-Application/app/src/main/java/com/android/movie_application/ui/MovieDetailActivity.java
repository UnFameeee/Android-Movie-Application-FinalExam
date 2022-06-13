package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieAdapter;
import com.android.movie_application.adapters.MovieItemClickListener;
import com.android.movie_application.fragment.HomePageFragment;
import com.android.movie_application.models.Chapter;
import com.android.movie_application.models.Movie;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MovieDetailActivity extends AppCompatActivity implements MovieItemClickListener {

    private ImageView MovieThumbnailImg, MovieCoverImg;
    private TextView tv_title, tv_description;
    private FloatingActionButton play_fab;
    private String movieTitle, movieCategory, movieCoverPhoto, movieThumbnail, movieDescription;
    private ArrayList<Chapter> chaplist;
    List<Movie> lstMovie = new ArrayList<>();
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initiate views
        iniViews();

        //RecycleView Setup
        initiateRV(movieCategory);
    }

    private void initiateRV(String category) {
        RecyclerView movieRV = findViewById((R.id.rv_movie));
        getAllMoviesByCate(movieCategory, lstMovie, tv_title.getText().toString());
        movieAdapter = new MovieAdapter(this, lstMovie, this);
        movieRV.setAdapter(movieAdapter);
        movieRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        //Here we send movie information to detail activity
        //also we ll create the transition animation between the two activity
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("category", movie.getCategory());
        intent.putExtra("thumbnail", movie.getThumbnail());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        intent.putExtra("description", movie.getDescription());

        ArrayList<Chapter> chapList = movie.getChapter();
        intent.putExtra("chapterList", chapList);

//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, movieImageView, "sharedName");
//        startActivity(intent, options.toBundle());

        startActivity(intent);
//        Toast.makeText(getActivity(), "item clicked" + movie.getTitle(), Toast.LENGTH_LONG).show();
    }

    void iniViews(){
        //get the data
        movieTitle = getIntent().getExtras().getString("title");
        movieCategory = getIntent().getExtras().getString("category");
        movieCoverPhoto = getIntent().getExtras().getString("coverPhoto");
        movieThumbnail = getIntent().getExtras().getString("thumbnail");
        movieDescription = getIntent().getExtras().getString("description");
        chaplist = (ArrayList<Chapter>) getIntent().getSerializableExtra("chapterList");

        play_fab = findViewById(R.id.play_fab);
        MovieThumbnailImg = findViewById(R.id.movie_detail_img);
        Glide.with(this).load(movieThumbnail).into(MovieThumbnailImg);
        MovieCoverImg = findViewById(R.id.movie_detail_cover);
        Glide.with(this).load(movieCoverPhoto).into(MovieCoverImg);
        tv_title = findViewById(R.id.movie_detail_title);
        tv_title.setText(movieTitle);
        setTitle(movieTitle);
        tv_description = findViewById(R.id.movie_detail_desc);
        tv_description.setText(movieDescription);

        //setup animation
        play_fab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
    }

    public void stream(View view)
    {
        Intent intent = new Intent(MovieDetailActivity.this, StreamVideoActivity.class);
        intent.putExtra("chapterList", chaplist);
        intent.putExtra("movieTitle", movieTitle);
        startActivity(intent);
    }

    private void getAllMoviesByCate(String category, List<Movie> lstMovie, String titlePresent){
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
                                        if(!Objects.equals(title, titlePresent)){
                                            Movie movie = new Movie(title, category, thumbnail, coverPhoto, description, chapterList);
                                            lstMovie.add(movie);
                                            Collections.shuffle(lstMovie, new Random());
                                            movieAdapter.notifyDataSetChanged();
                                        }
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

}