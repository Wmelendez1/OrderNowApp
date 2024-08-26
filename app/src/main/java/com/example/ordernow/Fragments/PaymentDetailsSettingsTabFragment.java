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

import java.util.ArrayList;
import java.util.List;

import Models.Card;


public class PaymentDetailsSettingsTabFragment extends Fragment {

    private static final int ADD_CARD_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<Card> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_details_settings_tab, container, false);

        recyclerView = view.findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample card data
        cards = new ArrayList<>();
        cards.add(new Card("**** 1234", true,"John Doe", "12/23" ));
        cards.add(new Card("**** 5678", false, "Jane Doe", "11/24"));
        // Add more cards as needed

        cardAdapter = new CardAdapter(cards);
        recyclerView.setAdapter(cardAdapter);

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
                cards.add(newCard);
                cardAdapter.notifyDataSetChanged();
            }
        }
    }
}