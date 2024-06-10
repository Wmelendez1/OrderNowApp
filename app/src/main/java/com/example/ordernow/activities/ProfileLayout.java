package com.example.ordernow.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.databinding.ActivityProfileLayoutBinding;
import com.example.ordernow.databinding.ActivitySignUpBinding;
import com.example.ordernow.databinding.AddProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ProfileLayout extends AppCompatActivity {

    // View binding
    private ActivityProfileLayoutBinding binding;
    private AddProfileBinding AddProfilebinding;
    private ActivitySignUpBinding SignupBinding;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference profileRef;
    private String id;

    // Profile information
    private String firstName, lastName, Age, email, username, bio;
    private Uri pdfUri;

    private static final String TAG = "PROFILE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileLayoutBinding.inflate(getLayoutInflater());
        AddProfilebinding = AddProfileBinding.inflate(getLayoutInflater());
        SignupBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                profileRef = FirebaseDatabase.getInstance().getReference("Profiles");
                retrieveProfileInfo(email);
            }
        } else {
            Log.e(TAG, "No user is currently signed in.");
        }

        // Back button click listener
        binding.backBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    private void retrieveProfileInfo(String Email) {
        profileRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.exists()) {
                        firstName = ds.child("firstName").getValue(String.class);
                        lastName = ds.child("lastName").getValue(String.class);
                        Age = ds.child("age").getValue(String.class);
                        email = ds.child("email").getValue(String.class);
                        bio = ds.child("Bio").getValue(String.class);
                        username = ds.child("username").getValue(String.class);

                        // Log the retrieved data to verify
                        Log.d(TAG, "First Name: " + firstName);
                        Log.d(TAG, "Last Name: " + lastName);
                        Log.d(TAG, "Age: " + Age);
                        Log.d(TAG, "Email: " + email);
                        Log.d(TAG, "Bio: " + bio);
                        Log.d(TAG, "username: " + username);

                        // Update UI with retrieved profile information
                        binding.firstnameET.setText(firstName);
                        binding.lastNameET.setText(lastName);
                        binding.aboutmeET.setText(bio);
                        binding.username.setText(username);


                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }

    private void uploadInfoProfile(String firstName, String lastName, String age) {
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firstName", firstName);
        hashMap.put("lastName", lastName);
        hashMap.put("age", age);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profiles");
        ref.child(uid).setValue(hashMap)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Profile updated successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to update profile: " + e.getMessage()));
    }
}