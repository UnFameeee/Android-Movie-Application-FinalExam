package com.android.movie_application.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.movie_application.R;
import com.android.movie_application.models.Movie;

public class PersonalVideo extends AppCompatActivity {

    VideoView videoView;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_video);
        textView = (TextView) findViewById(R.id.textView);
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse(getIntent().getExtras().getString("myVideo"));
        videoView.setVideoURI(uri);
        String title = getIntent().getExtras().getString("title");
        textView.setText(title);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });
    }

}