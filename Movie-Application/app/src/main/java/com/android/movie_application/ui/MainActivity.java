package com.android.movie_application.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.movie_application.R;
import com.android.movie_application.adapters.SliderPagerAdapter;
import com.android.movie_application.models.Slide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private List<Slide> listslide;
    private ViewPager sliderpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                //                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        sliderpaper = findViewById(R.id.silder_paper);

        listslide = new ArrayList<>();
        listslide.add(new Slide(R.drawable.parasyte_lofi, "Slide Title \nmore text here"));
        listslide.add(new Slide(R.drawable.one_punch_man_lofi, "Slide Title \nmore text here"));
        listslide.add(new Slide(R.drawable.demon_slayer_lofi, "Slide Title \nmore text here"));
        listslide.add(new Slide(R.drawable.my_hero_academy_lofi, "Slide Title \nmore text here"));
        listslide.add(new Slide(R.drawable.fire_force_lofi, "Slide Title \nmore text here"));
        SliderPagerAdapter adapter = new SliderPagerAdapter(this, listslide) ;
        sliderpaper.setAdapter(adapter);
        //Set up time for changing the theme
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainActivity.SliderTimer(), 2000, 4000);
    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    if(sliderpaper.getCurrentItem() < (listslide.size() - 1)){
                        sliderpaper.setCurrentItem(sliderpaper.getCurrentItem()+1);
                    }
                    else
                        sliderpaper.setCurrentItem(0);
                }
            });
        }
    }
    public void OnClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}