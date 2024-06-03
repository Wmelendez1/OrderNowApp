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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProfileActivity extends AppCompatActivity {
    private AddProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pdfUri;
    private static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "ADD_PROFILE_TAG";
    private ArrayList<ModelPdf> pdfArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfPickIntent();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        loadProfiles();
    }

    private String firstName = "", lastName = "", Age = "", username = "", bio = "";

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
            uploadPdfToStorage(firstName, lastName, Age, username, bio);
        }
    }

    private void uploadPdfToStorage(String firstName, String lastName, String Age, String username, String bio) {
        Log.d(TAG, "uploadPdfToStorage: upload to storage...");
        progressDialog.setMessage("Uploading Profile Picture...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        String pdfFileName = "profile_" + timestamp + ".pdf";
        StorageReference pdfStorageReference = FirebaseStorage.getInstance().getReference().child("ProfilePDFs").child(pdfFileName);

        pdfStorageReference.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "onSuccess: PDF uploaded to storage...");
                    Log.d(TAG, "onSuccess: getting pdf url...");

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(uri -> {
                        String uploadPdfUrl = "" + uri.toString();
                        uploadPdfInfoToDB(firstName, lastName, uploadPdfUrl, timestamp, Age, username, bio);
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

    private void uploadPdfInfoToDB(String firstName, String lastName, String uploadPdfUrl, long timestamp, String Age, String username, String bio) {
        Log.d(TAG, "uploadPdfInfoToDB: uploading PDF info to firebase db...");
        progressDialog.setMessage("Uploading Profile Info...");
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + uid);
        hashMap.put("id", "" + timestamp);
        hashMap.put("firstName", "" + firstName);
        hashMap.put("lastName", "" + lastName);
        hashMap.put("url", "" + uploadPdfUrl);
        hashMap.put("timestamp", timestamp);
        hashMap.put("Age", "" + Age);
        hashMap.put("username", "" + username);
        hashMap.put("Bio", "" + bio);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profiles");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onSuccess: Successfully uploaded...");
                    Toast.makeText(AddProfileActivity.this, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProfileActivity.this, ProfileLayout.class));
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: Failed to upload to db due to " + e.getMessage());
                    Toast.makeText(AddProfileActivity.this, "Failed to upload to db due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (resultCode == RESULT_OK) {
            if (requestCode == PDF_PICK_CODE) {
                Log.d(TAG, "onActivityResult: PDF Picked");

                pdfUri = data.getData();
                Log.d(TAG, "onActivityResult: URI;" + pdfUri);
            }
        } else {
            Log.d(TAG, "onActivityResult: cancelled picking pdf");
            Toast.makeText(this, "Cancelled picking PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfiles() {
        Log.d(TAG, "loadProfiles: Loading profiles...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Profiles");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPdf modelPdf = ds.getValue(ModelPdf.class);
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
