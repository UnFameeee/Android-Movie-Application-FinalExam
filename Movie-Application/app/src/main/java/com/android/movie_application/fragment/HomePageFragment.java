package com.android.movie_application.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieAdapter;
import com.android.movie_application.adapters.MovieItemClickListener;
import com.android.movie_application.adapters.SliderPagerAdapter;
import com.android.movie_application.models.Movie;
import com.android.movie_application.models.Slide;
import com.android.movie_application.ui.MainActivity;
import com.android.movie_application.ui.MovieDetailActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageFragment extends Fragment implements MovieItemClickListener {

    private List<Slide> listslide;
    private ViewPager sliderpaper;
    private RecyclerView movieRV;
    private TabLayout indicator;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    List<Movie> lstMovie = new ArrayList<>();
    MovieAdapter movieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        sliderpaper = view.findViewById(R.id.silder_paper);
        listslide = new ArrayList<>();
        listslide.add(new Slide(R.drawable.parasyte_lofi, "Parasyte"));
        listslide.add(new Slide(R.drawable.one_punch_man_lofi, "One Punch Man"));
        listslide.add(new Slide(R.drawable.demon_slayer_lofi, "Demon Slayer"));
        listslide.add(new Slide(R.drawable.my_hero_academy_lofi, "My Hero Academy"));
        listslide.add(new Slide(R.drawable.fire_force_lofi, "Fire Force"));
        SliderPagerAdapter adapter = new SliderPagerAdapter(getActivity(), listslide) ;
        sliderpaper.setAdapter(adapter);
        //Set up time for changing the theme
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 4000);
        indicator = view.findViewById(R.id.indicator);
        indicator.setupWithViewPager(sliderpaper, true);

        //Recyclerview Setup
        movieRV = view.findViewById((R.id.rv_movie));

//        lstMovie.add(new Movie("Demon Slayer", R.drawable.demon_slayer, R.drawable.demon_slayer_lofi));
//        lstMovie.add(new Movie("Black Clover", R.drawable.black_clover));
//        lstMovie.add(new Movie("Dr Stone", R.drawable.dr_stone));
//        lstMovie.add(new Movie("Fairy Tail", R.drawable.fairy_tail));
//        lstMovie.add(new Movie("Fire Force", R.drawable.fire_force, R.drawable.fire_force_lofi));
//        lstMovie.add(new Movie("Sword Oratoria", R.drawable.is_it_wrong_to_try_to_pick_up_girls_in_dungeon));
//        lstMovie.add(new Movie("Jujutsu Kaisen", R.drawable.jujutsu_kaisen, R.drawable.jujutsu_kaisen_lofi));
//        lstMovie.add(new Movie("My Hero Academia", R.drawable.my_hero_academia, R.drawable.my_hero_academy_lofi));
//        lstMovie.add(new Movie("One Punch Man", R.drawable.one_punch_man, R.drawable.one_punch_man_lofi));
//        lstMovie.add(new Movie("Parasyte", R.drawable.parasyte, R.drawable.parasyte_lofi));
//        lstMovie.add(new Movie("Stein Gate", R.drawable.stein_gate));
        getAllMovies();
        movieAdapter = new MovieAdapter(getActivity(), lstMovie, HomePageFragment.this);
        movieRV.setAdapter(movieAdapter);
        movieRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        //Here we send movie information to detail activity
        //also we ll create the transition animation between the two activity
        Log.d("movie cover photo: ", movie.getCoverPhoto());
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
//        intent.putExtra("imgURL", movie.getThumbnail());
//        intent.putExtra("imgCover", movie.getCoverPhoto());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), movieImageView, "sharedName");

        startActivity(intent, options.toBundle());

//        Toast.makeText(getActivity(), "item clicked" + movie.getTitle(), Toast.LENGTH_LONG).show();
    }


    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if(isAdded()){
                getActivity().runOnUiThread(new Runnable(){
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
    }


    private void getAllMovies()
    {
        //Get reference for the Movie node
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Movie");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                lstMovie.add(movie);
                movieAdapter.notifyDataSetChanged();
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