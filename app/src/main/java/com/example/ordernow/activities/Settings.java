package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
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


        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, FeedBack.class);
                startActivity(toSignUp);
            }
        });

        paymentBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, Payment.class);
                startActivity(toSignUp);
            }
        });

         privSecBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, Settings_PS.class);
                startActivity(toSignUp);
            }
        });

        adBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, Ad_Settings.class);
                startActivity(toSignUp);
            }
        });

        inviteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, InviteSetting.class);
                startActivity(toSignUp);
            }
        });

        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}