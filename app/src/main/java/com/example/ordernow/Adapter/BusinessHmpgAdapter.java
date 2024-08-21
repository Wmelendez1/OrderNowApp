package com.example.ordernow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordernow.Domain.BusinessDomain;
import com.example.ordernow.R;

import java.util.ArrayList;

public class BusinessHmpgAdapter extends RecyclerView.Adapter<BusinessHmpgAdapter.ViewHolder> {
    ArrayList<BusinessDomain> businessDomains;

    //constructor to initialize the adapter
    public BusinessHmpgAdapter(ArrayList<BusinessDomain> businessDomains) {
        this.businessDomains = businessDomains;
    }

    @NonNull
    @Override
    public BusinessHmpgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_businesshpmg, parent, false);
        return new BusinessHmpgAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHmpgAdapter.ViewHolder holder, int position) {
        BusinessDomain business = businessDomains.get(position);

        //bind data to viewholder
        holder.businessName.setText(business.getBusinessName());
        holder.businessAddress.setText(business.getBusinessAddress());

        if (business.getBusinessPic() != null && !business.getBusinessPic().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(business.getBusinessPic())
                    .into(holder.businessPic);
        }
    }

    @Override
    public int getItemCount() {
        return businessDomains.size();
    }

    //method to update data and notify adapter
    public void updateData(ArrayList<BusinessDomain> newBusinessDomains) {
        this.businessDomains = newBusinessDomains;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView businessPic;
        private TextView businessName;
        private TextView businessAddress;
        private TextView businessPhone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            businessPic = itemView.findViewById(R.id.businesspic);
            businessName = itemView.findViewById(R.id.businessName);
            businessAddress = itemView.findViewById(R.id.businessAddress);
            businessPhone = itemView.findViewById(R.id.businessPhone);
        }
    }
}

