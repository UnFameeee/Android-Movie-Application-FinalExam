package com.android.movie_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movie_application.R;
import com.android.movie_application.models.Movie;

import java.util.List;

public class MovieTableAdapter extends RecyclerView.Adapter<com.android.movie_application.adapters.MovieTableAdapter.MyViewHolder>
{
    Context context;
    List<Movie> movieList;
    SelectTableItemListener selectTableItemListener;

    public MovieTableAdapter(Context context, List<Movie> movieList)
    {
        this.context = context;
        this.movieList = movieList;
    }

    public MovieTableAdapter(Context context, List<Movie> movieList, SelectTableItemListener selectTableItemListener)
    {
        this.context = context;
        this.movieList = movieList;
        this.selectTableItemListener = selectTableItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.table_movie_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        if (movieList != null && movieList.size() > 0)
        {
            Movie movie = movieList.get(position);
            holder.columnTitle.setText(movie.getTitle());
            holder.columnCategory.setText(movie.getCategory());
            holder.columnDateCreated.setText(movie.getCreatedDate());
            holder.tableLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    selectTableItemListener.onItemClick(movieList.get(position));
                }
            });
        }
        else
        {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView columnTitle, columnCategory, columnDateCreated;
        TableLayout tableLayout;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            columnTitle = itemView.findViewById(R.id.columnTitle);
            columnCategory = itemView.findViewById(R.id.columnCategory);
            columnDateCreated = itemView.findViewById(R.id.columnDateCreated);
            tableLayout = itemView.findViewById(R.id.main_container);
        }
    }
}
