package com.example.ordernow.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import com.example.ordernow.R;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set buttons here
        Button signUpButton = findViewById(R.id.signupButton);
        Button loginBtn = findViewById(R.id.loginButton);
        //on click listeners here
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to start signup activitry
                Intent intent = new Intent(WelcomeScreen.this, SignUp.class);
                startActivity(intent); //starts activity
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // intent to homepage
                Intent toLogin = new Intent(WelcomeScreen.this, Login.class); // add intent later
                startActivity(toLogin);

            }
        });

    }
}

