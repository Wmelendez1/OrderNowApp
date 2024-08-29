package com.example.ordernow.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.databinding.BusinessSignUpBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BusinessSignUp extends AppCompatActivity {


    private BusinessSignUpBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "BusinessSignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BusinessSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Set status bar color
        getWindow().setStatusBarColor(Color.parseColor("#36B5FF"));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set up click listener for the sign-up button using binding
        binding.BusinessSignupButton.setOnClickListener(v -> {
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String FirstName = binding.OwnerFirstName.getText().toString().trim();
            String LastName = binding.OwnerLastName.getText().toString().trim();






            if (password.length() < 6) {
                Toast.makeText(BusinessSignUp.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(BusinessSignUp.this, AddBusiness.class);
                                intent.putExtra("uid", user.getUid());
                                intent.putExtra("FirstName", FirstName);
                                intent.putExtra("LastName", LastName);

                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If sign-in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(BusinessSignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });




        });
    }
}


