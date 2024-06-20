package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import java.util.List;

public class CatergoryAdapter extends RecyclerView.Adapter<CatergoryAdapter.ViewHolder> {

    //list to hold data for category items
    ArrayList<CategoryDomain> categoryDomains;
    private ArrayList<CategoryDomain> catSearchList = null;

    //constructor to initialize the adapter
    public CatergoryAdapter(ArrayList<CategoryDomain> categoryDomains) {
        this.categoryDomains = categoryDomains;
        this.catSearchList = new ArrayList<CategoryDomain>();
        this.catSearchList.addAll(categoryDomains);
    }

    //custom filter for CategoriesSearch
    public void filter(String text){
        text = text.toLowerCase();
        categoryDomains.clear();
        if(text.length() == 0){
            categoryDomains.addAll(catSearchList);
        }
        else{
            for (CategoryDomain cd : catSearchList){
                if (cd.getTitle().toLowerCase().contains(text)){
                    categoryDomains.add(cd);
                }
            }
        }
        notifyDataSetChanged();
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
        CategoryDomain cd = categoryDomains.get(position);
        holder.categoryName.setText(cd.getTitle());
        String picUrl = cd.getPic();

        //add cases and enter photo name added to res/drawable
        holder.homecategoryLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));

        //get resource id for drawable with provided name
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView .getContext().getPackageName());

        holder.categoryPic.setImageResource(drawableResourceId);
        //loads image to ImageView
    }

    //returns total number in list
    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    //viewholder class references to each item
    class ViewHolder extends RecyclerView.ViewHolder{
        final TextView categoryName;
        final ImageView categoryPic;
        final ConstraintLayout homecategoryLayout;

        //constructor to initialize views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic =  itemView.findViewById((R.id.categoryPic));
            homecategoryLayout = itemView.findViewById((R.id.homecategoryLayout));
        }
    }
}