package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ordernow.R;
import java.util.List;

import Models.Card;

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
            cards.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cards.size());
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
}

