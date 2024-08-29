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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ordernow.R;
import com.example.ordernow.Domain.FoodListDomain;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddMenuItem extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String selectedImageUrl;
    private TextInputEditText foodNameEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText descriptionEditText;
    private Button confirmBtn;
    private ImageButton addMenuItemPic;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_menu_item);

        //fetch business ID from Intent
        businessId = getIntent().getStringExtra("businessId");

        //initialize UI members
        foodNameEditText = findViewById(R.id.foodname).findViewById(R.id.foodnameEdit);
        priceEditText = findViewById(R.id.price).findViewById(R.id.priceEdit);
        descriptionEditText = findViewById(R.id.description).findViewById(R.id.descriptionEdit);
        confirmBtn = findViewById(R.id.addMenuItemBtn);
        addMenuItemPic = findViewById(R.id.addMenuItemPic);

        //handle image selection
        addMenuItemPic.setOnClickListener(v -> openPicSelect());

        //save data button
        confirmBtn.setOnClickListener(v -> saveMenuItemData());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                    .into(addMenuItemPic); //load image to display
        }

        uploadPic();
    }

    private void uploadPic() {
        if (imageUri != null) {
            //create unique id for pic
            String fileName = UUID.randomUUID().toString();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("menu_item_images/" + fileName);

            //upload pic
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get download URL
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            selectedImageUrl = uri.toString(); // Save the download URL
                            Toast.makeText(AddMenuItem.this, "Photo saved!", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddMenuItem.this, "Failed to upload photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveMenuItemData() {
        String foodName = foodNameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String priceStr = priceEditText.getText().toString();
        Double price = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);

        if (foodName.isEmpty() || price == null) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //reference to firestore DB
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        //create map to menu item data to upload
        Map<String, Object> menuItemData = new HashMap<>();
        menuItemData.put("foodname", foodName);
        menuItemData.put("pic", selectedImageUrl);
        menuItemData.put("description", description);
        menuItemData.put("price", price);

        //save data to DB under the specific business id
        firestore.collection("businesses").document(businessId).collection("menu_items").add(menuItemData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddMenuItem.this, "Menu item saved successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddMenuItem.this, BusinessPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddMenuItem.this, "Failed to save menu item: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
