package com.android.movie_application.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.movie_application.fragment.SearchFragment;
import com.android.movie_application.fragment.HomePageFragment;
import com.android.movie_application.fragment.ProfileFragment;
import com.android.movie_application.R;
import com.android.movie_application.fragment.SettingFragment;
import com.android.movie_application.databinding.ActivityMainBinding;
import com.android.movie_application.models.Movie;
import com.android.movie_application.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String account = "", role = "";
    Integer count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        View decorView = getWindow().getDecorView();
//
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                //                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                | View.SYSTEM_UI_FLAG_IMMERSIVE);


        //Check condition if there is an account logging in
        if (getIntent().getStringExtra("account") != null)
        {
            //Set value for account
            account = getIntent().getStringExtra("account");
            role = getIntent().getStringExtra("role");
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomePageFragment());

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomePageFragment());
                    break;
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.profile:
                    //if there is already account logging in, pass data to the profile fragment
                    ProfileFragment profileFragment = new ProfileFragment();
                    if (account.length()>0)
                        {
                            //initialize bundle and put value then pass argument
                            Bundle bundle = new Bundle();
                            bundle.putString("myAccount",account);
                            profileFragment.setArguments(bundle);
                        }
                    replaceFragment(profileFragment);
                    break;
                case R.id.setting:
                    SettingFragment settingFragment = new SettingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("role",role);
                    settingFragment.setArguments(bundle);
                    replaceFragment(settingFragment);
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void loginFromSetting(View view)
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void addMovieFromSetting(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddNewMovie.class);
        startActivity(intent);
    }
}