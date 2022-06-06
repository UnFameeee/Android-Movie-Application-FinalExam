package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.movie_application.R;
import com.android.movie_application.databinding.ActivityAddMovieBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNewMovie extends AppCompatActivity
{
    ActivityAddMovieBinding activityAddMovieBinding;
    Uri coverUri;
    Uri thumbnailUri;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText title, category;
    String coverPhoto, thumbnail, sourceVideo;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Use view binding to create the subclass of AddNewMovie with all the view attributes
        activityAddMovieBinding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(activityAddMovieBinding.getRoot());

        title = (EditText) findViewById(R.id.editTextTitle);
        category = (EditText) findViewById(R.id.editTextCategory);

        //Select the movie's cover photo and upload
        activityAddMovieBinding.imageViewSelectMovieCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                selectCoverPhoto();
            }
        });

        activityAddMovieBinding.buttonUploadCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                uploadCoverPhoto();
            }
        });

        //Select the movie's thumbnail and upload
        activityAddMovieBinding.imageViewSelectThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectThumbnail();
            }
        });

        activityAddMovieBinding.buttonUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadThumbnail();
            }
        });

        //Select the video's source and upload
        activityAddMovieBinding.imageViewSelectMovieSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideo();
            }
        });


    }

    //Select Cover Photo from gallery
    private void selectCoverPhoto()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    public void selectThumbnail()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,200);
    }

    public void selectVideo()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null)
        {
            coverUri = data.getData();
            activityAddMovieBinding.imageViewLoadCoverPhoto.setImageURI(coverUri);
        }
        else if(requestCode == 200 && data != null && data.getData() != null)
        {
            thumbnailUri = data.getData();
            activityAddMovieBinding.imageViewLoadThumbnail.setImageURI(thumbnailUri);
        }
        else if(requestCode == 300 && data != null && data.getData() != null)
        {
            thumbnailUri = data.getData();
            activityAddMovieBinding.imageViewLoadThumbnail.setImageURI(thumbnailUri);
        }
    }

    //Upload Cover Photo and Thumbnail to storage in Firebase
    public void uploadCoverPhoto()
    {
        String fileName = title.getText().toString();
        if (fileName.isEmpty())
        {
            title.setError("Title is required!");
            title.requestFocus();
        }
        else
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Cover Photo....");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("movie-coverphoto/" + fileName);
            storageReference.putFile(coverUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AddNewMovie.this, "Upload Cover Photo successfully!", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task)
                                {
                                    coverPhoto = task.getResult().toString();
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(AddNewMovie.this, "Failure", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }



    public void uploadThumbnail()
    {
        String fileName = title.getText().toString();
        if (fileName.isEmpty())
        {
            title.setError("Title is required!");
            title.requestFocus();
        }
        else
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Thumbnail....");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("movie-thumbnail/" + fileName);
            storageReference.putFile(thumbnailUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AddNewMovie.this, "Upload Thumbnail successfully!", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {

                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            thumbnail = task.getResult().toString();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(AddNewMovie.this, "Failure", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }







}