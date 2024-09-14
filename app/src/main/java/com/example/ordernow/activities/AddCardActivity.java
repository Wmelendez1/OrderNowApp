package com.example.ordernow.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordernow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;

public class AddCardActivity extends AppCompatActivity {

    private Stripe stripe;
    private CardInputWidget cardInputWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        // Initialize Stripe and CardInputWidget
        cardInputWidget = findViewById(R.id.cardInputWidget);
        stripe = new Stripe(this, PaymentConfiguration.getInstance(this).getPublishableKey());

        Button saveCardButton = findViewById(R.id.saveCardButton);
        saveCardButton.setOnClickListener(v -> {
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                // Create a PaymentMethod using Stripe's API
                stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                    @Override
                    public void onSuccess(@NonNull PaymentMethod paymentMethod) {
                        // You get the PaymentMethod ID and card details here
                        String paymentMethodId = paymentMethod.id;
                        String last4 = paymentMethod.card != null ? paymentMethod.card.last4 : "";
                        Integer expiryMonth = paymentMethod.card != null ? paymentMethod.card.expiryMonth : null;
                        Integer expiryYear = paymentMethod.card != null ? paymentMethod.card.expiryYear : null;

                        // Store the card details in Firebase
                        storePaymentMethodInFirebase(paymentMethodId, last4, expiryMonth, expiryYear);
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        // Handle error during PaymentMethod creation
                        Toast.makeText(AddCardActivity.this, "Failed to save card", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(AddCardActivity.this, "Invalid card data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Store the Stripe PaymentMethod ID and other card details in Firebase.
     * @param paymentMethodId Stripe's PaymentMethod ID for the created card
     * @param last4 Last 4 digits of the card number
     * @param expiryMonth Card expiration month
     * @param expiryYear Card expiration year
     */
    private void storePaymentMethodInFirebase(String paymentMethodId, String last4, Integer expiryMonth, Integer expiryYear) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference userCardsRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Cards");

        // Prepare card details map for Firebase
        Map<String, Object> cardData = new HashMap<>();
        cardData.put("paymentMethodId", paymentMethodId); // Store only the PaymentMethod ID
        cardData.put("last4", last4);
        cardData.put("expiryDate", expiryMonth + "/" + expiryYear);
        cardData.put("isDefault", false); // Default flag (can set true later for default card)

        // Push the card details to Firebase
        userCardsRef.push().setValue(cardData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddCardActivity.this, "Card saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCardActivity.this, "Failed to save card", Toast.LENGTH_SHORT).show();
                });
    }
}
