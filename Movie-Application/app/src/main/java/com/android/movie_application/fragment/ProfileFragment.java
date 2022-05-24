package com.android.movie_application.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.movie_application.R;
import com.android.movie_application.databinding.FragmentProfileBinding;
import com.android.movie_application.models.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileFragment extends Fragment
{
    //Use the concept of View Binding using binding class and database reference
    FragmentProfileBinding fragmentProfileBinding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
    //Initialize variables
    TextView textViewUsername, textViewAccount,textViewFirstName,textViewLastName,textViewPassword;
    EditText editTextUsername;
    ImageView imageView;
    Button btnEditProfile;
    String userName, firstName, lastName, email, password;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Create an object of this binding class
        fragmentProfileBinding = FragmentProfileBinding.inflate(getLayoutInflater());

        //Identify necessary items
        textViewAccount = view.findViewById(R.id.textViewAccountInput);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        textViewFirstName = view.findViewById(R.id.textViewFirstNameInput);
        textViewLastName = view.findViewById(R.id.textViewLastNameInput);
        textViewPassword = view.findViewById(R.id.textViewPasswordInput);
        btnEditProfile = view.findViewById(R.id.btn_account_edit_username);
        imageView = view.findViewById(R.id.imageViewAvatar);

        //Check condition if there is an account logging in and retrieve data
        if (getArguments() != null)
        {
            email = getArguments().getString("myAccount");
            textViewAccount.setText(email);
            getUserByEmail(email);
        }

        //Handle edit profile button click event
        btnEditProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userName = editTextUsername.getText().toString();
                databaseReference.child(firebaseAuth.getUid()).child("userName").setValue(userName);
                Toast.makeText(getActivity(),"Update Username successfully",Toast.LENGTH_SHORT).show();
                editTextUsername.setText(userName);
            }
        });

        return view;
    }

    //Get the document of the Users node with email parameter
    public void getUserByEmail(String email)
    {
        //Get reference for the Users node
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //Performing task with the retrieving data
                User user = snapshot.getValue(User.class);
                Toast.makeText(getActivity(),"Welcome back "+ user.getUserName(),Toast.LENGTH_SHORT).show();
                //Fetch data to the layout
                fetchProfileData(user.getUserName(),user.getFirstName(),user.getLastName(),user.getPassword(),user.getImage().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getActivity(),"Failed to get user data",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchProfileData(String userName, String firstName, String lastName, String password, String image)
    {
        editTextUsername.setText(userName);
        textViewFirstName.setText(firstName);
        textViewLastName.setText(lastName);
        textViewPassword.setText(password);
        imageView.setImageURI(Uri.parse(image));

        Glide.with(getActivity())
                .load(Uri.parse(image))
                .into(imageView);
    }

    public void isUsernameChanged()
    {
        databaseReference.child(firebaseAuth.getUid()).child("userName").setValue(textViewUsername.getText().toString());
    }




}