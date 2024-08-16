package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.databinding.ActivityProfileLayoutBinding;
import com.example.ordernow.databinding.AddcontentrowBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity implements SelectListener {

    //view binding
    private ActivityProfileLayoutBinding binding;

    //arrayList to hold list of data of type ModelPdf
    private ArrayList<ModelContent> ContentArrayList;

    //adapter
    private AdapterContent adapterContent;

    private String contentId, contentTitle;

    private static final String TAG = "PDF_LIST_TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadPdfList();
        //get data from intent
        Intent intent = getIntent();
        contentId = intent.getStringExtra("contentId");
        contentTitle = intent.getStringExtra("contentTitle");

        //set pdf category


        loadPdfList();

        //search




    }

    private void loadPdfList() {
        //init list before adding data
        ContentArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Content");
        ref.orderByChild("contentId").equalTo(contentId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ContentArrayList.clear();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            //get data
                            ModelContent model = ds.getValue(ModelContent.class);

                            //add to list
                            ContentArrayList.add(model);


                        }
                        //setup adapter
                        adapterContent = new AdapterContent(ContentListActivity.this, ContentArrayList,ContentListActivity.this  );
                        binding.Content.setAdapter(adapterContent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
    }



    @Override
    public void onItemClicked(ModelContent modelContent) {


        long timestamp = modelContent.getTimestamp();

        // Pass the timestamp to the EditContentInfo activity
        Intent intent = new Intent(ContentListActivity.this, EditContentInfo.class);
        intent.putExtra("modelContent", modelContent); // Pass the object
        startActivity(intent);
    }

}