package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ordernow.R;
import com.example.ordernow.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // button set up
        Button signUpBtn = findViewById(R.id.signup_button);
        Button loginBtn = findViewById(R.id.loginButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to signup activity
                Intent toSignUp = new Intent(Login.this, SignUp.class);
                startActivity(toSignUp);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // intent to homepage
                Intent LoggingIn = new Intent(); // add intent later
                startActivity(LoggingIn);

            }
        });

    }
}