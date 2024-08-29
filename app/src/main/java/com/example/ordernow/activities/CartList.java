package com.example.ordernow.activities;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ordernow.Adapter.CartListAdapter;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.Helper.ManagementCart;
import com.example.ordernow.Interface.ChangeQuantityListener;
import com.example.ordernow.R;


import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CartList extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;
    TextView subTotal, deliveryFee, taxes, totalPrice, cartemptyText, checkoutButton;
    private double tax;
    private ScrollView scrollView;
    private ImageView cartbackButton;

    private FoodNearYouDomain foodNearYouDomain;
    private PaymentSheet paymentSheet;
    private double total;
    private boolean apiFlag;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_list);

        //initialize members here
        recyclerView = findViewById(R.id.cartrecyclerview);
        subTotal = findViewById(R.id.subTotal);
        deliveryFee = findViewById(R.id.deliveryFee);
        taxes = findViewById(R.id.taxes);
        totalPrice = findViewById(R.id.totalPrice);
        cartemptyText = findViewById(R.id.cartemptyText);
        scrollView = findViewById(R.id.cartscrollview);
        cartbackButton = findViewById(R.id.cartbackButton);
        checkoutButton = findViewById(R.id.checkoutBtn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        managementCart = new ManagementCart(this);

        cartbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartList.this, HomePage.class);
                startActivity(intent);
            }
        });

        initList();
        CalculateCart();
        makeRequest();

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, new PaymentSheet.Configuration(
                        "Order Now", customerConfig
                ));
            }
        });

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

    }
    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult)
    {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled){
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof  PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment Declined", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            //TODO go to next page
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        }
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CartListAdapter(managementCart.getCartList(), this, new ChangeQuantityListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });

        recyclerView.setAdapter(adapter);

        //cart will be visible ONLY IF theres something in the cart
        if (managementCart.getCartList().isEmpty()) {
            cartemptyText.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            cartemptyText.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    //calculates the cart and sets data into the view
    private void CalculateCart() {
        double taxPercent = 0.06; //example til we feed with data. Florida food sales tax
        double deliveryFeeAmount = 2.99;

        double subTotalAmount = (double) Math.round(managementCart.getSubtotal() * 100) / 100;
        tax = (double) Math.round((subTotalAmount * taxPercent) * 100) / 100;
        total = (double) Math.round((subTotalAmount + tax + deliveryFeeAmount) * 100) / 100;

        subTotal.setText("$" + subTotalAmount);
        taxes.setText("$" + tax);
        deliveryFee.setText("$" + deliveryFeeAmount);
        totalPrice.setText("$" + total);
    }

    //POST request to server. Sends dollar amount gets publishable key.
    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://illustrious-branch-couch.glitch.me/checkout";
        JSONObject postData = new JSONObject();
        try {
            postData.put("amount", String.valueOf(total));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = postData.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                try {
                    final JSONObject result = new JSONObject(response);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));
                } catch (JSONException e) { Log.e("LOG_VOLLEY", response); }
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        //Allow null
                        if (jsonString == null || jsonString.length() == 0) {
                            jsonString = "{'status':'success'}";
                        }

                        return Response.success(jsonString,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };

            requestQueue.add(stringRequest);
        }

    }

