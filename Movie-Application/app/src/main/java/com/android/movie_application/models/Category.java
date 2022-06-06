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

//    public Category(String title, String thumbnail, List<Movie> movies) {
//        this.title = title;
//        this.thumbnail = thumbnail;
//        this.movies = movies;
//    }


    public Category(String title, String thumbnail, String movies) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.movies = movies;
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

//    public List<Movie> getMovies() {
//        return movies;
//    }
//
//    public void setMovies(List<Movie> movies) {
//        this.movies = movies;
//    }


    public String getMovies() {
        return movies;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }
}
