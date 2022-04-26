package com.android.movie_application.adapters;

import android.widget.ImageView;

import com.android.movie_application.models.Movie;

public interface MovieItemClickListener {
    void onMovieClick(Movie movie, ImageView movieImageView); //We will need the ImageView to make the shared animation between the two activity
}
