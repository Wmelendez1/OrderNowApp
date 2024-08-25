package com.example.ordernow.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ordernow.Adapter.CardAdapter;
import com.example.ordernow.R;

import java.util.ArrayList;
import java.util.List;

import Models.Card;


public class PaymentDetailsSettingsTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_details_settings_tab, container, false);

        recyclerView = view.findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample card data
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("**** 1234", true,"John Doe", "12/23" ));
        cards.add(new Card("**** 5678", false, "Jane Doe", "11/24"));
        // Add more cards as needed

        cardAdapter = new CardAdapter(cards);
        recyclerView.setAdapter(cardAdapter);

        return view;
    }
}
