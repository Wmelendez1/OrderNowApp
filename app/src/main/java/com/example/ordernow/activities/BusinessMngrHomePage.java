package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.ordernow.databinding.ActivityBusinessMngrHomePageBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.ArrayList;

public class BusinessMngrHomePage extends AppCompatActivity {

    private TextView welcomeBackText;

    private ActivityBusinessMngrHomePageBinding Binding;

    private TextView firstNameText;
    private RecyclerView businessRecyclerView;
    private Button addBusinessButton;
    private ImageView addBusinessButtonHomePage;
    private BusinessHmpgAdapter businessHmpgAdapter;
    private ArrayList<BusinessDomain> businessList;
    private FirebaseFirestore firestore;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_mngr_home_page);

        Binding = ActivityBusinessMngrHomePageBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());


        Intent businessintent = getIntent();
        uid = businessintent.getStringExtra("uid");


        businessHmpgAdapter = new BusinessHmpgAdapter(businessList);

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
        fetchBusinessOwnersName(uid);


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

        fetchBusinessData(uid);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //reload business from AddBusiness activity
        fetchBusinessData(uid);
    }

    private void fetchBusinessData(String uid) {
        firestore.collection("businesses").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        businessList.clear(); //clear the list before adding new data
                        if (document.exists()) {
                            BusinessDomain business = document.toObject(BusinessDomain.class);
                            if (business != null) {
                                businessList.add(business);
                            }
                        businessHmpgAdapter.updateData(businessList);
                        businessHmpgAdapter.notifyDataSetChanged();

                        //show or hide the add business button based on the list
                        updateButtonVisibility();
                        } else {
                            // Handle case where document does not exist
                            Toast.makeText(BusinessMngrHomePage.this, "No business data found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(BusinessMngrHomePage.this, "Error getting businesses: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchBusinessOwnersName(String uid) {
        // Replace 'businessId' with the actual ID or reference to the document you need
        ; // This could come from an Intent or other source
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("businesses").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve the ownerFullName from the document
                            String ownerFullName = document.getString("businessOwnersName");
                            if (ownerFullName != null) {
                                Binding.firstname.setText(ownerFullName);
                            } else {
                                Binding.firstname.setText("No data found");
                            }
                        } else {
                            // Handle case where the document does not exist
                            Binding.firstname.setText("No data found");
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(BusinessMngrHomePage.this, "Error fetching ownerFullName: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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