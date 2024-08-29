package com.example.ordernow.activities;
import com.example.ordernow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BusinessLogIn extends AppCompatActivity {
    TextInputEditText emailText, passwordText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        Button signUpBtn = findViewById(R.id.signup_button);
        Button loginBtn = findViewById(R.id.loginButton);

        // Set up onClick listeners
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp = new Intent(BusinessLogIn.this, BusinessSignUp.class);
                startActivity(toSignUp);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(emailText.getText());
                password = String.valueOf(passwordText.getText());

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String uid = user.getUid(); // Get UID
                                        Intent businessintent = new Intent(BusinessLogIn.this, BusinessMngrHomePage.class);
                                        businessintent.putExtra("uid", uid);;
                                        startActivity(businessintent);
                                        finish();
                                    }
                                } else {
                                    // Sign in failure
                                    // Show an error message
                                    Toast.makeText(BusinessLogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}