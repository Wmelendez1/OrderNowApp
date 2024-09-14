package com.example.ordernow.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.R;
import com.example.ordernow.databinding.AddcontentrowBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterContent extends RecyclerView.Adapter<AdapterContent.MyViewHolder> {

    private Context context;


    public ArrayList<ModelContent> ContentArrayList;
    public ArrayList<ModelContent> filterList;

    private FilterPdfContent filter;

    private SelectListener listener;

    public AddcontentrowBinding Contentbinding;

    private static final String TAG = "PDF_ADAPTER_TAG";
    private static final long MAX_BYTES_PDF = 1024 * 1024 * 10; // 10MB

    public AdapterContent(Context context, ArrayList<ModelContent> contentArrayList, SelectListener listener) {
        this.context = context;
        ContentArrayList = contentArrayList;
        this.filterList = new ArrayList<>(contentArrayList);
        this.listener = listener;

    }
    public Filter getContentFilter() {
        if (filter == null) {
            filter = new FilterPdfContent(filterList, this);
        }
        return filter;

    }

    // Update content list
    public void updateContentList(ArrayList<ModelContent> filteredList) {
        ContentArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Contentbinding = AddcontentrowBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(Contentbinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get data
        ModelContent model = ContentArrayList.get(position);


        // Set data to views
        holder.Contentbinding.titleTv.setText(model.getContentTitle());
        holder.Contentbinding.descriptionTV.setText(model.getContentDescription());

        holder.editContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(model);
            }
        });

        // Load PDF from URL
        loadPdfFromUrl(model, holder);
    }
    public void loadPdfFromUrl(ModelContent model, AdapterContent.MyViewHolder holder) {
        // Load PDF from URL and display in PDFView
        String pdfUrl = model.getContentPdf();

        Log.d(TAG, "PDF URL: " + pdfUrl); // Log the URL
        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Log.e(TAG, "PDF URL is null or empty");
            return;
        }

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF).addOnSuccessListener(bytes -> {
            Log.d(TAG, "onSuccess: " + model.getContentPdf() + " successfully loaded");

            // Set PDF bytes to PDFView
            holder.contentPdf.fromBytes(bytes)
                    .pages(0) // Show only the first page

                    .swipeHorizontal(false)
                    .enableSwipe(false)
                    .onError(t -> {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onError: " + t.getMessage());
                    })
                    .onLoad(nbPages -> {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "LoadComplete: PDF loaded");
                    })
                    .load();
        }).addOnFailureListener(e -> {
            holder.progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onFailure: Failed to load PDF from URL due to " + e.getMessage());
        });
    }

    @Override
    public int getItemCount() {
        return ContentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public AddcontentrowBinding Contentbinding;
        TextView contentTitle, contentDescription;

        public ImageButton editContentBtn;

        ProgressBar progressBar;
        //String pdfUrl;
        PDFView contentPdf;

        public MyViewHolder(AddcontentrowBinding Contentbinding) {
            super(Contentbinding.getRoot());
            this.Contentbinding = Contentbinding;
            this.contentPdf = Contentbinding.ContentPdf;
            this.editContentBtn = Contentbinding.editContentBtn;
            progressBar = Contentbinding.progressBar;
//            super(Contentbinding.getRoot());
//            this.Contentbinding = Contentbinding;
//            this.contentPdf = Contentbinding.ContentPdf;
//
//            contentPdf = itemView.findViewById(R.id.ContentPdf);
//            this.editContentBtn = Contentbinding.editContentBtn;
//            progressBar = itemView.findViewById(R.id.progressBar);
//            contentTitle = itemView.findViewById(R.id.titleTv);
//            contentDescription = itemView.findViewById(R.id.descriptionTV);


        }
    }
    // Fetch content from Firebase Realtime Database
    public void fetchSharedContent() {
        DatabaseReference contentRef = FirebaseDatabase.getInstance().getReference("SharedContent");
        contentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ModelContent> contentList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelContent content = snapshot.getValue(ModelContent.class);
                    contentList.add(content);
                }
                updateContentList(contentList); // Update adapter's content list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}