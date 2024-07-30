package com.example.ordernow.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.ordernow.R;

import com.example.ordernow.Adapter.FavoritesAdapter;
import com.example.ordernow.Models.FavoriteItem;

public class Favorites extends AppCompatActivity {

    private RecyclerView recyclerViewFavorites;
    private FavoritesAdapter favoritesAdapter;
    private List<FavoriteItem> favoriteItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));

        favoriteItemList = new ArrayList<>();
        // Populate favoriteItemList with your data
        favoriteItemList.add(new FavoriteItem("Restaurant 1", R.drawable.tacobell));
        favoriteItemList.add(new FavoriteItem("Restaurant 2", R.drawable.wendys));

        favoritesAdapter = new FavoritesAdapter(favoriteItemList);
        recyclerViewFavorites.setAdapter(favoritesAdapter);
    }
}