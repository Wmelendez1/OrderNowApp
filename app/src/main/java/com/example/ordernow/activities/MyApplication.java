package com.example.ordernow.activities;

import android.app.Application;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

//application class runs before your launcher activity
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //Created a static method to convert timestamp to proper date format, so we can use it everywhere in project, no need to rewrite again
    public static final String formatTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);

        //format timestamp to MM/DD/YYYY
        String date = DateFormat.format("MM/DD/YYYY", cal).toString();

        return date;
    }
}
