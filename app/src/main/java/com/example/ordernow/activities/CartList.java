package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.Helper.ManagementCart;
import com.example.ordernow.R;

public class CartList extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;
    TextView subTotal, deliveryFee, taxes, totalPrice, cartemptyText;
    private double tax;
    private ScrollView scrollView;
    private ImageView cartbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_list);

        //initialize members here
        recyclerView = findViewById(R.id.cartrecyclerview);
        subTotal = findViewById(R.id.subTotal);
        deliveryFee = findViewById(R.id.deliveryFee);
        taxes = findViewById(R.id.taxes);
        totalPrice = findViewById(R.id.totalPrice);
        cartemptyText = findViewById(R.id.cartemptyText);
        scrollView = findViewById(R.id.cartscrollview);
        cartbackButton = findViewById(R.id.cartbackButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managementCart = new ManagementCart(this);

        cartbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartList.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}