package com.example.ordernow.activities;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.example.ordernow.R;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

public class HomePage extends AppCompatActivity {

    private View orderTrackerLayout;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategories;
    private AutoCompleteTextView enterAddress;
    private ImageView whitePin;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //initialize members here
        orderTrackerLayout = findViewById(R.id.orderStatusTextView);
        enterAddress = findViewById(R.id.enteraddress);
        whitePin = findViewById(R.id.whitepin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homepage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //check for ongoing order status
        boolean hasOngoingOrder = checkForOngoingOrder();

        //show or hide order tracker based on ongoing order status
        if (hasOngoingOrder) {
            showOrderTracker();
        } else {
            hideOrderTracker();
        }

        recycleViewCategory();
    }

    //setup recycle view to display categories horizontally
    private void recycleViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories = findViewById(R.id.categoryrecview);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
    }

    //method to check if there's an ongoing order
    private boolean checkForOngoingOrder() {
        //return true; //for testing
        return false; //for testing
    }

    //show order tracker
    private void showOrderTracker() {
        orderTrackerLayout.setVisibility(View.VISIBLE);

        //get reference
        TextView orderStatusTextView = orderTrackerLayout.findViewById(R.id.orderStatusTextView);
        LottieAnimationView animationView = findViewById(R.id.animationView);

        //will create text and also image/gif if needed
        SpannableStringBuilder builder = new SpannableStringBuilder();

        //append the text
        builder.append("Preparing your order...");

        //start the animation
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();

        //set the text and (image/gif if needed)
        orderStatusTextView.setText(builder);
    }

    //hides order tracker
    private void hideOrderTracker() {
        orderTrackerLayout.setVisibility(View.GONE);
    }
}