package com.example.ordernow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.databinding.ActivityEditContentInfoBinding;
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
public class EditContentInfo extends AppCompatActivity {
    private ActivityEditContentInfoBinding binding;  // Ensure you create this layout file
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pdfUri;
    private static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "EDIT_CONTENT_TAG";

    private ArrayList<ModelContent> ContentArrayList = new ArrayList<>();




    private String contentUid;




    // Set this properly based on authenticated user
    private static final String CONTENT_PATH = "Content";
    private ModelContent modelContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditContentInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);



        modelContent = (ModelContent) getIntent().getSerializableExtra("modelContent");

        if (modelContent != null) {
            // Use the modelContent object to populate UI elements
            String contentTitle = modelContent.getContentTitle();
            String contentDescription = modelContent.getContentDescription();
            String contentPdf = modelContent.getContentPdf();
            long timestamp = modelContent.getTimestamp();

            fetchAndDisplayCurrentNodeContent( timestamp);

        }


        contentUid = firebaseAuth.getUid();

        // Fetch and display current node content





        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.attachBtn.setOnClickListener(v -> pdfPickIntent());
        binding.submitBtn2.setOnClickListener(v -> validateData());




    }


    private void fetchAndDisplayCurrentNodeContent(Long timestamp) {

        ContentArrayList.clear();

        String uid = firebaseAuth.getUid();
        if (uid == null || uid.isEmpty()) {
            Log.e(TAG, "UID is null or empty");
            return;
        }


        DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference("Content");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Content").child(uid);
        contentRef.orderByChild("timestamp").equalTo(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Get data from snapshot
                    String contentTitle = ds.child("title").getValue(String.class);
                    String contentDescription = ds.child("description").getValue(String.class);
                    String contentPdfUrl = ds.child("url").getValue(String.class);
                    Long timestamp = ds.child("timestamp").getValue(Long.class);

                    // Create ModelContent object and add to ContentArrayList
                    ModelContent modelContent = new ModelContent(contentTitle, contentDescription, contentPdfUrl, timestamp);

                    ContentArrayList.add(modelContent);
                }

                // Display the first item in the list (or change this logic based on your requirement)
                if (!ContentArrayList.isEmpty()) {
                    modelContent = ContentArrayList.get(0);
                    // Store the current content
                    displayContentForEdit( modelContent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
                Toast.makeText(EditContentInfo.this, "Failed to load content for edit.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayContentForEdit(ModelContent content) {

        if (content != null) {
            binding.titleEt.setText(content.getContentTitle());
            binding.descriptionEt.setText(content.getContentDescription());
            loadPdfFromUrl(content.getContentPdf());
        }
    }

    private String title = "", description = "";

    private void validateData() {
        Log.d(TAG, "validateData: validating data...");

        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();


        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Pick PDF...", Toast.LENGTH_SHORT).show();
        } else {
            uploadPdfToStorage(title,description,  modelContent);
        }
    }

    private void uploadPdfToStorage(String title, String description, ModelContent currentContent) {
        Log.d(TAG, "uploadPdfToStorage: uploading to storage...");
        progressDialog.setMessage("Uploading Content...");
        progressDialog.show();

        String uid = firebaseAuth.getUid();

        long timestamp = currentContent.getTimestamp();
        String pdfFileName = "Content/" + timestamp + ".pdf";
        StorageReference pdfStorageReference = FirebaseStorage.getInstance().getReference().child("content").child(pdfFileName);

        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Content");
        profileRef.orderByChild("timestamp").equalTo(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Step 2: Delete the existing profile node if found
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
                // Upload PDF to Firebase Storage
                pdfStorageReference.putFile(pdfUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d(TAG, "onSuccess: PDF uploaded to storage...");
                            Log.d(TAG, "onSuccess: getting pdf url...");

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask.addOnSuccessListener(uri -> {
                                String uploadPdfUrl = uri.toString();
                                updateContentInfoInDB(currentContent,uploadPdfUrl, timestamp, title, description);
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Log.d(TAG, "onFailure: Failed to get PDF URL due to " + e.getMessage());
                                Toast.makeText(EditContentInfo.this, "Failed to get PDF URL due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "onFailure: PDF upload failed due to " + e.getMessage());
                            Toast.makeText(EditContentInfo.this, "PDF upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.d(TAG, "onCancelled: Failed to query existing profile due to " + databaseError.getMessage());
                Toast.makeText(EditContentInfo.this, "Failed to query existing profile due to " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateContentInfoInDB(ModelContent CurrentContent, String uploadPdfUrl, long timestamp, String title, String description) {
        Log.d(TAG, "updateContentInfoInDB: uploading content info to firebase db...");
        progressDialog.setMessage("Updating Content Info...");
        String uid = firebaseAuth.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Content");

        // Update the specific node identified by the timestamp
        ref.orderByChild("timestamp").equalTo(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Step 2: Delete the existing profile node if found

                // Step 3: Upload the new profile info to the database
                String profileId = ref.push().getKey();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", title);
                hashMap.put("description", description);
                hashMap.put("url", uploadPdfUrl);
                hashMap.put("timestamp", timestamp);
                hashMap.put("uid", uid);


                ref.child(String.valueOf(timestamp))
                        .setValue(hashMap)
                        .addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "onSuccess: Successfully uploaded...");
                            Toast.makeText(EditContentInfo.this, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
                            // Reset pdfUri after successful upload
                            pdfUri = null;
                            startActivity(new Intent(EditContentInfo.this, ProfileLayout.class));
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.d(TAG, "onFailure: Failed to upload to db due to " + e.getMessage());
                            Toast.makeText(EditContentInfo.this, "Failed to upload to db due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.d(TAG, "onCancelled: Failed to query existing profile due to " + databaseError.getMessage());
                Toast.makeText(EditContentInfo.this, "Failed to query existing profile due to " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(EditContentInfo.this, "Loading Image", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PDF_PICK_CODE && data != null && data.getData() != null) {
            Log.d(TAG, "onActivityResult: PDF Picked");
            pdfUri = data.getData();
            Log.d(TAG, "onActivityResult: URI: " + pdfUri);


            long timestamp = System.currentTimeMillis();;
            String pdfFileName = "Content/" + timestamp + ".pdf";
            StorageReference pdfStorageReference = FirebaseStorage.getInstance().getReference().child("content").child(pdfFileName);

            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Content");
            profileRef.orderByChild("timestamp").equalTo(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Step 2: Delete the existing profile node if found
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().removeValue();
                    }

                    // Upload PDF to Firebase Storage
                    pdfStorageReference.putFile(pdfUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                Log.d(TAG, "onSuccess: PDF uploaded to storage...");
                                Log.d(TAG, "onSuccess: getting pdf url...");

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                uriTask.addOnSuccessListener(uri -> {

                                    String uploadPdfUrl = uri.toString();
                                    loadPdfFromUrl(uploadPdfUrl);

                                }).addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Log.d(TAG, "onFailure: Failed to get PDF URL due to " + e.getMessage());
                                    Toast.makeText(EditContentInfo.this, "Failed to get PDF URL due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Log.d(TAG, "onFailure: PDF upload failed due to " + e.getMessage());
                                Toast.makeText(EditContentInfo.this, "PDF upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onCancelled: Failed to query existing profile due to " + databaseError.getMessage());
                    Toast.makeText(EditContentInfo.this, "Failed to query existing profile due to " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        } else {
            Log.d(TAG, "onActivityResult: cancelled picking pdf");
            Toast.makeText(this, "Cancelled picking PDF", Toast.LENGTH_SHORT).show();
        }
    }






    private void loadPdfFromUrl(String pdfUrl) {
        Log.d(TAG, "PDF URL: " + pdfUrl); // Log the URL

        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Log.e(TAG, "PDF URL is null or empty");
            return;
        }

        binding.ContentPdf.recycle();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Log.d(TAG, "onSuccess: PDF successfully loaded");

            // Set PDF bytes to PDFView
            binding.ContentPdf.fromBytes(bytes)
                    .pages(0) // Show only the first page

                    .swipeHorizontal(false)
                    .enableSwipe(false)
                    .onError(t -> {
                        Log.d(TAG, "onError: " + t.getMessage());
                    })
                    .onLoad(nbPages -> {
                        Log.d(TAG, "LoadComplete: PDF loaded");
                    })
                    .load();
        }).addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: Failed to load PDF from URL due to " + e.getMessage());
        });
    }
}