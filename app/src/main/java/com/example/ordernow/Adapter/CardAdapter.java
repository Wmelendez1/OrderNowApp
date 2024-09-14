package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ordernow.R;
import com.example.ordernow.activities.Alerts;

import java.util.List;

import com.example.ordernow.Models.Card;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Card> cards;

    public CardAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.cardNumber.setText(card.getCardNumber());
        holder.cardHolderName.setText(card.getCardHolderName());
        holder.expiryDate.setText(card.getExpiryDate());

        // Set default card indicator
        if (card.isDefault()) {
            holder.defaultIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.defaultIndicator.setVisibility(View.GONE);
        }

        // Set click listeners for delete and other actions if needed
        // Handle card deletion
        holder.deleteCardBtn.setOnClickListener(v -> {
            String deletedCardNumber = card.getCardNumber();
            cards.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cards.size());

            // Trigger an alert after card is deleted
            Alerts.addAlert("Payment Method Removed", "The card ending in " + getLast4Digits(deletedCardNumber) + " has been removed.");
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public View deleteCardBtn;
        TextView cardNumber;
        TextView cardHolderName;
        TextView expiryDate;
        View defaultIndicator;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.cardNumberTV);
            cardHolderName = itemView.findViewById(R.id.cardHolderNameTV);
            expiryDate = itemView.findViewById(R.id.expiryDateTV);
            defaultIndicator = itemView.findViewById(R.id.defaultCardCB);
            deleteCardBtn = itemView.findViewById(R.id.deleteCardBtn);
        }
    }
    // Helper method to get the last 4 digits of the card number
    private String getLast4Digits(String cardNumber) {
        if (cardNumber.length() > 4) {
            return cardNumber.substring(cardNumber.length() - 4);
        } else {
            return cardNumber;
        }
    }
}

