package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordernow.R;
import com.example.ordernow.activities.FilterPdfAdmin;
import com.example.ordernow.databinding.ActivityProfileLayoutBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Models.ModelPdf;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> implements Filterable {

    private Context context;
    public ArrayList<ModelPdf> pdfArrayList, filterList;
    private ActivityProfileLayoutBinding binding;
    public FilterPdfAdmin filter;

    private static final String TAG = "PDF_ADAPTER_TAG";
    private static final long MAX_BYTES_PDF = 50000000; // 50 MB

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
        View view = LayoutInflater.from(context).inflate(R.layout.activity_profile_layout, parent, false);
        binding = ActivityProfileLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {
        // Get data
        ModelPdf model = pdfArrayList.get(position);
        String firstName = model.getFirstName();
        String lastName = model.getLastName();
        long timestamp = model.getTimestamp();
        String Age = model.getAge();

        // Set data to views
        holder.firstNameTv.setText(firstName);
        holder.lastNameTv.setText(lastName);
        holder.AgeTv.setText(Age);


        // Load further details like category, pdf from URL, pdf size in separate functions
        loadPdfFromUrl(model, holder);
        loadPdfSize(model, holder);
    }

    private void loadPdfSize(ModelPdf model, HolderPdfAdmin holder) {
        // Using URL, retrieve file metadata from Firebase Storage
        String pdfUrl = model.getUrl();
        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Log.e(TAG, "PDF URL is null or empty");
            return;
        }

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata().addOnSuccessListener(storageMetadata -> {
            // Get size in bytes
            double bytes = storageMetadata.getSizeBytes();
            Log.d(TAG, "onSuccess: " + model.getFirstName() + " " + bytes);

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

            // Assuming you have a sizeTextView to display the size
            holder.sizeTextView.setText(sizeText);
        }).addOnFailureListener(e -> {
            // Failed to get metadata
            Log.d(TAG, "onFailure: " + e.getMessage());
        });
    }

    private void loadPdfFromUrl(ModelPdf model, HolderPdfAdmin holder) {
        // Load PDF from URL and display in PDFView
        String pdfUrl = model.getUrl();
        Log.d(TAG, "PDF URL: " + pdfUrl); // Log the URL

        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Log.e(TAG, "PDF URL is null or empty");
            return;
        }

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF).addOnSuccessListener(bytes -> {
            Log.d(TAG, "onSuccess: " + model.getFirstName() + " successfully loaded");

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

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    static class HolderPdfAdmin extends RecyclerView.ViewHolder {
        PDFView pdfView;
        ProgressBar progressBar;
        EditText firstNameTv, lastNameTv, AgeTv; // Change TextView to EditText
        ImageButton moreBtn;
        TextView sizeTextView;

        public HolderPdfAdmin(@NonNull View itemView) {
            super(itemView);
            pdfView = itemView.findViewById(R.id.pdfView);
            progressBar = itemView.findViewById(R.id.progressBar);
            firstNameTv = itemView.findViewById(R.id.firstNameTv);
            lastNameTv = itemView.findViewById(R.id.lastNameTv);
            AgeTv = itemView.findViewById(R.id.AgeTv);

        }
    }
}
