package com.android.movie_application.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private String thumbnail;
    private String category;
    private String rating;
    private String streamingLink;
    private String coverPhoto;

    public ArrayList<String> chapter;

    public Movie()
    {

    }

//    public Movie(String title, String thumbnail, String coverPhoto) {
//        this.title = title;
//        this.thumbnail = thumbnail;
//        this.coverPhoto = coverPhoto;
//    }

//    public Movie(String title, List<String> chapter, String thumbnail, String coverPhoto) {
//        this.title = title;
//        this.chapter = chapter;
//        this.thumbnail = thumbnail;
//        this.coverPhoto = coverPhoto;
//    }

    public Movie(String title, ArrayList<String> chapter, String thumbnail, String coverPhoto, String description) {
        this.title = title;
        this.chapter = chapter;
        this.thumbnail = thumbnail;
        this.coverPhoto = coverPhoto;
        this.description = description;
    }


    public Movie(String title, String thumbnail)
    {
        this.thumbnail = thumbnail;
        this.title = title;
    }


//    public Movie(String title, String thumbnail, String coverPhoto, String category) {
//        this.title = title;
//        this.thumbnail = thumbnail;
//        this.coverPhoto = coverPhoto;
//        this.category = category;
//    }


    public Movie(String title, String description, String coverPhoto, String thumbnail, String streamingLink)
    {
        this.title = title;
        this.description = description;
        this.coverPhoto = coverPhoto;
        this.thumbnail = thumbnail;
        this.streamingLink = streamingLink;
    }


    public ArrayList<String> getChapter() {
        return chapter;
    }

    public void setChapter(ArrayList<String> chapter) {
        this.chapter = chapter;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStreamingLink() {
        return streamingLink;
    }

    public void setStreamingLink(String streamingLink) {
        this.streamingLink = streamingLink;
    }
}
