package com.example.ordernow.activities;

import android.widget.Filter;

import java.util.ArrayList;


public class FilterPdfContent extends Filter {

    private ArrayList<ModelContent> filterList;
    private AdapterContent adapterContent;

    public FilterPdfContent(ArrayList<ModelContent> filterList, AdapterContent adapterContent) {
        this.filterList = filterList;
        this.adapterContent = adapterContent;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            String filterPattern = constraint.toString().toUpperCase().trim();

            ArrayList<ModelContent> filterModels = new ArrayList<>();
            for (ModelContent model : filterList) {
                if (model.getContentTitle().toUpperCase().contains(filterPattern)) {
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
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterContent.ContentArrayList.clear();
        adapterContent.ContentArrayList.addAll((ArrayList<ModelContent>) results.values);
        adapterContent.notifyDataSetChanged();
    }
}
