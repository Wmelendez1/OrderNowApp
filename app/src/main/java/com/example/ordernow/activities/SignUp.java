package com.example.ordernow.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.R;
import com.example.ordernow.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {


    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button loginreg = findViewById(R.id.loginreg);

        // Set status bar color
        getWindow().setStatusBarColor(Color.parseColor("#36B5FF"));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        binding.loginreg.setOnClickListener(v -> startActivity(new Intent(this, Login.class)));

        loginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to start signup activitry
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent); //starts activity
            }
        });

        // Set up click listener for the sign-up button using binding
        binding.signupButton.setOnClickListener(v -> {
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (password.length() < 6) {
                Toast.makeText(SignUp.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create user with email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Navigate to main activity upon successful sign-up
                                startActivity(new Intent(SignUp.this, AddProfileActivity.class));
                                finish(); // Close the sign-up activity
                            }
                        } else {
                            // If sign-in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }
}


