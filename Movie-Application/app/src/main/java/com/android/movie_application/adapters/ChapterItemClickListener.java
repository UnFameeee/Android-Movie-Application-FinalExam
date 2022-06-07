package com.android.movie_application.adapters;

import android.widget.ImageView;

import com.android.movie_application.models.Chapter;

public interface ChapterItemClickListener {
    void onChapterClick(Chapter chapter, ImageView chapterImageView);
}
