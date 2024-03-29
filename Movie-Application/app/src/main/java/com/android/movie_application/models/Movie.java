package com.android.movie_application.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
    private String title;
    private String description;
    private String thumbnail;
    private String category;
    private String coverPhoto;
    public ArrayList<Chapter> chapter;
    public String createdDate;
    private String rating;
    private String streamingLink;


    public Movie() {

    }

    public Movie(String title, String category, String thumbnail, String coverPhoto, String description, ArrayList<Chapter> chapter) {
        this.title = title;
        this.category = category;
        this.chapter = chapter;
        this.thumbnail = thumbnail;
        this.coverPhoto = coverPhoto;
        this.description = description;
    }


    public Movie(String title, String thumbnail) {
        this.thumbnail = thumbnail;
        this.title = title;
    }


    public Movie(String title, String description, String coverPhoto, String thumbnail, String streamingLink, String createdDate)
    {
        this.title = title;
        this.category = category;
        this.description = description;
        this.coverPhoto = coverPhoto;
        this.thumbnail = thumbnail;
        this.streamingLink = streamingLink;
        this.createdDate = createdDate;
    }

    public Movie(String title, String category, String description, String coverPhoto, String thumbnail, String streamingLink, String createdDate)
    {
        this.title = title;
        this.category = category;
        this.description = description;
        this.coverPhoto = coverPhoto;
        this.thumbnail = thumbnail;
        this.streamingLink = streamingLink;
        this.createdDate = createdDate;
    }

    public Movie(String title, String category, String createdDate)
    {
        this.title = title;
        this.category = category;
        this.createdDate = createdDate;
    }

    public ArrayList<Chapter> getChapter() {
        return chapter;
    }

    public void setChapter(ArrayList<Chapter> chapter) {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
