package com.example.ordernow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.databinding.AddProfileBinding;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;

import com.example.ordernow.Models.ModelPdf;

public class AddProfileActivity extends AppCompatActivity {
    private AddProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pdfUri;
    private static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "ADD_PROFILE_TAG";

    String id;
    private ArrayList<String> pdfArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.attachBtn.setOnClickListener(v -> pdfPickIntent());

        binding.submitBtn.setOnClickListener(v -> validateData());

        loadProfiles();


    }

    private String firstName = "", lastName = "", Age = "", username = "", bio = "",uploadContentPdfUrl = "", title = "", description = "" ;

    private void validateData() {
        Log.d(TAG, "validateData: validating data...");

        firstName = binding.firstNameTv.getText().toString().trim();
        lastName = binding.lastNameTv.getText().toString().trim();
        Age = binding.AgeTv.getText().toString().trim();
        username = binding.username.getText().toString().trim();
        bio = binding.Bio.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Age)) {
            Toast.makeText(this, "Enter Age", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bio)) {
            Toast.makeText(this, "Enter Bio", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Pick PDF...", Toast.LENGTH_SHORT).show();
        } else {
            uploadPdfToStorage(firstName, lastName, Age, username, bio, uploadContentPdfUrl, title, description );
        }
    }


    private void uploadPdfToStorage(String firstName, String lastName, String Age, String username, String bio,String uploadContentPdfUrl,String title,String description) {
        Log.d(TAG, "uploadPdfToStorage: upload to storage...");
        progressDialog.setMessage("Uploading Profile Picture...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        String pdfFileName = "Profiles" + timestamp + ".pdf";
        StorageReference pdfStorageReference = FirebaseStorage.getInstance().getReference().child("url").child(pdfFileName);

        String uid = firebaseAuth.getUid();
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Profiles");

        // Step 1: Query for the existing profile node
        profileRef.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Step 2: Delete the existing profile node if found
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }

                // Step 3: Upload the new profile PDF to storage
                pdfStorageReference.putFile(pdfUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d(TAG, "onSuccess: PDF uploaded to storage...");
                            Log.d(TAG, "onSuccess: getting pdf url...");

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask.addOnSuccessListener(uri -> {
                                String uploadPdfUrl = uri.toString();
                                uploadPdfInfoToDB(firstName, lastName, uploadPdfUrl, timestamp, Age, username, bio, uploadContentPdfUrl,title, description);
                                // Reset pdfUri after successful URL retrieval and database upload
                                pdfUri = null;
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Log.d(TAG, "onFailure: Failed to get PDF URL due to " + e.getMessage());
                                Toast.makeText(AddProfileActivity.this, "Failed to get PDF URL due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "onFailure: PDF upload failed due to " + e.getMessage());
                            Toast.makeText(AddProfileActivity.this, "PDF upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.d(TAG, "onCancelled: Failed to query existing profile due to " + databaseError.getMessage());
                Toast.makeText(AddProfileActivity.this, "Failed to query existing profile due to " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadPdfInfoToDB(String firstName, String lastName, String uploadPdfUrl, long timestamp, String Age, String username, String bio, String uploadContentPdfUrl, String title, String description) {
        Log.d(TAG, "uploadPdfInfoToDB: uploading PDF info to firebase db...");
        progressDialog.setMessage("Uploading Profile Info...");
        String uid = firebaseAuth.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profiles");

        // Step 1: Query for the existing profile node
        ref.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Step 2: Delete the existing profile node if found
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }

                // Step 3: Upload the new profile info to the database
                String profileId = ref.push().getKey();

                // Create a HashMap for the profile node
                HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("uid", uid);
                profileMap.put("id", timestamp);
                profileMap.put("firstName", firstName);
                profileMap.put("lastName", lastName);
                profileMap.put("url", uploadPdfUrl);
                profileMap.put("timestamp", timestamp);
                profileMap.put("Age", Age);
                profileMap.put("username", username);
                profileMap.put("Bio", bio);



                // Upload the profile info with the nested content node to the database
                ref.child(String.valueOf(timestamp))
                        .setValue(profileMap)
                        .addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "onSuccess: Successfully uploaded...");
                            Toast.makeText(AddProfileActivity.this, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
                            // Reset pdfUri after successful upload
                            pdfUri = null;
                            startActivity(new Intent(AddProfileActivity.this, ProfileLayout.class));
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "onFailure: Failed to upload to db due to " + e.getMessage());
                            Toast.makeText(AddProfileActivity.this, "Failed to upload to db due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.d(TAG, "onCancelled: Failed to query existing profile due to " + databaseError.getMessage());
                Toast.makeText(AddProfileActivity.this, "Failed to query existing profile due to " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PDF_PICK_CODE && data != null && data.getData() != null) {
            Log.d(TAG, "onActivityResult: PDF Picked");
            pdfUri = data.getData();
            Log.d(TAG, "onActivityResult: URI: " + pdfUri);
        } else {
            Log.d(TAG, "onActivityResult: cancelled picking pdf");
            Toast.makeText(this, "Cancelled picking PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfiles() {
        Log.d(TAG, "loadProfiles: Loading profiles...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profiles" + "id");
        pdfArrayList = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String id = ""+ds.child("id").getValue();

                    String modelPdf = ds.child("url").getValue(String.class);

                    pdfArrayList.add(modelPdf);

                }
                Log.d(TAG, "onDataChange: Loaded " + pdfArrayList.size() + " profiles.");
                // Update UI or notify adapter here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: Failed to load profiles due to " + error.getMessage());
                Toast.makeText(AddProfileActivity.this, "Failed to load profiles.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
