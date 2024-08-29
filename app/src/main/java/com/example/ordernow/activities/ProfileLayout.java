


package com.example.ordernow.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.R;
import com.example.ordernow.databinding.ActivityProfileLayoutBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ProfileLayout extends AppCompatActivity implements SelectListener {

    // View binding
    private ActivityProfileLayoutBinding binding;


    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference profileRef, contentRef, contentIdRef;
    private String id;

    private ArrayList<ModelContent> ContentArrayList, filterList;
    private AdapterContent adapterContent;


    // Profile information
    private String firstName, lastName, Age, email, username, bio, url, contentId, Content, description, title, uid, ContentUrl;
    private Uri pdfUri;
    private PDFView profilePicIV, ContentPdf;
    private static final String TAG = "PROFILE_TAG";





    private RecyclerView contentRecyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        String profileId = intent.getStringExtra("id");
        contentId = intent.getStringExtra("Content");
        title = intent.getStringExtra("title");



        firebaseAuth = FirebaseAuth.getInstance();
        profilePicIV = findViewById(R.id.profilePicIV);
        ContentPdf = findViewById(R.id.ContentPdf);
        profileRef = FirebaseDatabase.getInstance().getReference("Profiles");
        contentRef = FirebaseDatabase.getInstance().getReference("Content");

        contentRecyclerView = binding.Content;
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ContentArrayList = new ArrayList<>();
        adapterContent = new AdapterContent(this, ContentArrayList, this);
        contentRecyclerView.setAdapter(adapterContent);
        contentRecyclerView = findViewById(R.id.Content);
        contentRecyclerView.setHasFixedSize(true);






        // Example usage of getContentFilter() method
        Filter contentFilter = adapterContent.getContentFilter();



        retrieveProfileInfo(email);


        String usernameRef = username;




        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Profiles");


            profileRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String url = ds.child("url").getValue(String.class);
                            if (url != null) {

                                // Use the retrieved URL here
                                loadPdfFromUrl(url);

                                return; // Exit the loop after finding the URL
                            }
                        }
                        // If the loop finishes without finding a URL
                        Log.e(TAG, "URL not found for the user: " + user.getUid());
                        // Handle the case where the URL is not found
                    } else {
                        Log.e(TAG, "User profile not found in the database.");
                        // Handle the case where the user profile is not found
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "DatabaseError: " + error.getMessage());
                    // Handle the database error
                }
            });
        } else {
            Log.e(TAG, "No user is currently signed in.");
        }


        firebaseAuth = FirebaseAuth.getInstance();

        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                profileRef = FirebaseDatabase.getInstance().getReference("Profiles");
                retrieveProfileInfo(email);
            }
        } else {
            Log.e(TAG, "No user is currently signed in.");
        }



        if (user != null) {
            retrieveContentInfo();
            contentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String url = ds.child("url").getValue(String.class);
                        ModelContent modelContent = dataSnapshot.getValue(ModelContent.class);
                        ContentArrayList.add(modelContent);
                        loadPdfContentFromUrl();
                        loadContentList();

                    }


                    adapterContent.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "DatabaseError: " + error.getMessage());
                    // Handle the database error
                }
            });
        }




        binding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileLayout.this, EditProfileLayout.class));
            }
        });

        binding.AddProfileContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileLayout.this, AddContent.class));
            }
        });

        binding.HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileLayout.this, HomePage.class));
            }
        });


    }

    private void retrieveProfileInfo(String Email) {
        profileRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        url = ds.child("url").getValue(String.class);
                        String fullname = firstName + " " + lastName;

                        // Log the retrieved data to verify
                        Log.d(TAG, "First Name: " + firstName);
                        Log.d(TAG, "Last Name: " + lastName);
                        Log.d(TAG, "Age: " + Age);
                        Log.d(TAG, "Email: " + email);
                        Log.d(TAG, "Bio: " + bio);
                        Log.d(TAG, "username: " + username);

                        // Update UI with retrieved profile information
                        binding.firstnameET.setText(fullname);
                        binding.aboutmeET.setText(bio);
                        binding.username.setText(username);


                        // Retrieve and update content information



                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });


    }


    private void retrieveContentInfo() {
        contentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ContentArrayList.clear(); // Clear the list before adding new data

                for (DataSnapshot profileSnapshot : snapshot.getChildren()) {
                    DataSnapshot contentSnapshot = profileSnapshot.child("Content");
                    for (DataSnapshot ds : contentSnapshot.getChildren()) {
                        String contentTitle = ds.child("title").getValue(String.class);
                        String contentDescription = ds.child("description").getValue(String.class);
                        String contentPdfUrl = ds.child("url").getValue(String.class);
                        long timestamp = ds.child("timestamp").getValue(Long.class); // Retrieve the timestamp

                        // Create ModelContent object and add to contentArrayList
                        ModelContent modelContent = new ModelContent();
                        modelContent.setContentTitle(contentTitle);
                        modelContent.setContentDescription(contentDescription);
                        modelContent.setContentPdf(contentPdfUrl);
                        modelContent.setTimestamp(timestamp); // Set the timestamp

                        ContentArrayList.add(modelContent);
                    }
                }
                adapterContent.notifyDataSetChanged(); // Notify adapter after data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }


    private void loadPdfFromUrl(String pdfUrl) {

        Log.d(TAG, "PDF URL: " + pdfUrl); // Log the URL

        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Log.e(TAG, "PDF URL is null or empty");
            return;
        }


        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Log.d(TAG, "onSuccess: PDF successfully loaded");

            // Set PDF bytes to PDFView
            profilePicIV.fromBytes(bytes)
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


    private void loadPdfContentFromUrl() {
        if (ContentArrayList.isEmpty()) {
            Log.e(TAG, "ContentArrayList is empty, cannot load PDF");
            return;
        }

        // Get the first item from the list
        ModelContent firstContent = ContentArrayList.get(0); // Change this logic based on your requirements

        if (firstContent == null) {
            Log.e(TAG, "First content item is null, cannot load PDF");
            return;
        }

        String contentPdfUrl = firstContent.getContentPdf();

        if (contentPdfUrl == null || contentPdfUrl.isEmpty()) {
            Log.e(TAG, "PDF URL is null or empty");
            return;
        }

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(contentPdfUrl);
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Log.d(TAG, "onSuccess: PDF successfully loaded");

            // Set PDF bytes to ContentPdf
            ContentPdf.fromBytes(bytes)
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
    private void loadContentList() {
        ContentArrayList.clear(); // Clear existing data if needed

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Content");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Get data
                    String contentTitle = ds.child("title").getValue(String.class);
                    String contentDescription = ds.child("description").getValue(String.class);
                    String contentPdf = ds.child("url").getValue(String.class);
                    Long timestamp = ds.child("timestamp").getValue(Long.class); // Changed to Long

                    // Create ModelContent object
                    ModelContent model = new ModelContent(contentTitle, contentDescription, contentPdf, timestamp);

                    // Add to list
                    ContentArrayList.add(model);
                }

                // Notify adapter of data change
                adapterContent.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }



    @Override
    public void onItemClicked(ModelContent modelContent) {
        // Pass the whole ModelContent object to EditContentInfo
        Intent intent = new Intent(ProfileLayout.this, EditContentInfo.class);
        intent.putExtra("modelContent", modelContent); // Pass the object
        startActivity(intent);
    }


}