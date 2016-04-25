package com.relsellglobal.crk.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;

import java.util.ArrayList;

/**
 * Created by anilkukreti on 02/03/16.
 */
public class Utility extends Application {
    //sensible place to declare a log tag for the application
    public static final String LOG_TAG = "myapp";

    //instance
    private static Utility instance = null;

    private boolean showDBToastMsg = true;

    private ArrayList<QuotesListItem> mListForStationaryQuotesData;


    private ArrayList<ServicesListItem> mListForServicesData;


    public ArrayList<ServicesListItem> getmListForServicesData() {
        return mListForServicesData;
    }

    public void setmListForServicesData(ArrayList<ServicesListItem> mListForServicesData) {
        this.mListForServicesData = mListForServicesData;
    }

    public ArrayList<QuotesListItem> getmListForStationaryQuotesData() {
        return mListForStationaryQuotesData;
    }

    public void setmListForStationaryQuotesData(ArrayList<QuotesListItem> mListForStationaryQuotesData) {
        this.mListForStationaryQuotesData = mListForStationaryQuotesData;
    }

    public static Utility getInstance() {
        checkInstance();
        return instance;
    }

    public static void setInstance(Utility instance) {
        Utility.instance = instance;
    }
    private static void checkInstance() {
        if (instance == null)
            throw new IllegalStateException("Application not created yet!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //provide an instance for our static accessors
        instance = this;
    }

    public void showToastMsgForDB(Context context, String msg, boolean major) {
        if (showDBToastMsg || major) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void writeToPrefs(String fileName, String key, String value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void writeToLongPrefs(String fileName, String key, long value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getDataFromPefs(String fileName, String key) {
        String value = null;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getString(key, null);
        return value;
    }

    public long getLongDataFromPerfs(String fileName, String key) {
        long value = -1;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getLong(key, -1);
        return value;
    }
}
