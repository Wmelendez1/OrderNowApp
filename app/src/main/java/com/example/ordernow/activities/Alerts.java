package com.example.ordernow.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.R;
import com.example.ordernow.Adapter.AlertAdapter;
import com.example.ordernow.Models.AlertItem;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Alerts extends AppCompatActivity {

    public static List<AlertItem> alertList = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerViewAlerts;
    private AlertAdapter alertsAdapter;


    ImageButton backBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        Toolbar toolbar = findViewById(R.id.Alert_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backBttn = findViewById(R.id.backBttnAlerts);
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerViewAlerts = findViewById(R.id.Alert_recyclerView);
        recyclerViewAlerts.setLayoutManager(new LinearLayoutManager(this));

        alertList = Alerts.alertList;

        // Set up the adapter with the alerts list
        alertsAdapter = new AlertAdapter(alertList);
        recyclerViewAlerts.setAdapter(alertsAdapter);

        // Apply insets to the layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            int left = insets.getInsets(WindowInsetsCompat.Type.systemBars()).left;
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int right = insets.getInsets(WindowInsetsCompat.Type.systemBars()).right;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(left, top, right, bottom);
            return WindowInsetsCompat.CONSUMED;
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Static method to add alerts to the list
    public static void addAlert(String title, String message) {
        AlertItem newAlert = new AlertItem(title, message);
        alertList.add(newAlert);
    }
}

