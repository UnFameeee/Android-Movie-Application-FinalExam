package com.android.movie_application.models;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String title;
    private String thumbnail;
    private String data;

    public Chapter() {
    }

    public Chapter(String title, String thumbnail, String data) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.data = data;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
