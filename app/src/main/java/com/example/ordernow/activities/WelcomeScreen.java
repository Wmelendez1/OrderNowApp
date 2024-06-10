package com.example.ordernow.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import com.example.ordernow.R;

import com.example.ordernow.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ActivitySignUpBinding binding;
    public String TAG = "StringTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

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

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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
    }

}

