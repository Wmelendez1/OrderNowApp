package com.example.ordernow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ordernow.Adapter.CartListAdapter;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.Helper.ManagementCart;
import com.example.ordernow.Interface.ChangeQuantityListener;
import com.example.ordernow.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.CreateIntentCallback;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String clientSecret;
    private PaymentSheet.CustomerConfiguration config;

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
        fetchAPI();
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration(
                        "Order Now", config
                ));
            }
        });

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

    }
    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult)
    {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof  PaymentSheetResult.Failed) {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchAPI()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "localhost:8000";
        JSONObject postJSON = null;
        try {
            postJSON.put("amount",Float.parseFloat(totalPrice.getText().toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Update server with cart information
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Response: "+response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);

        //Update Client with Payment Intent
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            config = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey"));
                            clientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(request);
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
        double total = (double) Math.round((subTotalAmount + tax + deliveryFeeAmount) * 100) / 100;

        subTotal.setText("$" + subTotalAmount);
        taxes.setText("$" + tax);
        deliveryFee.setText("$" + deliveryFeeAmount);
        totalPrice.setText("$" + total);
    }
}