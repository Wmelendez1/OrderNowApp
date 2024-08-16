package com.example.ordernow.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.Serializable;
import com.example.ordernow.R;
public class ModelContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ContentTitle;
    private String ContentDescription;
    private String ContentPdf;
    private long timestamp;

    private String uid;


    public ModelContent() {
    }

    public ModelContent (String ContentTitle, String ContentDescription, String ContentPdf, long timestamp) {
        this.ContentTitle = ContentTitle;
        this.ContentDescription = ContentDescription;
        this.ContentPdf = ContentPdf;
        this.timestamp = timestamp;
    }

    public String getContentTitle() {
        return ContentTitle;
    }

    public void setContentTitle(String contentTitle) {
        ContentTitle = contentTitle;
    }

    public String getContentDescription() {
        return ContentDescription;
    }

    public void setContentDescription(String contentDescription) {
        ContentDescription = contentDescription;
    }

    public String getContentPdf() {
        return ContentPdf;
    }

    public void setContentPdf(String contentPdf) {
        ContentPdf = contentPdf;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}