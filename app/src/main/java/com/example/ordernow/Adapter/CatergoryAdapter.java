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
import com.example.ordernow.Domain.CategoryDomain;
import com.example.ordernow.R;

import java.util.ArrayList;

public class CatergoryAdapter extends RecyclerView.Adapter<CatergoryAdapter.ViewHolder> {

    //list to hold data for category items
    ArrayList<CategoryDomain> categoryDomains;

    //constructor to initialize the adapter
    public CatergoryAdapter(ArrayList<CategoryDomain> categoryDomains) {
        this.categoryDomains = categoryDomains;
    }

    //method to create viewholder object
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater .from(parent.getContext()).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(inflate);
    }

    //bind data to viewholder objects
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoryDomains.get(position).getTitle());
        String picUrl = "";

        //add cases and enter photo name added to res/drawable
        switch(position){
            case 0 : {
                picUrl ="pizza";
                holder.homecategoryLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
                break;
            }
            case 1 : {
                picUrl ="burger";
                holder.homecategoryLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
                break;
            }
            case 2 : {
                picUrl ="pancake";
                holder.homecategoryLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
                break;
            }
            case 3 : {
                picUrl ="chinesefood";
                holder.homecategoryLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
                break;
            }
            case 4 : {
                picUrl ="fastfood";
                holder.homecategoryLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
                break;
            }
        }

        //get resource id for drawable with provided name
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView .getContext().getPackageName());

        //loads image to ImageView
        Glide.with(holder. itemView.getContext())
                .load(drawableResourceId)
                .into(holder.categoryPic);
    }

    //returns total number in list
    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    //viewholder class references to each item
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout homecategoryLayout;

        //constructor to initialize views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic =  itemView.findViewById((R.id.categoryPic));
            homecategoryLayout = itemView.findViewById((R.id.homecategoryLayout));
        }
    }
}