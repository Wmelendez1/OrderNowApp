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
import com.example.ordernow.Adapter.CatergoryAdapter;
import com.example.ordernow.Adapter.FoodNearYouAdapter;
import com.example.ordernow.Domain.CategoryDomain;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.R;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.sql.Array;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private View orderTrackerLayout;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewFoodNearYou;
    private AutoCompleteTextView enterAddress;
    private ImageView whitePin;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    static ArrayList<FoodNearYouDomain> foodnearyou;


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
        recyclerViewFoodNearYou();
    }

    //setup recycle view to display categories horizontally
    private void recycleViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories = findViewById(R.id.categoryrecview);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        //add categories
        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Pizza", "Pizza","pizza"));
        category.add(new CategoryDomain("Burger", "Burger", "burger"));
        category.add(new CategoryDomain("Breakfast", "Breakfast","pancake"));
        category.add(new CategoryDomain("Chinese", "Chinese","chinesefood"));
        category.add(new CategoryDomain("Fast Food", "Fast Food","fastfood"));

        adapter = new CatergoryAdapter(category);
        recyclerViewCategories.setAdapter(adapter);
    }

    //setup recycle view to display food near you horizontally
    public void recyclerViewFoodNearYou() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFoodNearYou = findViewById(R.id.foodnearyourecview);
        recyclerViewFoodNearYou.setLayoutManager(linearLayoutManager);

        //add stores
        foodnearyou = new ArrayList<>();
        foodnearyou.add(new FoodNearYouDomain("mcdonaldss", "McDonald's", 4.3, "1.5 mi", "$1.99", "30-45 min"));


        adapter = new FoodNearYouAdapter(foodnearyou);
        recyclerViewFoodNearYou.setAdapter(adapter);
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