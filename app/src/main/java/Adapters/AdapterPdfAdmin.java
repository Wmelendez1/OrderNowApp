package Adapters;


import static com.example.ordernow.activities.Constants.MAX_BYTES_PDF;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.R;
import com.example.ordernow.activities.ModelPdf;
import com.example.ordernow.activities.FilterPdfAdmin;
import com.example.ordernow.activities.MyApplication;
import com.example.ordernow.databinding.ActivityProfileLayoutBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> implements Filterable {

    private Context context;
    public ArrayList<ModelPdf> pdfArrayList, filterList;
    private ActivityProfileLayoutBinding binding;
    public FilterPdfAdmin filter;

    private static final String TAG = "PDF_ADAPTER_TAG";

    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList = new ArrayList<>(pdfArrayList);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterPdfAdmin(filterList, this);
        }
        return filter;
    }

    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ActivityProfileLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {
        // Get data
        ModelPdf model = pdfArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();
        String formattedDate = MyApplication.formatTimestamp(timestamp);

        // Set data to views
        holder.firstNameTv.setText(title);
        holder.lastNameTv.setText(description);
        holder.dateTv.setText(formattedDate);

        // Load further details like category, pdf from URL, pdf size in separate functions
        loadCategory(model, holder);
        loadPdfFromUrl(model, holder);
        loadPdfSize(model, holder);
    }

    private void loadPdfSize(ModelPdf model, HolderPdfAdmin holder) {
        // Using URL, retrieve file metadata from Firebase Storage
        String pdfUrl = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata().addOnSuccessListener(storageMetadata -> {
            // Get size in bytes
            double bytes = storageMetadata.getSizeBytes();
            Log.d(TAG, "onSuccess: " + model.getTitle() + " " + bytes);

            // Convert bytes to KB / MB
            double kb = bytes / 1024;
            double mb = kb / 1024;

            String sizeText;
            if (mb >= 1) {
                sizeText = String.format("%.2f MB", mb);
            } else if (kb >= 1) {
                sizeText = String.format("%.2f KB", kb);
            } else {
                sizeText = String.format("%.2f bytes", bytes);
            }

            holder.sizeTv.setText(sizeText);
        }).addOnFailureListener(e -> {
            // Failed to get metadata
            Log.d(TAG, "onFailure: " + e.getMessage());
        });
    }

    private void loadPdfFromUrl(ModelPdf model, HolderPdfAdmin holder) {
        // Load PDF from URL and display in PDFView
        String pdfUrl = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF).addOnSuccessListener(bytes -> {
            Log.d(TAG, "onSuccess: " + model.getTitle() + " successfully loaded");

            // Set PDF bytes to PDFView
            holder.pdfView.fromBytes(bytes)
                    .pages(0) // Show only the first page
                    .spacing(0)
                    .swipeHorizontal(false)
                    .enableSwipe(false)
                    .onError(t -> {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onError: " + t.getMessage());
                    })
                    .onPageError((page, t) -> {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onPageError: " + t.getMessage());
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

    private void loadCategory(ModelPdf model, HolderPdfAdmin holder) {
        // Get category using categoryId from Firebase Database
        String categoryId = model.getCategoryId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get category
                String category = String.valueOf(snapshot.child("category").getValue());

                // Set category to categoryTv
                holder.categoryTv.setText(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    static class HolderPdfAdmin extends RecyclerView.ViewHolder {
        PDFView pdfView;
        ProgressBar progressBar;
        TextView firstNameTv, lastNameTv, categoryTv, sizeTv, dateTv;
        ImageButton moreBtn;

        public HolderPdfAdmin(@NonNull View itemView) {
            super(itemView);
            pdfView = itemView.findViewById(R.id.pdfView);
            progressBar = itemView.findViewById(R.id.progressBar);
            firstNameTv = itemView.findViewById(R.id.firstNameTv);
            lastNameTv = itemView.findViewById(R.id.lastNameTv);

        }
    }
}
