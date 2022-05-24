package com.android.movie_application.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.movie_application.R;
import com.android.movie_application.ui.SearchedMovieActivity;
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
        rv_movie_search = view.findViewById(R.id.rv_searched_movie);
        List<Movie> lstMovie = new ArrayList<>();
        lstMovie.add(new Movie("Anime", Integer.toString(R.drawable.category_anime)));
        lstMovie.add(new Movie("Animal", Integer.toString(R.drawable.category_anime)));
        lstMovie.add(new Movie("Science", Integer.toString(R.drawable.category_anime)));
        lstMovie.add(new Movie("Food", Integer.toString(R.drawable.category_anime)));
        lstMovie.add(new Movie("Documentary", Integer.toString(R.drawable.category_anime)));
        lstMovie.add(new Movie("Popular", Integer.toString(R.drawable.category_anime)));

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), lstMovie);
        rv_movie_search.setAdapter(categoryAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
////        movieRV_2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_movie_search.setLayoutManager(layoutManager);

        final EditText searchText = (EditText) view.findViewById(R.id.search_input);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                //If the event is a key-down event on the "enter" button
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        //perform action
                        Intent intent = new Intent(getActivity(), SearchedMovieActivity.class);
                        intent.putExtra("searchValue", searchText.getText().toString());
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }
}