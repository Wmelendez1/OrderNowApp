package com.example.ordernow.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ordernow.R;
import com.example.ordernow.Domain.BusinessDomain;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddBusiness extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner categorySpinner;
    private String[] categories;
    private boolean[] checkedCategories;
    private ArrayList<Integer> selectedCategories = new ArrayList<>();
    private Uri imageUri;
    private String selectedImageUrl;
    private TextInputEditText businessNameEditText;
    private AutoCompleteTextView businessAddressEditText;
    private TextInputEditText businessPhoneEditText;
    private Button confirmBtn;
    private ImageButton addBusinessPic;
    private AutocompleteSessionToken sessionToken;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_business);

        //places sdk and firebaseAPI key
        String firebaseApiKey = getString(R.string.firebaseApiKey);
        Places.initialize(getApplicationContext(), firebaseApiKey);
        placesClient = Places.createClient(this);
        sessionToken = AutocompleteSessionToken.newInstance();

        //init ui members
        categorySpinner = findViewById(R.id.categorySpinner);
        businessNameEditText = findViewById(R.id.businessName).findViewById(R.id.businessNameEdit);
        businessAddressEditText = findViewById(R.id.businessAddressEdit);
        businessPhoneEditText = findViewById(R.id.businessPhone).findViewById(R.id.businesssPhoneEdit);
        confirmBtn = findViewById(R.id.confirmBtn);
        addBusinessPic = findViewById(R.id.addBusinessPic);

        //initialize catergories from string array
        categories = getResources().getStringArray(R.array.business_categories);
        checkedCategories = new boolean[categories.length];

        categorySpinner = findViewById(R.id.categorySpinner);

        //set adapter to display spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, new String[]{"Which categories best describe your business?"});
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        categorySpinner.setAdapter(adapter);

        //set a click listener to show the multi-select dialog
        categorySpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showMultiSelectDialog();
            }
            return true;
        });

        //handle pic selection
        addBusinessPic.setOnClickListener(v -> openPicSelect());

        //save data button
        confirmBtn.setOnClickListener(v -> saveBusinessData());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_addBusiness), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        businessAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    getAutocompleteSuggestions(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    //fetch address suggestions
    private void getAutocompleteSuggestions(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            List<String> suggestions = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                suggestions.add(prediction.getFullText(null).toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
            businessAddressEditText.setAdapter(adapter);
            businessAddressEditText.showDropDown();
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void openPicSelect() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(addBusinessPic); //load pic to display
        }

        uploadPic();
    }

    private void uploadPic() {
        if (imageUri != null) {
            //create unique id fo pic
            String fileName = UUID.randomUUID().toString();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("business_images/" + fileName);

            //uploads pic
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        //get download url
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            selectedImageUrl = uri.toString(); //save the download URL
                            Toast.makeText(AddBusiness.this, "Photo saved!", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddBusiness.this, "Failed to upload photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveBusinessData() {
        String businessName = businessNameEditText.getText().toString();
        String businessAddress = businessAddressEditText.getText().toString();
        String businessPhone = businessPhoneEditText.getText().toString();

        //reference to firestore db
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        //document ref for business
        DocumentReference businessRef = firestore.collection("businesses").document();

        //create map to business data to upload
        Map<String, Object> businessData = new HashMap<>();
        businessData.put("businessName", businessName);
        businessData.put("businessAddress", businessAddress);
        businessData.put("businessPhone", businessPhone);
        businessData.put("businessCategories", getSelectedCategories());
        businessData.put("businessPic", selectedImageUrl);

        //save data to db under businesses collection
        firestore.collection("businesses").add(businessData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddBusiness.this, "Business data saved successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddBusiness.this, BusinessMngrHomePage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddBusiness.this, "Failed to save business data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showMultiSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Categories");

        builder.setMultiChoiceItems(categories, checkedCategories, (dialog, which, isChecked) -> {
            if (isChecked) {
                selectedCategories.add(which); //add to arraylist
            } else {
                selectedCategories.remove(Integer.valueOf(which)); //dont add to arraylist
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            StringBuilder selectedCategoriesString = new StringBuilder();
            for (int i = 0; i < selectedCategories.size(); i++) {
                selectedCategoriesString.append(categories[selectedCategories.get(i)]);
                if (i != selectedCategories.size() - 1) {
                    selectedCategoriesString.append(", "); //add commas on selected categories
                }
            }

            //adapter to display selected categories
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.custom_spinner_item,
                    new String[]{selectedCategoriesString.toString()});
            adapter.setDropDownViewResource(R.layout.custom_spinner_item);
            categorySpinner.setAdapter(adapter);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getSelectedCategories() {
        StringBuilder selectedCategoriesString = new StringBuilder();
        for (int i = 0; i < selectedCategories.size(); i++) {
            selectedCategoriesString.append(categories[selectedCategories.get(i)]);
            if (i != selectedCategories.size() - 1) {
                selectedCategoriesString.append(", ");
            }
        }
        return selectedCategoriesString.toString();
    }
}