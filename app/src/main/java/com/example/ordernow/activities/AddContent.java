package com.example.ordernow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;


import android.widget.Toast;

import com.example.ordernow.databinding.AddcontentBinding;
import com.example.ordernow.databinding.AddcontentrowBinding;

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


public class AddContent extends AppCompatActivity implements SelectListener {

    private AddcontentBinding binding;

    AddcontentrowBinding rowBinding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pdfUri;

    private ArrayList<ModelContent> ContentArrayList;
    private AdapterContent adapterContent;
    private static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "ADD_PROFILE_TAG";

    private String title = "", description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddcontentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        ContentArrayList = new ArrayList<>();
        adapterContent = new AdapterContent(this, ContentArrayList,this);





        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.attachBtn.setOnClickListener(v -> pdfPickIntent());

        binding.submitBtn2.setOnClickListener(v -> validateData());

        loadPdfContent();
    }

    private void validateData() {
        Log.d(TAG, "validateData: validating data...");

        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Pick PDF...", Toast.LENGTH_SHORT).show();
        } else {
            uploadContentPdfToStorage();

        }
    }

    private void uploadContentPdfToStorage() {
        Log.d(TAG, "uploadPdfToStorage: upload to storage...");

        progressDialog.setMessage("Uploading Content...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        String filePathAndName = "Content/" + timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "onSuccess: PDF uploaded to storage... ");
                    Log.d(TAG, "onSuccess: getting PDF url... ");

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        String uploadPdfUrl = uri.toString();
                        uploadContentPdfInfoToDB(uploadPdfUrl, timestamp);
                        startActivity(new Intent(AddContent.this, ProfileLayout.class));
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: Failed to get PDF URL due to " + e.getMessage());
                        Toast.makeText(AddContent.this, "Failed to get PDF URL due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: PDF upload failed due to " + e.getMessage());
                    Toast.makeText(AddContent.this, "PDF upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadContentPdfInfoToDB(String uploadPdfUrl, long timestamp) {
        Log.d(TAG, "uploadContentPdfInfoToDB: uploading PDF info to Firebase DB...");

        progressDialog.setMessage("Uploading PDF info...");

        String uid = firebaseAuth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("title", title);
        hashMap.put("description", description);
        hashMap.put("url", uploadPdfUrl);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", uid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Content");
        ref.child(String.valueOf(timestamp))
                .setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onSuccess: Successfully uploaded...");
                    Toast.makeText(AddContent.this, "Successfully uploaded...", Toast.LENGTH_SHORT).show();

                    binding.titleEt.setText("");
                    binding.descriptionEt.setText("");
                    pdfUri = null;
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: Failed to upload to DB due to " + e.getMessage());
                    Toast.makeText(AddContent.this, "Failed to upload to DB due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadPdfContent() {
        Log.d(TAG, "loadPdfContent: Loading PDF content...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Content");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ContentArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelContent model = ds.getValue(ModelContent.class);
                    ContentArrayList.add(model);
                }
                adapterContent.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }


    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: starting PDF pick intent");
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
                Log.d(TAG, "onActivityResult: URI: " + pdfUri);
            }
        } else {
            Log.d(TAG, "onActivityResult: cancelled picking PDF");
            Toast.makeText(this, "Cancelled picking PDF", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onItemClicked(ModelContent modelContent) {




        // Pass the timestamp to the EditContentInfo activity
        Intent intent = new Intent(AddContent.this, EditContentInfo.class);
        intent.putExtra("modelContent", modelContent); // Pass the object
        startActivity(intent);
    }




}