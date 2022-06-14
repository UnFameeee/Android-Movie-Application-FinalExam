package com.android.movie_application.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movie_application.R;
import com.android.movie_application.models.Chapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder>{
    Context context;
    ArrayList<Chapter> mData;
    int index;
    ChapterItemClickListener chapterItemClickListener;

    public ChapterAdapter(Context context, ArrayList<Chapter> mData, int index, ChapterItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        this.index = index;
        this.chapterItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.TvTitle.setText(mData.get(position).getTitle());
        if(position == index){
            myViewHolder.TvTitle.setTextColor(Color.rgb(242,229,78));
        }else
            myViewHolder.TvTitle.setTextColor(Color.rgb(255,255,255));
//        myViewHolder.data = (mData.get(position).getData());
        Uri uri = Uri.parse(mData.get(position).getThumbnail());
//        myViewHolder.ImgMovie.setImageURI(uri);
        Glide.with(context)
                .load(uri)
                .into(myViewHolder.ImgChapter);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView TvTitle;
        private ImageView ImgChapter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            TvTitle = itemView.findViewById(R.id.chapter_item_title);
            ImgChapter = itemView.findViewById(R.id.chapter_item_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chapterItemClickListener.onChapterClick(mData.get(getAdapterPosition()), ImgChapter);
                }
            });
        }
    }
}
