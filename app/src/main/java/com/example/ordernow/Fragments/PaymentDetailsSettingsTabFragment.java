package com.example.ordernow.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.ordernow.Adapter.CardAdapter;
import com.example.ordernow.R;
import com.example.ordernow.activities.AddCardActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.example.ordernow.Models.Card;
import com.example.ordernow.activities.Alerts;


public class PaymentDetailsSettingsTabFragment extends Fragment {

    private static final int ADD_CARD_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<Card> cards;
    private DatabaseReference cardRef; // Firebase reference


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_details_settings_tab, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        cardRef = database.getReference("Cards");

        recyclerView = view.findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample card data
        cards = new ArrayList<>();
        cardAdapter = new CardAdapter(cards);
        recyclerView.setAdapter(cardAdapter);

        loadCardsFromFirebase();

        Button addNewCardBtn = view.findViewById(R.id.addNewCardBtn);
        addNewCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            if (data != null && data.hasExtra("newCard")) {
                Card newCard = (Card) data.getSerializableExtra("newCard");

                addCardToFirebase(newCard);

                cards.add(newCard);
                cardAdapter.notifyDataSetChanged();

                Alerts.addAlert("Payment Method Added", "A new payment method has been added.");
            }
        }
    }

    private void addCardToFirebase(Card card) {
        cardRef.push().setValue(card);

    }
    private void loadCardsFromFirebase() {
        cardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cards.clear(); // Clear the list before adding data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Card card = snapshot.getValue(Card.class);
                    cards.add(card); // Add card to the list
                }
                cardAdapter.notifyDataSetChanged(); // Notify adapter about the data change
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database errors if necessary
            }
        });
    }
}