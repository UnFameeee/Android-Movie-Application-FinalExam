package com.android.movie_application.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movie_application.R;
import com.android.movie_application.models.Chapter;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView MovieThumbnailImg, MovieCoverImg;
    private TextView tv_title, tv_description;
    private FloatingActionButton play_fab;
    private String movieTitle, movieCoverPhoto, movieThumbnail, movieDescription;
    private ArrayList<Chapter> chaplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //initiate views
        iniViews();

    }

    void iniViews(){
        //get the data
        movieTitle = getIntent().getExtras().getString("title");
        movieCoverPhoto = getIntent().getExtras().getString("coverPhoto");
        movieThumbnail = getIntent().getExtras().getString("thumbnail");
        movieDescription = getIntent().getExtras().getString("description");
//        ArrayList<Chapter> chaplist = (ArrayList<Chapter>) getIntent().getSerializableExtra("chapterList");
        chaplist = (ArrayList<Chapter>) getIntent().getSerializableExtra("chapterList");

        for(int i = 0; i < chaplist.size(); ++i){
            System.out.println(chaplist.get(i).getTitle());
        }

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
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }
}