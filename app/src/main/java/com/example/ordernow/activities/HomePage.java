package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;

import java.sql.Array;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    private Button nav;
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
        setContentView(R.layout.navigation_layout);

        //initialize members here
        orderTrackerLayout = findViewById(R.id.orderStatusTextView);
        enterAddress = findViewById(R.id.enteraddress);
        whitePin = findViewById(R.id.whitepin);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView navMenu = findViewById(R.id.navmenu);

        if (drawerLayout == null) {
            Log.e("HomePage", "DrawerLayout not found!");
        }

        if (navigationView == null) {
            Log.e("HomePage", "NavigationView not found!");
        }


        if (navMenu != null) {
            navMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        } else {
            Log.e("HomePage", "navMenu ImageView not Found!");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.profilenav) {
                    // Handle the profile action
                    openProfile();
                } else if (id == R.id.pastordersnav) {
                    // Handle the past orders action
                    openPastOrders();
                } else if (id == R.id.alertsnav) {
                    // Handle the alerts action
                    openAlerts();
                } else if (id == R.id.favoritesnav) {
                    // Handle the favorites action
                    openFavorites();
                } else if (id == R.id.promonav) {
                    // Handle the promotions action
                    openPromotions();
                } else if (id == R.id.settingsnav) {
                    // Handle the settings action
                    openSettings();
                }

                // Close the drawer after an item is clicked
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
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

    private void openPromotions() {

    }

    private void openFavorites() {
        Intent intent = new Intent(this, Favorites.class);
        startActivity(intent);
    }

    private void openAlerts() {
        Intent intent = new Intent(this, Alerts.class);
        startActivity(intent);
    }

    private void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void openPastOrders() {
        Intent intent = new Intent(this, PastOrders.class);
        startActivity(intent);

    }

    private void openProfile() {
        Intent intent = new Intent(this, ProfileLayout.class);
        startActivity(intent);

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