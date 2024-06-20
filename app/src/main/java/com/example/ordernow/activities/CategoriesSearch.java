package com.example.ordernow.activities;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.Adapter.CatergoryAdapter;
import com.example.ordernow.Adapter.FoodNearYouAdapter;
import com.example.ordernow.Domain.CategoryDomain;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.R;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;



public class CategoriesSearch extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private CatergoryAdapter categoryAdapter;
    private RecyclerView recyclerViewCategories;
    private ArrayList<CategoryDomain> category;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_search);
        createRecycleView();
    }
    private void createRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategories = findViewById(R.id.RV);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        //add categories
        category = new ArrayList<>();
        category.add(new CategoryDomain("Chinese", "Chinese","chinesefood"));
        category.add(new CategoryDomain("Fast Food", "Fast Food","fastfood"));
        category.add(new CategoryDomain("Pizza", "Pizza","pizza"));
        category.add(new CategoryDomain("Burger", "Burger", "burger"));
        category.add(new CategoryDomain("Breakfast", "Breakfast","pancake"));

        adapter = new CatergoryAdapter(category);
        recyclerViewCategories.setAdapter(adapter);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        categoryAdapter = new CatergoryAdapter(category);
        SearchView searchView = findViewById(R.id.SV);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                categoryAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.categories_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_text);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                categoryAdapter.filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
