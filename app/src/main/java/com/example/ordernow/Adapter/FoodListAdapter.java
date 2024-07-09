package com.example.ordernow.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordernow.Domain.FoodListDomain;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.R;
import com.example.ordernow.activities.FoodDetails;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    //TODO: populate recycler view with food items. different menu's for each restaurant

    //list holds food item
    ArrayList<FoodListDomain> foodListDomainArrayList;

    //constructor to initialize the adapter
    public FoodListAdapter(ArrayList<FoodListDomain> foodListDomainArrayList) {
        this.foodListDomainArrayList = foodListDomainArrayList;
    }

    //method to create viewholder object
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodListDomain currentItem = foodListDomainArrayList.get(position);

        holder.foodname.setText(currentItem.getFoodname());

        //get resource id for drawable with provided name
        int drawableResId = holder.itemView.getContext().getResources().getIdentifier(currentItem.getPic(), "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResId)
                .into(holder.foodpic);

        holder.addItem.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), FoodDetails.class);
            intent.putExtra("object", foodListDomainArrayList.get(position));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.foodLayout.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), FoodDetails.class);
            intent.putExtra("object", foodListDomainArrayList.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodListDomainArrayList.size();
    }

    //viewholder class references to each item
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView addItem;
        ImageView foodpic;
        TextView foodname;
        TextView price;
        ConstraintLayout foodLayout;

        //constructor to initialize views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addItem = itemView.findViewById(R.id.addItem);
            foodpic =  itemView.findViewById((R.id.foodpic));
            foodname =  itemView.findViewById((R.id.foodname));
            price =  itemView.findViewById((R.id.price));
            foodLayout =  itemView.findViewById((R.id.foodLayout));
        }
    }
}

