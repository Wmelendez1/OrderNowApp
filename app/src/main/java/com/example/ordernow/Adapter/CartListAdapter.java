package com.example.ordernow.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordernow.Domain.FoodListDomain;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.Helper.ManagementCart;
import com.example.ordernow.Interface.ChangeQuantityListener;
import com.example.ordernow.R;
import com.example.ordernow.activities.CartList;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter <CartListAdapter.ViewHolder> {
    private ArrayList<FoodListDomain> foodListDomains;
    private ManagementCart managementCart;
    private ChangeQuantityListener changeQuantityListener;


    public CartListAdapter(ArrayList<FoodListDomain> foodListDomains, Context context, ChangeQuantityListener changeQuantityListener) {
        this.foodListDomains = foodListDomains;
        this.managementCart = new ManagementCart(context);
        this.changeQuantityListener = changeQuantityListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodListDomain currentItem = foodListDomains.get(position);

        holder.foodName.setText(currentItem.getFoodname());
        double price = currentItem.getPrice() * currentItem.getNumberInCart();
        double roundedPrice = Math.round(price * 100.0) / 100.0;
        holder.price.setText(String.valueOf(roundedPrice));
        holder.itemCount.setText(String.valueOf(currentItem.getNumberInCart()));

        int drawableResId = holder.itemView.getContext().getResources().getIdentifier(currentItem.getPic(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResId)
                .into(holder.foodPic);

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = holder.getAdapterPosition();
                managementCart.subtractFoodQuantity(foodListDomains, currentPos, new ChangeQuantityListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeQuantityListener.changed();
                    }
                });
            }
        });

        holder.plusButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = holder.getAdapterPosition();
                managementCart.addFoodQuantity(foodListDomains, currentPos, new ChangeQuantityListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeQuantityListener.changed();
                    }
                });
            }
        }));
    }

    @Override
    public int getItemCount() {
        return foodListDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, price, itemCount;
        ImageView foodPic, plusButton, minusButton;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodname);
            price = itemView.findViewById(R.id.price);
            itemCount = itemView.findViewById(R.id.itemCount);
            foodPic = itemView.findViewById(R.id.foodpic);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
        }
    }
}
