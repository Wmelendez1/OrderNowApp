package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.R;

import java.util.List;

import com.example.ordernow.Models.FavoriteItem;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<FavoriteItem> favoriteItemList;


    public FavoritesAdapter(List<FavoriteItem> favoriteItemList) {
        this.favoriteItemList = favoriteItemList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        FavoriteItem item = favoriteItemList.get(position);
        holder.imageViewRestaurant.setImageResource(item.getImageResId());
        holder.textViewRestaurantName.setText(item.getName());

        holder.buttonUnfavorite.setOnClickListener(v -> {
            // Handle unfavorite action
            favoriteItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteItemList.size());
        });
    }

    @Override
    public int getItemCount() {
        return favoriteItemList.size();
    }

    static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewRestaurant;
        TextView textViewRestaurantName;
        ImageButton buttonUnfavorite;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRestaurant = itemView.findViewById(R.id.imageViewRestaurant);
            textViewRestaurantName = itemView.findViewById(R.id.textViewRestaurantName);
            buttonUnfavorite = itemView.findViewById(R.id.buttonUnfavorite);
        }
    }
}