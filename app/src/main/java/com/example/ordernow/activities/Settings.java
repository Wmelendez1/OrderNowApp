package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSignUp = new Intent(Settings.this, FeedBack.class);
                startActivity(toSignUp);
            }
        });

    }
}