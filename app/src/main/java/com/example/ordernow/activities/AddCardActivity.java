package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ordernow.R;

import Models.Card;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        EditText cardHolderNameEditText = findViewById(R.id.cardHolderNameEditText);
        EditText cardNumberEditText = findViewById(R.id.cardNumberEditText);
        EditText expiryDateEditText = findViewById(R.id.expiryDateEditText);
        EditText cvvEditText = findViewById(R.id.cvvEditText);
        Button saveCardButton = findViewById(R.id.saveCardButton);

        saveCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardHolderName = cardHolderNameEditText.getText().toString();
                String cardNumber = cardNumberEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();

                // Create a new card object (for simplicity, card number is masked)
                Card newCard = new Card("**** " + cardNumber.substring(cardNumber.length() - 4), false, cardHolderName, expiryDate);

                // Return the new card to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newCard", newCard);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close the activity
            }
        });
    }
}