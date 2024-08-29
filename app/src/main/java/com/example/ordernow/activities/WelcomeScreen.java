package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.R;
import com.example.ordernow.databinding.ActivityMainBinding;
import com.example.ordernow.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ActivitySignUpBinding binding;
    ActivityMainBinding MainBinding;
    public String TAG = "StringTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        MainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);

        //set buttons here
        Button signUpButton = findViewById(R.id.signupButton);
        Button loginButton = findViewById(R.id.loginButton);

        setVariable();

        getWindow().setStatusBarColor(getResources().getColor(R.color.themecolor));
        //on click listeners here
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to start signup activitry
                Intent intent = new Intent(WelcomeScreen.this, SignUp.class);
                startActivity(intent); //starts activity
            }
        });

       loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to start signup activitry
                Intent intent = new Intent(WelcomeScreen.this, Login.class);
                startActivity(intent); //starts activity
            }
        });


    }

    private void setVariable() {
        binding.loginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.signupButton.setOnClickListener(v -> startActivity(new Intent(WelcomeScreen.this, SignUp.class)));

        MainBinding.loginButton.setOnClickListener(v -> startActivity(new Intent(this, Login.class)));
    }



}

