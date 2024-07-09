package com.example.ordernow.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.ordernow.Domain.FoodListDomain;
import com.example.ordernow.Interface.ChangeQuantityListener;

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
            if (foodListDomain.get(i).getFoodname().equals(item.getFoodname())) {
                alreadyExists = true;
                n = i;
                break;
            }
        }

        if (alreadyExists) {
            foodListDomain.get(n).setNumberInCart(item.getNumberInCart());
        }
        else {
            foodListDomain.add(item);
        }

        tinyDB.putListObject("CartList", foodListDomain);
        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodListDomain> getCartList() {
        return tinyDB.getListObject("CartList");
    }

    public void addFoodQuantity (ArrayList <FoodListDomain> foodListDomain, int position, ChangeQuantityListener changeQuantityListener) {
        FoodListDomain currentItem = foodListDomain.get(position);

        //add quantity
        currentItem.setNumberInCart(currentItem.getNumberInCart() + 1);
        tinyDB.putListObject("CartList", foodListDomain);
        changeQuantityListener.changed();
    }

    public void subtractFoodQuantity (ArrayList <FoodListDomain> foodListDomain, int position, ChangeQuantityListener changeQuantityListener) {
        FoodListDomain currentItem = foodListDomain.get(position);

        //removes food item if only one in cart but removes a single number if item != 1
        if (currentItem.getNumberInCart() == 1) {
            foodListDomain.remove(position);
        }
        else {
            currentItem.setNumberInCart(currentItem.getNumberInCart() - 1);
        }

        tinyDB.putListObject("CartList", foodListDomain);
        changeQuantityListener.changed();
    }

    //calculates the subtotal
    public Double getSubtotal() {
        ArrayList <FoodListDomain> foodListDomains = getCartList();
        double fees = 0;

        //multiply the price of each item * number in cart
        for (int i = 0; i < foodListDomains.size(); i++) {
            fees = fees + (foodListDomains.get(i).getPrice() * foodListDomains.get(i).getNumberInCart());
        }

        return fees;
    }
}
