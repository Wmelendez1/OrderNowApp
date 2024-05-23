package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordernow.Domain.FoodNearYouDomain;
import com.example.ordernow.R;

import java.util.ArrayList;

public class FoodNearYouAdapter extends RecyclerView.Adapter<FoodNearYouAdapter.ViewHolder> {

    //list to hold data for food near you items
    ArrayList<FoodNearYouDomain> foodNearYouDomains;

    //constructor to initialize the adapter
    public FoodNearYouAdapter(ArrayList<FoodNearYouDomain> foodNearYouDomains) {
        this.foodNearYouDomains = foodNearYouDomains;
    }

    //method to create viewholder object
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater .from(parent.getContext()).inflate(R.layout.viewholder_foodnearyou, parent, false);
        return new ViewHolder(inflate);
    }

    //bind data to viewholder objects
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fnyName.setText(foodNearYouDomains.get(position).getName());
        holder.fnyNumReviews.setText(String.valueOf(foodNearYouDomains.get(position).getNumberReviews()));
        holder.fnyDistance.setText(foodNearYouDomains.get(position).getDistance());
        holder.fnydeliveryFee.setText(foodNearYouDomains.get(position).getDeliveryFee());
        holder.fnyTime.setText(foodNearYouDomains.get(position).getTime());
        String picUrl = "";

        //add cases and enter photo name added to res/drawable
        switch(position){
            case 0 : {
                picUrl = "mcdonaldss";
                holder.homefnylayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
                break;
            }
        }

        //get resource id for drawable with provided name
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView .getContext().getPackageName());

        //loads image to ImageView
        Glide.with(holder. itemView.getContext())
                .load(drawableResourceId)
                .into(holder.fnyLogo);
    }

    //returns total number in list
    @Override
    public int getItemCount() {
        return foodNearYouDomains.size();
    }

    //viewholder class references to each item
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView fnyLogo;
        TextView fnyName;
        TextView fnyNumReviews;
        TextView fnyDistance;
        TextView fnydeliveryFee;
        TextView fnyTime;
        ConstraintLayout homefnylayout;

        //constructor to initialize views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fnyLogo = itemView.findViewById(R.id.foodnearyoulogo);
            fnyName =  itemView.findViewById((R.id.foodnearyouName));
            fnyNumReviews =  itemView.findViewById((R.id.foodnearyouNumReview));
            fnyDistance =  itemView.findViewById((R.id.foodnearyouDistance));
            fnydeliveryFee =  itemView.findViewById((R.id.foodnearyoudeliveryFee));
            fnyTime =  itemView.findViewById((R.id.foodnearyouTime));
            homefnylayout = itemView.findViewById((R.id.foodnearyouLayout));
        }
    }
}