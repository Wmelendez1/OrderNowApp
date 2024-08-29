package com.example.ordernow.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordernow.R;

public class FeedBack extends AppCompatActivity {

    ImageView backBttn;
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
        submitBttn = findViewById(R.id.SubmitBttnFeedBack);
        backBttn = findViewById(R.id.backBttnFeedB);  // You missed initializing this

        // Check if views are properly initialized
        if (ratingBar == null) {
            Log.e("FeedBack", "Rating bar not found!");
        }
        if (feedback == null) {
            Log.e("FeedBack", "EditText not found!");
        }
        if (submitBttn == null) {
            Log.e("FeedBack", "Submit button not found!");
        }
        if (backBttn == null) {
            Log.e("FeedBack", "Back button not found!");
        }



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