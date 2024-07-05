package com.example.ordernow.activities;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.Adapter.CatergoryAdapter;
import com.example.ordernow.Domain.CategoryDomain;
import com.example.ordernow.R;

import java.util.ArrayList;



public class CategoriesSearch extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategories;
    private ArrayList<CategoryDomain> category;
    private ArrayList<CategoryDomain> catSearchList = null;
    private ArrayList<String> catFilterList = null;
    private ArrayList<CategoryDomain> filteredList = null;
    private String[] catArray = {"Pizza", "Chinese", "Breakfast", "Fast Food", "Burger"};
    private boolean filtered = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_search);
        createRecycleView();
        catSearchList = new ArrayList<>();
        catFilterList = new ArrayList<>();
        filteredList = new ArrayList<>();
        catSearchList.addAll(category);
        AlertDialog.Builder filterDialog = new AlertDialog.Builder(CategoriesSearch.this)
        .setTitle("Filter By Category")
                .setMultiChoiceItems(catArray, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checks the item, add it to the selected
                                    // items.
                                    catFilterList.add(catArray[which]);
                                } else if (catFilterList.contains(which)) {
                                    // If the item is already in the array, remove it.
                                    catFilterList.remove(catArray[which]);}
                            }
                        }
                )
                .setPositiveButton(
                        "Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filterCategories(catFilterList);
                                catFilterList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                .setNeutralButton(
                "Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        catFilterList.clear();
                        filterCategories(catFilterList);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(
                "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        findViewById(R.id.filter).setOnClickListener(v -> {
            filterDialog.create();
            filterDialog.show();
        });

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
        category.add(new CategoryDomain("Chinese", "Chinese","chinesefood"));
        category.add(new CategoryDomain("Fast Food", "Fast Food","fastfood"));
        category.add(new CategoryDomain("Pizza", "Pizza","pizza"));
        category.add(new CategoryDomain("Burger", "Burger", "burger"));
        category.add(new CategoryDomain("Breakfast", "Breakfast","pancake"));

        adapter = new CatergoryAdapter(category);
        recyclerViewCategories.setAdapter(adapter);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        SearchView searchView = findViewById(R.id.SV);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    //custom filter for CategoriesSearch
    public void filter(String text){
        text = text.toLowerCase();
        category.clear();
        if(text.length() == 0){
            category.addAll(filtered ? filteredList: catSearchList);
        }
        else{
            for (CategoryDomain cd : filtered ? filteredList: catSearchList){
                if (cd.getTitle().toLowerCase().contains(text)){
                    category.add(cd);
                }
            }
        }
    }

    //custom filter using categories
    public void filterCategories(ArrayList<String> categoriesSel) {
        category.clear();
        filteredList.clear();
        if(categoriesSel.isEmpty()) {
            category.addAll(catSearchList);
            filtered = false;
        }
        for (String cat : categoriesSel){
            for (CategoryDomain cd : catSearchList){
                if (cd.getCategory().contains(cat)){
                    category.add(cd);
                    filtered = true;
                }
            }
        }
        filteredList.addAll(category);
    }

}
