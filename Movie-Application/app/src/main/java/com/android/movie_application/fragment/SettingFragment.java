package com.android.movie_application.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.movie_application.R;
import com.android.movie_application.ui.AddNewMovie;
import com.android.movie_application.ui.ViewMovieItems;

public class SettingFragment extends Fragment {

    String role ="";
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView textViewAddMovie, textViewYourMovies, textViewAddChapter;
        textViewAddMovie = (TextView) view.findViewById(R.id.textViewAddMovie);
        textViewYourMovies = (TextView) view.findViewById(R.id.textViewListMovies);
        textViewAddChapter = (TextView) view.findViewById(R.id.textViewAddChapter);
        textViewAddMovie.setVisibility(View.INVISIBLE);
        textViewYourMovies.setVisibility(View.INVISIBLE);
        textViewAddChapter.setVisibility(View.INVISIBLE);
        if (getArguments() != null)
        {
            role = getArguments().getString("role");
        }
        if (role.equals("admin"))
        {
            textViewAddMovie.setVisibility(View.VISIBLE);
            textViewYourMovies.setVisibility(View.VISIBLE);
            textViewAddChapter.setVisibility(View.VISIBLE);
        }

        textViewAddMovie.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Intent intent = new Intent(getContext(), AddNewMovie.class);
               startActivity(intent);
           }
       });


        textViewYourMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewMovieItems.class);
                startActivity(intent);
            }
        });

//        return inflater.inflate(R.layout.fragment_setting, container, false);
        return view;

    }
}