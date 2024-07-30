package com.example.ordernow.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private View orderTrackerLayout;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewFoodNearYou;
    private AutoCompleteTextView enterAddress;
    private ImageView whitePin;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private ImageView navMenu;
    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        //initialize members here
        orderTrackerLayout = findViewById(R.id.orderStatusTextView);
        enterAddress = findViewById(R.id.enteraddress);
        whitePin = findViewById(R.id.whitepin);
        navMenu = findViewById(R.id.navmenu);
        drawerLayout = findViewById(R.id.drawer_layout);

        //places sdk and firebaseAPI key
        String firebaseApiKey = getString(R.string.firebaseApiKey);
        Places.initialize(getApplicationContext(), firebaseApiKey);
        placesClient = Places.createClient(this);
        sessionToken = AutocompleteSessionToken.newInstance();

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

        //watch text for changes
        enterAddress.addTextChangedListener(new TextWatcher() {
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

        //open nav drawer
        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    //handle navigation
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //handle menu item click
        if (id == R.id.profilenav) {
            //TODO: start profile activity here
            return true;
        } else if (id == R.id.pastordersnav) {
            //TODO: past orders activity here
            return true;
        } else if (id == R.id.alertsnav) {
            //TODO: alerts activity here
            return true;
        } else if (id == R.id.favoritesnav) {
            //TODO: favorites activity here
            return true;
        } else if (id == R.id.promonav) {
            //TODO: promo activity here
            return true;
        } else if (id == R.id.settingsnav) {
            //TODO: settings activity here
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            enterAddress.setAdapter(adapter);
            enterAddress.showDropDown();
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    //setup recycle view to display categories horizontally
    private void recycleViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories = findViewById(R.id.categoryrecview);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        //add categories
        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Pizza", "pizza"));
        category.add(new CategoryDomain("Burger", "burger"));
        category.add(new CategoryDomain("Breakfast", "pancake"));
        category.add(new CategoryDomain("Chinese", "chinesefood"));
        category.add(new CategoryDomain("Fast Food", "fastfood"));

        adapter = new CatergoryAdapter(category);
        recyclerViewCategories.setAdapter(adapter);
    }

    //setup recycle view to display food near you horizontally
    private void recyclerViewFoodNearYou() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFoodNearYou = findViewById(R.id.foodnearyourecview);
        recyclerViewFoodNearYou.setLayoutManager(linearLayoutManager);

        //add stores
        ArrayList<FoodNearYouDomain> foodnearyou = new ArrayList<>();
        foodnearyou.add(new FoodNearYouDomain("mcdonaldss", "McDonald's", 4.3, "1.5 mi", "$1.99", "30-45 min"));
        foodnearyou.add(new FoodNearYouDomain("dominos", "Domino's", 4.0, "1.7 mi", "$1.99", "30-45 min"));
        foodnearyou.add(new FoodNearYouDomain("jollibee", "Jollibee", 5.0, "7.0", "$4.99", "50-60 min"));
        foodnearyou.add(new FoodNearYouDomain("tacobell", "Taco Bell", 3.7, "2.5 mi", "$2.99", "40-55 min"));
        foodnearyou.add(new FoodNearYouDomain("wendys", "Wendy's", 3.5, "0.7 mi", "$0.99", "10-20 min"));

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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}