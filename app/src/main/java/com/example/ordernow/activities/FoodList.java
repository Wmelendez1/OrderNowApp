package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordernow.Adapter.CatergoryAdapter;
import com.example.ordernow.Adapter.FoodListAdapter;
import com.example.ordernow.Domain.FoodListDomain;
import com.example.ordernow.R;

import java.util.ArrayList;

public class FoodList extends AppCompatActivity {

    private RecyclerView.Adapter adapterfoodList;
    private int restaurantId; //use in future
    private String restaurantName;
    private ImageView foodlistbackButton;
    private ImageView restaurantLogo;
    private TextView viewcartBtn;
    private TextView foodPlaceNameTextView;
    private RecyclerView recyclerView;
    private String restaurantLogoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_list);

        //initialize members here
        foodlistbackButton = findViewById(R.id.foodlistbackButton);
        foodPlaceNameTextView = findViewById(R.id.foodplaceName);
        recyclerView = findViewById(R.id.foodlistrecyclerview);
        restaurantLogo = findViewById(R.id.foodlisttoolbar);
        viewcartBtn = findViewById(R.id.viewCartBtn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.foodlistview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntentExtra();
        setupUI();

        foodlistbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodList.this, HomePage.class);
                startActivity(intent);
            }
        });

        viewcartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodList.this, CartList.class);
                startActivity(intent);
            }
        });
    }

    private void getIntentExtra() {
        restaurantId = getIntent().getIntExtra("RestaurantId", 0);
        restaurantName = getIntent().getStringExtra("RestaurantName");
        restaurantLogoUrl = getIntent().getStringExtra("RestaurantLogo");
    }

    private void setupUI() {
        foodPlaceNameTextView.setText(restaurantName);

        Glide.with(this)
                .load((restaurantLogoUrl))
                .into(restaurantLogo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchMenu();
    }

    private void fetchMenu() {
      //TODO: fetch items from FoodListAdapter
        ArrayList<FoodListDomain> menuItems = null;

        if (restaurantName != null) {
            if (restaurantName.equals("McDonald's")) {
                menuItems = getMenu1();
            } else if (restaurantName.equals("Wendy's")) {
                menuItems = getMenu2();
            } else {
                //handle uknown errors
                
            }

            if (menuItems != null) {
                adapterfoodList = new FoodListAdapter(menuItems);
                recyclerView.setAdapter(adapterfoodList);
            }
        } else {
            //handle case where restaurant name is null
            finish();
        }
    }

    private ArrayList<FoodListDomain> getMenu1() {
        ArrayList<FoodListDomain> menuItems = new ArrayList<>();

        menuItems.add(new FoodListDomain("Big Mac", "burgerz", "BARger", 4.99));
        menuItems.add(new FoodListDomain("French Fries", "fries", "yes fries", 2.49));

        return menuItems;
    }

    private ArrayList<FoodListDomain> getMenu2() {
        ArrayList<FoodListDomain> menuItems = new ArrayList<>();

        menuItems.add(new FoodListDomain("Wendymac", "burgerz", "BARger", 2.99));
        menuItems.add(new FoodListDomain("Australian Fries", "fries", "yes fries", 1.49));

        return menuItems;
    }
}