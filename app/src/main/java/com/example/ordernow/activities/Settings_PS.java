package com.example.ordernow.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ordernow.R;

public class Settings_PS extends AppCompatActivity {

    TextView privacyPolicyTextView;
    SpannableString spannableString;
    ImageButton backBttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_ps);
        backBttn = findViewById(R.id.backBttnPS);
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        privacyPolicyTextView= findViewById(R.id.locationDisclaimerTV);
        String text = "To ensure the optimal functionality and user experience of our app, it is essential that your device's location settings are turned on. By using this app, you acknowledge and agree that enabling location services is necessary for certain features and services to operate correctly. Your location data will be used in accordance with our Privacy Policy to provide personalized content and services. If you choose to disable location settings, some features of the app may not function as intended.";
        spannableString = getSpannableString(text);

        privacyPolicyTextView.setText(spannableString);
        privacyPolicyTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @NonNull
    private SpannableString getSpannableString(String text) {
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yourprivacypolicylink.com"));
                startActivity(intent);
            }
        };

        ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, com.google.android.libraries.places.R.color.quantum_googblue));

        int startIndex = text.indexOf("Privacy Policy");
        int endIndex = startIndex + "Privacy Policy".length();
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}