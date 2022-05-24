package com.android.movie_application.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.movie_application.R;
import com.android.movie_application.adapters.CategoryAdapter;
import com.android.movie_application.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView rv_movie_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //Recyclerview Setup
        rv_movie_search = view.findViewById(R.id.rv_movie_search);
        List<Movie> lstMovie = new ArrayList<>();
//        lstMovie.add(new Movie("Anime", R.drawable.category_anime));
//        lstMovie.add(new Movie("Animal", R.drawable.category_anime));
//        lstMovie.add(new Movie("Science", R.drawable.category_anime));
//        lstMovie.add(new Movie("Food", R.drawable.category_anime));
//        lstMovie.add(new Movie("Documentary", R.drawable.category_anime));
//        lstMovie.add(new Movie("Popular", R.drawable.category_anime));

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), lstMovie);
        rv_movie_search.setAdapter(categoryAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
////        movieRV_2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_movie_search.setLayoutManager(layoutManager);
        return view;
    }
}