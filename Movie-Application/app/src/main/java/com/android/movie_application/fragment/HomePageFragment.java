package com.android.movie_application.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.movie_application.R;
import com.android.movie_application.adapters.MovieAdapter;
import com.android.movie_application.adapters.MovieItemClickListener;
import com.android.movie_application.adapters.SlideMovieItemClickListener;
import com.android.movie_application.adapters.SliderPagerAdapter;
import com.android.movie_application.models.Chapter;
import com.android.movie_application.models.Movie;
import com.android.movie_application.models.Slide;
import com.android.movie_application.ui.MovieDetailActivity;
import com.android.movie_application.utils.CategorySingleton;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Timer;
import java.util.TimerTask;

public class HomePageFragment extends Fragment implements MovieItemClickListener, SlideMovieItemClickListener {
    private ViewPager sliderpaper;

    SliderPagerAdapter adapter;
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

        //Recyclerview Setup
        initiateRV1(view);

        //Recyclerview Setup
        initiateRV2(view);

        //Recyclerview Setup
        initiateRV3(view);

        //Slider pager and Indicator Setup
        initiateSlider(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void initiateSlider(View view) {
        sliderpaper = view.findViewById(R.id.slider_paper);
        adapter = new SliderPagerAdapter(getActivity(), lstMovie2, this) ;
        sliderpaper.setAdapter(adapter);
        //Set up time for changing the theme
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 4000);
        //Setup indicator
        TabLayout indicator = view.findViewById(R.id.indicator);
        indicator.setupWithViewPager(sliderpaper, true);
    }

    @Override
    public void onSlideMovieClick(Movie movie, ImageView movieImageView) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("category", movie.getCategory());
        intent.putExtra("thumbnail", movie.getThumbnail());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        intent.putExtra("description", movie.getDescription());

        ArrayList<Chapter> chapList = movie.getChapter();
        intent.putExtra("chapterList", chapList);
        startActivity(intent);
    }

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            if(isAdded()){
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        if(lstMovie2 == null) return;
                        if(sliderpaper.getCurrentItem() < (adapter.limit - 1)){
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
//        ArrayList<String> cate = new ArrayList<>(Arrays.asList("Anime", "Lofi", "Gamejams", "Devlog", "Review"));
        List<String> cateSingle = CategorySingleton.getInstance().lstCateSingleton;

        TextView tv = view.findViewById(R.id.tvCate);

        if(cateSingle.size() != 0){
            tv.setVisibility(View.VISIBLE);
            System.out.println("in: " + cateSingle.get(0));
            Collections.shuffle(cateSingle, new Random());
            tv.setText(cateSingle.get(0));
            getAllMoviesByCate(cateSingle.get(0), lstMovie);

            System.out.println("in: " + lstMovie);
        }else {
            tv.setVisibility(View.GONE);
        }

        movieAdapter = new MovieAdapter(getActivity(), lstMovie,HomePageFragment.this, 0);
        movieRV.setAdapter(movieAdapter);
        movieRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        System.out.println("out: " + cateSingle.size());
    }

    private void initiateRV2(View view) {
        RecyclerView movieRV2 = view.findViewById((R.id.rv_movie2));
        getAllMovies(lstMovie2);
        movie2Adapter = new MovieAdapter(getActivity(), lstMovie2, HomePageFragment.this, 0);
        movieRV2.setAdapter(movie2Adapter);
        movieRV2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initiateRV3(View view) {
        RecyclerView movieRV3 = view.findViewById((R.id.rv_movie3));
        getAllMovies(lstMovie3);
        movie3Adapter = new MovieAdapter(getActivity(), lstMovie3, HomePageFragment.this, 10);
        movieRV3.setAdapter(movie3Adapter);
        movieRV3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        //Here we send movie information to detail activity
        //also we ll create the transition animation between the two activity
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("category", movie.getCategory());
        intent.putExtra("thumbnail", movie.getThumbnail());
        intent.putExtra("coverPhoto",movie.getCoverPhoto());
        intent.putExtra("description", movie.getDescription());

        ArrayList<Chapter> chapList = movie.getChapter();
        intent.putExtra("chapterList", chapList);
        startActivity(intent);
    }

    private void getAllMoviesByCate(String category, List<Movie> lstMovie){
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
                                        Movie movie = new Movie(title, category,thumbnail, coverPhoto, description, chapterList);
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

    private void getAllMovies(List<Movie> lstMovie){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Database")
                .child("Category");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot idSnap : dataSnapshot.getChildren()) {
                    String key = idSnap.getKey();
                    assert key != null;
                    for(DataSnapshot cateDS : dataSnapshot.child(key).getChildren()) {
                        if(Objects.equals(cateDS.getKey(), "title") ){
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
                                        lstMovie.add(movie);

                                        Collections.shuffle(lstMovie, new Random());
                                        movie2Adapter.notifyDataSetChanged();
                                        movie3Adapter.notifyDataSetChanged();
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}