package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.ordernow.R;

public class Settings extends AppCompatActivity {

    Button feedbackbtn;
    Button inviteBttn;
    Button adBttn;
    Button privSecBttn;
    Button paymentBttn;
    ImageView backBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        feedbackbtn = findViewById(R.id.FeedbackSettBttn);
        inviteBttn = findViewById(R.id.InviteSettBttn);
        adBttn = findViewById(R.id.AdSettBttn);
        privSecBttn = findViewById(R.id.PrivSettBttn);
        paymentBttn = findViewById(R.id.PaymentSettBttn);
        backBttn = findViewById(R.id.backBttnSett);

        if (feedbackbtn == null) {
            Log.e("Settings", "feedback button not found!");
        }
        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, FeedBack.class);
                startActivity(toSignUp);
            }
        });

        if (paymentBttn == null) {
            Log.e("Settings", "payment button not found!");
        }
        paymentBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, Payment.class);
                startActivity(toSignUp);
            }
        });

        if (privSecBttn == null) {
            Log.e("Settings", "privacy and security button not found!");
        }
         privSecBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, Settings_PS.class);
                startActivity(toSignUp);
            }
        });

        if (adBttn == null) {
            Log.e("Settings", "ad settings button not found!");
        }
        adBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, Ad_Settings.class);
                startActivity(toSignUp);
            }
        });


        if (inviteBttn == null) {
            Log.e("Settings", "invite friends button not found!");
        }
        inviteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, InviteSetting.class);
                startActivity(toSignUp);
            }
        });


        if (backBttn == null) {
            Log.e("Settings", "feedback button not found!");
        }
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}