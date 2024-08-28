package com.example.ordernow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordernow.R;

public class FeedBack extends AppCompatActivity {

    Button backBttn;
    Button submitBttn;
    RatingBar ratingBar;
    EditText feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_back);

        ratingBar = findViewById(R.id.ratingBar);
        feedback = findViewById(R.id.feedbackEMT);

        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add save to database.

                int starRating = ratingBar.getNumStars();

                String savedFeedback = feedback.getText().toString();
            }
        });

    }
}