package com.example.ordernow.activities;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ordernow.R;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileLayout extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        //button st up
        ImageButton backBtn = findViewById(R.id.backBtnProfile);
        ImageButton editprofileBtn = findViewById(R.id.editProfileBtn);
        //need this bool for the edit texts

        ImageButton editPicBtn = findViewById(R.id.editProfilePicBtn);
        ImageButton saveProfileBtn = findViewById(R.id.saveBtn);
        // text view set ups
        EditText firstname = findViewById(R.id.firstnameET);
        EditText lastname = findViewById(R.id.lastNameET);
        TextInputEditText aboutMe = findViewById(R.id.aboutmeET);


        // on clicks
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // functionality to get to home page
            }
        });

        editprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editprofileBtn.setVisibility(View.INVISIBLE);
                editprofileBtn.setClickable(false);
                saveProfileBtn.setVisibility(View.VISIBLE);
                saveProfileBtn.setClickable(true);
                editPicBtn.setVisibility(View.VISIBLE);
                editPicBtn.setClickable(true);
                if (!(firstname.isEnabled())) {
                    firstname.setEnabled(true);
                }
                if (!(lastname.isEnabled())) {
                    lastname.setEnabled(true);
                }
                if (!(aboutMe.isEnabled())) {
                    aboutMe.setEnabled(true);
                }
            }
        });

        editPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(intent);
            }
        });


        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editprofileBtn.setVisibility(View.VISIBLE);
                editprofileBtn.setClickable(true);
                saveProfileBtn.setVisibility(View.INVISIBLE);
                saveProfileBtn.setClickable(false);
                editPicBtn.setVisibility(View.INVISIBLE);
                editPicBtn.setClickable(false);
                if ((firstname.isEnabled())) {
                    firstname.setEnabled(false);
                }
                if ((lastname.isEnabled())) {
                    lastname.setEnabled(false);
                }
                if ((aboutMe.isEnabled())) {
                    aboutMe.setEnabled(false);
                }
            }
        });

    }

}