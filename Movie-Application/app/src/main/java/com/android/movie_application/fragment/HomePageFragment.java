package com.android.movie_application.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieAdapter;
import com.android.movie_application.adapters.MovieItemClickListener;
import com.android.movie_application.adapters.SliderPagerAdapter;
import com.android.movie_application.models.Movie;
import com.android.movie_application.models.Slide;
import com.android.movie_application.ui.MovieDetailActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Timer;
import java.util.TimerTask;

public class HomePageFragment extends Fragment implements MovieItemClickListener {

    private List<Slide> listslide;
    private ViewPager sliderpaper;

    List<Movie> lstMovie = new ArrayList<>();
    List<Movie> lstMovie2 = new ArrayList<>();
    List<Movie> lstMovie3 = new ArrayList<>();
    MovieAdapter movieAdapter;
    MovieAdapter movie2Adapter;
    MovieAdapter movie3Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        //Slider paper and Indicator Setup
        initiateSlider(view);

        //Recyclerview Setup
        initiateRV1(view);

        //Recyclerview Setup
        initiateRV2(view);

        //Recyclerview Setup
        initiateRV3(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void initiateSlider(View view) {
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
        //Setup indicator
        TabLayout indicator = view.findViewById(R.id.indicator);
        indicator.setupWithViewPager(sliderpaper, true);
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

    private void initiateRV1(View view) {
        RecyclerView movieRV = view.findViewById((R.id.rv_movie));
        getAllMovies("Anime", lstMovie);
        movieAdapter = new MovieAdapter(getActivity(), lstMovie, HomePageFragment.this);
        movieRV.setAdapter(movieAdapter);
        movieRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initiateRV2(View view) {
        RecyclerView movieRV2 = view.findViewById((R.id.rv_movie2));
        getAllMovies("Anime", lstMovie2);
        movie2Adapter = new MovieAdapter(getActivity(), lstMovie2, HomePageFragment.this);
        movieRV2.setAdapter(movie2Adapter);
        movieRV2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initiateRV3(View view) {
        RecyclerView movieRV3 = view.findViewById((R.id.rv_movie3));
        getAllMovies("Anime", lstMovie3);
        movie3Adapter = new MovieAdapter(getActivity(), lstMovie3, HomePageFragment.this);
        movieRV3.setAdapter(movie3Adapter);
        movieRV3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        //Here we send movie information to detail activity
        //also we ll create the transition animation between the two activity
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("thumbnail", movie.getThumbnail());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        intent.putExtra("description", movie.getDescription());
        intent.putExtra("chapter",(Serializable) movie.getChapter());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), movieImageView, "sharedName");
        startActivity(intent, options.toBundle());
//        Toast.makeText(getActivity(), "item clicked" + movie.getTitle(), Toast.LENGTH_LONG).show();
    }

    private void getAllMovies(String category, List<Movie> lstMovie){
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
                            for(DataSnapshot movieId : dataSnapshot.child(key).child("movies").getChildren()) {
                                String movieKey = movieId.getKey();
                                assert movieKey != null;

                                ArrayList<String> chapter = new ArrayList<>();
                                String coverPhoto = "", description = "", thumbnail = "", title;

                                for(DataSnapshot movieDetail : dataSnapshot.child(key).child("movies").child(movieKey).getChildren()) {

                                    if((Objects.equals(movieDetail.getKey(), "chapter"))){
                                        for(DataSnapshot movieChapter : dataSnapshot.child(key).child("movies").child(movieKey).child("chapter").getChildren()) {
                                            chapter.add(movieChapter.getValue(String.class));
                                        }
                                    } else if((Objects.equals(movieDetail.getKey(), "coverPhoto"))){
                                        coverPhoto = movieDetail.getValue(String.class);
                                    }else if((Objects.equals(movieDetail.getKey(), "description"))) {
                                        description = movieDetail.getValue(String.class);
                                    }else if((Objects.equals(movieDetail.getKey(), "thumbnail"))){
                                        thumbnail = movieDetail.getValue(String.class);
                                    } else if((Objects.equals(movieDetail.getKey(), "title"))){
                                        title = movieDetail.getValue(String.class);
                                        Movie movie = new Movie(title, chapter, thumbnail, coverPhoto, description);
                                        lstMovie.add(movie);
                                        Collections.shuffle(lstMovie, new Random());
                                        movieAdapter.notifyDataSetChanged();
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


//    FirebaseDatabase firebaseDatabase;
//    FirebaseAuth firebaseAuth;
//    DatabaseReference databaseReference;
//    private void getAllMovies()
//    {
//        //Get reference for the Movie node
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("Movie");
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Movie movie = snapshot.getValue(Movie.class);
//                lstMovie.add(movie);
//                movieAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
