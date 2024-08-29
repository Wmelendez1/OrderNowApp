package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ordernow.R;


public class BusinessPage extends AppCompatActivity {
    private ImageView businessPicImageView;
    private ImageView backButton;
    private TextView addMenuItemBtn;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_page);

        //retrieve data passed from intent
        String businessPicUrl = getIntent().getStringExtra("businessPicUrl");

        businessPicImageView = findViewById(R.id.businessPagetoolbar);
        backButton = findViewById(R.id.businessPagebackButton);
        addMenuItemBtn = findViewById(R.id.addFoodItemButton);

        //load data into the views
        Glide.with(this)
                .load(businessPicUrl)
                .into(businessPicImageView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.businessPageView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //go back to business homepage
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BusinessPage.this, BusinessMngrHomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        //go to add menu item
        addMenuItemBtn.setOnClickListener(v -> {
            Intent intent = new Intent(BusinessPage.this, AddMenuItem.class);
            intent.putExtra("businessId", businessId);
            startActivity(intent);
        });
    }
}