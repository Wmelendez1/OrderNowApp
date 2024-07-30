package com.example.ordernow.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordernow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputEditText emailText, passwordText;
    FirebaseAuth mAuth;
    int failCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // button set up
        Button signUpBtn = findViewById(R.id.signup_button);
        Button loginBtn = findViewById(R.id.loginButton);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

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
                String email, password;
                email = String.valueOf(emailText.getText());
                password = String.valueOf(passwordText.getText());
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //TODO go to homepage
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent toSignUp = new Intent(Login.this, SignUp.class);
                                    startActivity(toSignUp);
                                    finish();
                                } else {
                                    Intent toSignUp = new Intent(Login.this, Login.class);
                                    startActivity(toSignUp);
                                    finish();
                                }
                            }
                        });


            }
        });

    }
}