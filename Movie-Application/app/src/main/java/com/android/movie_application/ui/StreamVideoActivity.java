package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.android.movie_application.R;

import java.util.ArrayList;

public class StreamVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private String videoPath;
    ArrayList<String> chapter;
    int chapterPointer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_video_activity);

        chapter = getIntent().getExtras().getStringArrayList("chapter");

        videoView = findViewById(R.id.videoViewMain);

        videoPath = chapter.get(0);
        videoView.setVideoPath(videoPath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chapterPointer < chapter.size()-1){
                    videoView.setVisibility(View.GONE);
                    chapterPointer++;
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(chapter.get(chapterPointer));
                    videoView.start();
                }
                //Handle next click here
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chapterPointer > 0){
                    videoView.setVisibility(View.GONE);
                    chapterPointer--;
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(chapter.get(chapterPointer));
                    videoView.start();
                }
                //Handle previous click here
            }
        });
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RelativeLayout relativeLayout =  findViewById(R.id.relativeLayout);
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            RelativeLayout relativeLayout =  findViewById(R.id.relativeLayout);
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (250 * (getResources().getDisplayMetrics().density))));
        }
    }



    @Override
    public void onBackPressed() {
        StreamVideoActivity.this.finish();
    }
}