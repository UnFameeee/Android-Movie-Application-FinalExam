package com.android.movie_application.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movie_application.R;
import com.android.movie_application.models.Movie;
import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    Context context;
    List<Movie> mData;

    MovieItemClickListener movieItemClickListener;

    public MovieAdapter(Context context, List<Movie> mData, MovieItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        this.movieItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.TvTitle.setText(mData.get(position).getTitle());
        Uri uri = Uri.parse(mData.get(position).getThumbnail());
//        myViewHolder.ImgMovie.setImageURI(uri);
        Glide.with(context)
                .load(uri)
                .into(myViewHolder.ImgMovie);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView TvTitle;
        private ImageView ImgMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            TvTitle = itemView.findViewById(R.id.movie_item_title);
            ImgMovie = itemView.findViewById(R.id.movie_item_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieItemClickListener.onMovieClick(mData.get(getAdapterPosition()), ImgMovie);
                }
            });
        }
    }
}
