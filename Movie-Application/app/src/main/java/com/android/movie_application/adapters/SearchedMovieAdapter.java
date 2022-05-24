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

import java.util.List;

public class SearchedMovieAdapter extends RecyclerView.Adapter<SearchedMovieAdapter.MyViewHolder> {
    Context context;
    List<Movie> mData;

    MovieItemClickListener movieItemClickListener;

    public SearchedMovieAdapter(Context context, List<Movie> mData, MovieItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        this.movieItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position){
        myViewHolder.tvTitle.setText(mData.get(position).getTitle());
//        myViewHolder.imgMovie.setImageResource(Integer.parseInt(mData.get(position).getCoverPhoto()));
        Uri uri = Uri.parse(mData.get(position).getCoverPhoto());
        Glide.with(context)
                .load(uri)
                .into(myViewHolder.imgMovie);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private ImageView imgMovie;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.movie_search_item_title);
            imgMovie = itemView.findViewById(R.id.movie_search_item_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieItemClickListener.onMovieClick(mData.get(getAdapterPosition()), imgMovie);
                }
            });
        }
    }
}
