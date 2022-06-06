package com.android.movie_application.adapters;

import android.widget.ImageView;

import com.android.movie_application.models.Category;
public interface CategoryItemClickListener {
    void onCateClick(Category category, ImageView movieImageView);
}
