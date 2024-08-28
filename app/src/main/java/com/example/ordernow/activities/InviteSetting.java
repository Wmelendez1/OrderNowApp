package com.example.ordernow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordernow.R;

public class InviteSetting extends AppCompatActivity {

    ImageButton backBttn;

    Button copyBttn;

    EditText link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invite_setting);

        backBttn =  findViewById(R.id.backBttnIF);
        copyBttn = findViewById(R.id.copyLinkBttn);

        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        copyBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // copy link text to clipboard
            }
        });


    }
}