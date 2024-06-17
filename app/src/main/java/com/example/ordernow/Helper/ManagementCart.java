package com.example.ordernow.Helper;

import android.content.Context;

import com.example.ordernow.Domain.FoodListDomain;

import java.util.ArrayList;

public class ManagementCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagementCart (Context context){
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFoodItem(FoodListDomain item){
        ArrayList<FoodListDomain> foodListDomain = getCartList();
        boolean alreadyExists = false;
        int n = 0;

        for (int i = 0; i < foodListDomain.size(); i++) {
        //TODO: implement logic for cart handling
        }
    }

    public ArrayList<FoodListDomain> getCartList() {
        return tinyDB.getListObject("CartList");
    }
}
