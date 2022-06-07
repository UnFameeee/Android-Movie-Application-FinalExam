package com.android.movie_application.models;

import java.util.List;

public class Category {
    private String key;
    private String title;
    private String thumbnail;
//    private List<Movie> movies;
    private String movies;

    public Category() {
    }

    public Category(String key)
    {
        this.key = key;
    }
    public Category(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) { this.key = key; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMovies() {
        return movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }
}
