package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.Adapter.BusinessHmpgAdapter;
import com.example.ordernow.Domain.BusinessDomain;
import com.example.ordernow.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class BusinessMngrHomePage extends AppCompatActivity {

    private TextView welcomeBackText;
    private TextView firstNameText;
    private RecyclerView businessRecyclerView;
    private Button addBusinessButton;
    private ImageView addBusinessButtonHomePage;
    private BusinessHmpgAdapter businessHmpgAdapter;
    private ArrayList<BusinessDomain> businessList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_mngr_home_page);

        //initialize ui components
        welcomeBackText = findViewById(R.id.welcomebacktext);
        firstNameText = findViewById(R.id.firstname);
        businessRecyclerView = findViewById(R.id.businesshmpgrecview);
        addBusinessButton = findViewById(R.id.addBusinessbtn);
        addBusinessButtonHomePage = findViewById(R.id.addBusinessbtnHomePage);

        //initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        //set up recylcer view
        businessList = new ArrayList<>();
        businessHmpgAdapter = new BusinessHmpgAdapter(businessList);
        businessRecyclerView.setAdapter(businessHmpgAdapter);
        businessRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addBusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessMngrHomePage.this, AddBusiness.class);
                startActivity(intent);
            }
        });

        addBusinessButtonHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessMngrHomePage.this, AddBusiness.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchBusinessData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //reload business from AddBusiness activity
        fetchBusinessData();
    }

    private void fetchBusinessData() {
        firestore.collection("businesses")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        businessList.clear(); //clear the list before adding new data
                        for (DocumentSnapshot document : task.getResult()) {
                            BusinessDomain business = document.toObject(BusinessDomain.class);
                            businessList.add(business);
                        }
                        businessHmpgAdapter.updateData(businessList);

                        businessHmpgAdapter.notifyDataSetChanged();

                        //show or hide the add business button based on the list
                        updateButtonVisibility();

                    } else {
                        Toast.makeText(BusinessMngrHomePage.this, "Error getting businesses: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //businesses will be visible ONLY IF list is populated
    private void updateButtonVisibility() {
        if (businessList.isEmpty()) {
            addBusinessButton.setVisibility(View.VISIBLE);
            addBusinessButtonHomePage.setVisibility(View.GONE);
        } else {
            addBusinessButton.setVisibility(View.GONE);
            addBusinessButtonHomePage.setVisibility(View.VISIBLE);
        }
    }
}