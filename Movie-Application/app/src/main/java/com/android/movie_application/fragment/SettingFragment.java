package com.android.movie_application.fragment;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.movie_application.R;
import com.android.movie_application.databinding.FragmentProfileBinding;
import com.android.movie_application.databinding.FragmentSettingBinding;
import com.android.movie_application.ui.LoginActivity;
import com.android.movie_application.ui.MainActivity;
import com.android.movie_application.ui.addNewMovie;

public class SettingFragment extends Fragment {

    String role;
    FragmentSettingBinding fragmentSettingBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //Create an object of this binding class
        fragmentSettingBinding = FragmentSettingBinding.inflate(getLayoutInflater());

        TextView textView;
        textView = view.findViewById(R.id.textView14);

       textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getContext(), addNewMovie.class);
               startActivity(intent);
           }
       });


        return inflater.inflate(R.layout.fragment_setting, container, false);

    }
}