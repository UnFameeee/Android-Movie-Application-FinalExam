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
import com.android.movie_application.models.Category;
import com.android.movie_application.models.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    List<Category> mData;
    CategoryItemClickListener categoryItemClickListener;


    public CategoryAdapter(Context context, List<Category> mData, CategoryItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        this.categoryItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.TvTitle.setText(mData.get(position).getTitle());
        Uri uri = Uri.parse(mData.get(position).getThumbnail());
        Glide.with(context)
                .load(uri)
                .into(myViewHolder.ImgCate);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView TvTitle;
        private ImageView ImgCate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TvTitle = itemView.findViewById(R.id.movie_item_title);
            ImgCate = itemView.findViewById(R.id.movie_item_img);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    categoryItemClickListener.onCateClick(mData.get(getAdapterPosition()), ImgCate);
                }
            });
        }
    }
}
