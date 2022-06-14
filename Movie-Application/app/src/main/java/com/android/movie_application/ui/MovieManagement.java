package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.movie_application.R;
import com.android.movie_application.databinding.ActivityAddMovieBinding;
import com.android.movie_application.databinding.ActivityMovieManagementBinding;
import com.android.movie_application.models.Category;
import com.android.movie_application.models.Movie;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieManagement extends AppCompatActivity
{
    ActivityMovieManagementBinding activityMovieManagementBinding;
    Uri coverUri, thumbnailUri, videoUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    List<Category> lstCategory = new ArrayList<>();
    EditText editTextTitle, editTextCategory, editTextDescription;
    String title, category, description, coverPhoto, thumbnail, streamingLink, createdDate;
    ProgressDialog progressDialog;
    String key ="";
    Movie movie = new Movie();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityMovieManagementBinding = ActivityMovieManagementBinding.inflate(getLayoutInflater());
        setContentView(activityMovieManagementBinding.getRoot());

        if (getIntent().getSerializableExtra("myMovie") != null)
        {
            movie = (Movie) getIntent().getSerializableExtra("myMovie");
            fetchData(movie);
        }

        activityMovieManagementBinding.videoViewSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieManagement.this,PersonalVideo.class);
                intent.putExtra("myVideo", videoUri.toString());
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });

        activityMovieManagementBinding.buttonConfirmUpdateMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMovie();
            }
        });




    }

    private void fetchData(Movie movie)
    {
        activityMovieManagementBinding.editTextTitle.setText(movie.getTitle());
        activityMovieManagementBinding.editTextCategory.setText(movie.getCategory());
        category = movie.getCategory();
        activityMovieManagementBinding.editTextDescription.setText(movie.getDescription());
        coverPhoto = movie.getCoverPhoto();
        thumbnail = movie.getThumbnail();
        streamingLink = movie.getStreamingLink();
        coverUri = Uri.parse(movie.getCoverPhoto());
        Glide.with(activityMovieManagementBinding.getRoot())
                .load(coverUri)
                .into(activityMovieManagementBinding.imageViewLoadCoverPhoto);
        thumbnailUri = Uri.parse((movie.getThumbnail()));
        Glide.with(activityMovieManagementBinding.getRoot())
                .load(thumbnailUri)
                .into(activityMovieManagementBinding.imageViewLoadThumbnail);
        videoUri = Uri.parse(movie.getStreamingLink());
        activityMovieManagementBinding.videoViewSource.setVideoURI(videoUri);
        createdDate = movie.getCreatedDate();
    }

    private void updateMovie()
    {
        editTextTitle  = (EditText) findViewById(R.id.editTextTitle);
        title = editTextTitle.getText().toString();

        editTextDescription  = (EditText) findViewById(R.id.editTextDescription);
        description = editTextDescription.getText().toString();


        Movie movie = new Movie(title, description, coverPhoto, thumbnail, streamingLink, createdDate);
        String categoryKey = "";
        DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Database").child("Category");
        Query query = dbReference.orderByChild("title").equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String keyResult = dataSnapshot.getKey();
//                    String key = dbReference.child(dataSnapshot.getKey()).child("movies").getKey();
//                    assert key != null;
                    Query query1 = dbReference.child(dataSnapshot.getKey()).child("movies").orderByChild("title").equalTo(title);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            for (DataSnapshot dataSnapshot3 : snapshot2.getChildren())
                            {
                                key = dataSnapshot3.getKey();
                                Map<String, Object> map = new HashMap<>();
                                map.put("title",title);
                                map.put("description",description);
                                map.put("coverPhoto",coverPhoto);
                                map.put("thumbnail",thumbnail);
                                map.put("streamingLink",streamingLink);
                                map.put("createdDate",createdDate);
                                dbReference.child(keyResult).child(key).updateChildren(map, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(MovieManagement.this,"Update Movie Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                    dbReference.child(dataSnapshot.getKey()).updateChildren(mapData, new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                            Toast.makeText(AddNewMovie.this,"Upload Movie Successfully",Toast.LENGTH_SHORT).show();
//                        }
//                    });




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}