package com.example.ordernow.activities;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.example.ordernow.activities.HomePage;

import java.sql.Array;
import java.util.ArrayList;

public class CategoriesPage extends AppCompatActivity {

    private View orderTrackerLayout;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewFoodNearYou;
    private AutoCompleteTextView enterAddress;
    private ImageView whitePin;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private EditText searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_page);
        searchBar = findViewById(R.id.search_text);

        ArrayList<FoodNearYouDomain> foodNearYouAdapterArrayList = HomePage.foodnearyou;
        FoodNearYouDomain x;
        if (foodNearYouAdapterArrayList != null){
            x = foodNearYouAdapterArrayList.get(1);
        }
        else {
            x = null;
        }
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText(x.getName());
            }
        });




    }
}
