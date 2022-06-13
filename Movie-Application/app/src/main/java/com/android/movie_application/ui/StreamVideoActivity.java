package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.ChapterAdapter;
import com.android.movie_application.adapters.ChapterItemClickListener;
import com.android.movie_application.models.Chapter;

import java.util.ArrayList;

public class StreamVideoActivity extends AppCompatActivity implements ChapterItemClickListener {

    private VideoView videoView;
    ArrayList<Chapter> chaplist;
    int chapterPointer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_video_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        chaplist = (ArrayList<Chapter>) getIntent().getSerializableExtra("chapterList");
        String movieName = getIntent().getExtras().getString("movieTitle");

        if(chapterPointer == 0)
            initiateRV(chaplist, 0);

        TextView movieTitle = findViewById(R.id.textViewTitle);
        movieTitle.setText(movieName);

        videoView = findViewById(R.id.videoViewMain);

        String videoPath = chaplist.get(0).getData();
        videoView.setVideoPath(videoPath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chapterPointer < chaplist.size()-1){
                    videoView.setVisibility(View.GONE);
                    chapterPointer++;
                    initiateRV(chaplist, chapterPointer);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(chaplist.get(chapterPointer).getData());
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
                    initiateRV(chaplist, chapterPointer);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(chaplist.get(chapterPointer).getData());
                    videoView.start();
                }
                //Handle previous click here
            }
        });
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }

    private void initiateRV(ArrayList<Chapter> chaplist, int index) {
        RecyclerView rv_chapter = findViewById(R.id.rv_chapter);
        ChapterAdapter chapterAdapter = new ChapterAdapter(this, chaplist, index,this);
        rv_chapter.setAdapter(chapterAdapter);
        rv_chapter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onChapterClick(Chapter chapter, ImageView chapterImageView) {
        chapterPointer = chaplist.indexOf(chapter);
        initiateRV(chaplist, chapterPointer);
        videoView.setVisibility(View.GONE);
        videoView.setVideoPath(chaplist.get(chapterPointer).getData());
        videoView.setVisibility(View.VISIBLE);
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