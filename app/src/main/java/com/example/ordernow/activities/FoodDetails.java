package com.example.ordernow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ordernow.Domain.FoodListDomain;
import com.example.ordernow.Helper.ManagementCart;
import com.example.ordernow.R;

public class FoodDetails extends AppCompatActivity {

    private TextView addToCartbtn;
    private TextView foodname, price, description, itemCount;
    ImageView sdminusBtn, sdplusBtn, foodpic;
    private FoodListDomain object;
    int itemCounting = 1;

    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_details);

        //initialize members here
        addToCartbtn = findViewById(R.id.addToCartbtn);
        foodname = findViewById(R.id.foodname);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        itemCount = findViewById(R.id.itemCount);
        sdminusBtn = findViewById(R.id.sdminusBtn);
        sdplusBtn = findViewById(R.id.sdplusBtn);
        foodpic = findViewById(R.id.foodpic);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.foodDetailsview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managementCart = new ManagementCart(this);
        getBundle();
    }

    private void getBundle() {
        object = (FoodListDomain) getIntent().getSerializableExtra("object");

        int drawableResId = this.getResources().getIdentifier(object.getPic(), "drawable", this.getPackageName());

        Glide.with(this)
                .load(drawableResId)
                .into(foodpic);

        foodname.setText(object.getFoodname());
        price.setText("$"+ object.getPrice());
        description.setText(object.getDescription());

        sdplusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCounting = itemCounting + 1;
                itemCount.setText(String.valueOf(itemCounting));
            }
        });

        sdminusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemCounting > 1 ) {
                    itemCounting = itemCounting - 1;
                }

                itemCount.setText(String.valueOf(itemCounting));
            }
        });

        addToCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberInCart(itemCounting);
                managementCart.insertFoodItem(object);
            }
        });
    }
}