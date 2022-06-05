package com.android.movie_application.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movie_application.R;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView MovieThumbnailImg, MovieCoverImg;
    private TextView tv_title, tv_description;
    private FloatingActionButton play_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //initiate views
        iniViews();

    }

    void iniViews(){
        //get the data
        String movieTitle = getIntent().getExtras().getString("title");
        String movieCoverPhoto = getIntent().getExtras().getString("coverPhoto");
        String movieThumbnail = getIntent().getExtras().getString("thumbnail");

        play_fab = findViewById(R.id.play_fab);
        MovieThumbnailImg = findViewById(R.id.movie_detail_img);
        Glide.with(this).load(movieThumbnail).into(MovieThumbnailImg);
        MovieCoverImg = findViewById(R.id.movie_detail_cover);
        Glide.with(this).load(movieCoverPhoto).into(MovieCoverImg);
        tv_title = findViewById(R.id.movie_detail_title);
        tv_title.setText(movieTitle);
        setTitle(movieTitle);

        tv_description = findViewById(R.id.movie_detail_desc);

        //setup animation
        play_fab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
    }

    public void stream(View view)
    {
        Intent intent = new Intent(MovieDetailActivity.this, StreamVideoActivity.class);
        startActivity(intent);
    }
}