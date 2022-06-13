package com.android.movie_application.adapters;

import android.widget.ImageView;

import com.android.movie_application.models.Movie;
import com.android.movie_application.models.Slide;

public interface SlideMovieItemClickListener {
    void onSlideMovieClick(Movie movie, ImageView movieImageView);
}
