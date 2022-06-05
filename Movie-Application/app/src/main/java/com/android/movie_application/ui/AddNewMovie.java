package com.android.movie_application.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.movie_application.R;
import com.android.movie_application.databinding.ActivityAddMovieBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddNewMovie extends AppCompatActivity
{
    ActivityAddMovieBinding activityAddMovieBinding;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    EditText title, category;
    String coverPhoto, thumbnail, sourceVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityAddMovieBinding = ActivityAddMovieBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_add_movie);
        setContentView(activityAddMovieBinding.getRoot());
        title = (EditText) findViewById(R.id.editTextTitle);
        category = (EditText) findViewById(R.id.editTextCategory);


        activityAddMovieBinding.imageViewMovieCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCoverPhoto();
            }
        });


    }

    private void selectCoverPhoto()
    {

    }
    //Upload Cover Photo and Thumbnail to storage in Firebase
    public void uploadCoverPhoto(View view)
    {

    }
    public void uploadThumbnail(View view)
    {

    }





}