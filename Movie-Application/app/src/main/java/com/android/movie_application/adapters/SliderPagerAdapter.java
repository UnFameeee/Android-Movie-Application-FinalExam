package com.android.movie_application.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.movie_application.R;
import com.android.movie_application.models.Movie;
import com.android.movie_application.models.Slide;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Movie> mList;
    public final int limit = 5;
    SlideMovieItemClickListener slideMovieItemClickListener;

    public SliderPagerAdapter(Context mContext, List<Movie> mList, SlideMovieItemClickListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.slideMovieItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return Math.min(mList.size(), limit);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item, null);
        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);
        TextView slideText = slideLayout.findViewById(R.id.slide_title);
        slideText.setText(mList.get(position).getTitle());
        Uri uri = Uri.parse(mList.get(position).getCoverPhoto());
        Glide.with(mContext)
                .load(uri)
                .into(slideImg);

        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                slideMovieItemClickListener.onSlideMovieClick(mList.get(position), slideImg);
            }
        });
        FloatingActionButton btn = slideLayout.findViewById(R.id.float_acc_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                slideMovieItemClickListener.onSlideMovieClick(mList.get(position), slideImg);
            }
        });
        container.addView(slideLayout);
        return slideLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
