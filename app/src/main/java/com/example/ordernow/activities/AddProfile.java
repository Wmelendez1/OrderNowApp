package com.example.ordernow.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ordernow.R;
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

public class AddProfile extends AppCompatActivity {
     private AddProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<String> categoryTitleArrayList, categoryIdArrayList;
    private String selectedCategoryId, selectedCategoryTitle;
    private Uri pdfUri;
    private static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "ADD_PROFILE_TAG";

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
    }

    private void validateData() {
        String firstName = binding.firstNameTv.getText().toString().trim();
        String lastName = binding.lastNameTv.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Upload Profile Image", Toast.LENGTH_SHORT).show();
        } else {
            uploadPdfToStorage(firstName, lastName);
        }
    }

    private void uploadPdfToStorage(String firstName, String lastName) {
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        String filePathAndName = "Profile/" + timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(uri -> {
                        String pdfUrl = uri.toString();
                        uploadPdfInfoToDB(firstName, lastName, pdfUrl, timestamp);
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProfile.this, "Failed to upload Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadPdfInfoToDB(String firstName, String lastName, String pdfUrl, long timestamp) {
        String uid = firebaseAuth.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books").child(String.valueOf(timestamp));

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("id", timestamp);
        hashMap.put("title", firstName);
        hashMap.put("description", lastName);
        hashMap.put("url", pdfUrl);
        hashMap.put("categoryId", selectedCategoryId);
        hashMap.put("timestamp", timestamp);

        ref.setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProfile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(AddProfile.this, "Failed to upload image info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void pdfPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PDF_PICK_CODE) {
                pdfUri = data.getData();
                if (pdfUri != null) {
                    Toast.makeText(this, "Image Selected: " + pdfUri.getPath(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
