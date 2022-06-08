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
import com.android.movie_application.models.Category;
import com.android.movie_application.models.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AddNewMovie extends AppCompatActivity {
    ActivityAddMovieBinding activityAddMovieBinding;
    Uri coverUri, thumbnailUri, videoUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    List<Category> lstCategory = new ArrayList<>();
    EditText editTextTitle, editTextCategory, editTextDescription;
    String title, category, description, coverPhoto, thumbnail, streamingLink;
    ProgressDialog progressDialog;
    String key ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Use view binding to create the subclass of AddNewMovie with all the view attributes
        activityAddMovieBinding = ActivityAddMovieBinding.inflate(getLayoutInflater());
        setContentView(activityAddMovieBinding.getRoot());

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);


        //Select the movie's cover photo and upload
        activityAddMovieBinding.imageViewSelectMovieCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCoverPhoto();
            }
        });

        activityAddMovieBinding.buttonUploadCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        activityAddMovieBinding.buttonUploadSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadVideo();
            }
        });

        activityAddMovieBinding.buttonConfirmUploadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = createRandomKey();
                title = editTextTitle.getText().toString();
                category = editTextCategory.getText().toString();
                description = editTextDescription.getText().toString();
                uploadMovieByTitle(title);
            }
        });


    }

    //Select Cover Photo from gallery
    private void selectCoverPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    public void selectThumbnail() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 200);
    }

    public void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            coverUri = data.getData();
            activityAddMovieBinding.imageViewLoadCoverPhoto.setImageURI(coverUri);
        } else if (requestCode == 200 && data != null && data.getData() != null) {
            thumbnailUri = data.getData();
            activityAddMovieBinding.imageViewLoadThumbnail.setImageURI(thumbnailUri);
        } else if (requestCode == 300 && data != null && data.getData() != null) {
            videoUri = data.getData();
            activityAddMovieBinding.videoViewSource.setVideoURI(videoUri);
            activityAddMovieBinding.videoViewSource.seekTo(1000);
        }
    }

    //Upload Cover Photo and Thumbnail to storage in Firebase
    public void uploadCoverPhoto() {
        String fileName = editTextTitle.getText().toString();
        if (fileName.isEmpty()) {
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Cover Photo....");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("movie/coverPhoto/" + fileName);
            storageReference.putFile(coverUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddNewMovie.this, "Upload Cover Photo successfully!", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
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


    public void uploadThumbnail() {
        String fileName = editTextTitle.getText().toString();
        if (fileName.isEmpty()) {
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Thumbnail....");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("movie/thumbnail/" + fileName);
            storageReference.putFile(thumbnailUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddNewMovie.this, "Upload Thumbnail successfully!", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
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


    private void uploadVideo() {

        String fileName = editTextTitle.getText().toString();
        if (fileName.isEmpty()) {
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Video....");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("movie/source/" + fileName);
            storageReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddNewMovie.this, "Upload Video successfully!", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            streamingLink = task.getResult().toString();
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

    private void confirmUploadMovie() {
        //create the reference to the database
//        databaseReference = FirebaseDatabase.getInstance().getReference("Database").child("Category");
//
//
//
//        Movie movie = new Movie(title,description,coverPhoto,thumbnail,streamingLink);


    }

    private void uploadMovieByTitle(String title) {
        Movie movie = new Movie(title, description, coverPhoto, thumbnail, streamingLink);
        String categoryKey = "";
        DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Database").child("Category");
        Query query = dbReference.orderByChild("title").equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String key = dbReference.child(dataSnapshot.getKey()).child("movies").push().getKey();
                    dbReference.child(dataSnapshot.getKey()).child("movies").child(key).setValue(movie);
                    Toast.makeText(AddNewMovie.this, "Add new movie successfully!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
////                for (DataSnapshot dataSnapshot: snapshot.getChildren())
////                {
////                        String key = dataSnapshot.getKey();
////                        Category category = new Category(key);
////                        lstCategory.add(category);
////                        for(DataSnapshot categoryDetail : snapshot.child(key).getChildren())
////                        Log.d("the category key: ", category.getKey());
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public String createRandomKey()
    {
        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 10;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();
//        System.out.println("Random String is: " + randomString);
        return randomString;
    }




}