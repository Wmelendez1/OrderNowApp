package com.example.ordernow.activities;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;


import com.example.ordernow.Adapter.AdapterPdfAdmin;
import com.example.ordernow.Models.ModelPdf;

import java.util.List;  // Import List class

public class FilterPdfAdmin extends Filter {

    // ArrayList in which we want to search
    ArrayList<ModelPdf> filterList;
    // Adapter in which filter needs to be implemented
    AdapterPdfAdmin adapterPdfAdmin;

    // Constructor
    public FilterPdfAdmin(ArrayList<ModelPdf> filterList, AdapterPdfAdmin adapterPdfAdmin) {
        this.filterList = filterList;
        this.adapterPdfAdmin = adapterPdfAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // Value should not be null and empty
        if (constraint != null && constraint.length() > 0) {
            // Change to upper case to avoid case sensitivity
            String filterPattern = constraint.toString().toUpperCase().trim();

            ArrayList<ModelPdf> filterModels = new ArrayList<>();
            for (ModelPdf model : filterList) {
                // Validate
                if (model.getFirstName().toUpperCase().contains(filterPattern)) {
                    // Add to filtered list
                    filterModels.add(model);
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // Apply filter changes
        if (results.values instanceof List) {
            adapterPdfAdmin.pdfArrayList = (ArrayList<ModelPdf>) results.values;
        }

        // Notify changes
        adapterPdfAdmin.notifyDataSetChanged();
    }
}