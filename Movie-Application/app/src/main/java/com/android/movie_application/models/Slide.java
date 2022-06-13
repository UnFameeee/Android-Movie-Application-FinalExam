package com.android.movie_application.models;

import java.util.ArrayList;

public class Slide {
    private String title;
    private String description;
    private String thumbnail;
    private String category;
    private String coverPhoto;
    public ArrayList<Chapter> chapter;

    public Slide(String title, String description, String thumbnail, String category, String coverPhoto, ArrayList<Chapter> chapter) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.category = category;
        this.coverPhoto = coverPhoto;
        this.chapter = chapter;
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

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public ArrayList<Chapter> getChapter() {
        return chapter;
    }

    public void setChapter(ArrayList<Chapter> chapter) {
        this.chapter = chapter;
    }
}
