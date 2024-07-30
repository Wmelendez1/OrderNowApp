package com.example.ordernow.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ordernow.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import Adapters.OrderAdapter;
import Models.Order;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PastOrders extends AppCompatActivity {

    private static final String DEFAULT_DATE_STRING = "0000-00-00";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Order> orders = new ArrayList<>();
        // Add sample data
        try {
            Date defaultDate = DATE_FORMAT.parse(DEFAULT_DATE_STRING);
            orders.add(new Order("McDonalds", "Big Mac Meal with Large Fries and Coke", new Date()));
            orders.add(new Order("Dunkin", " Small Carmel Iced Coffee 3 creams 3 sugars", defaultDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Add more orders as needed

        OrderAdapter adapter = new OrderAdapter(orders);
        recyclerView.setAdapter(adapter);
    }
}
