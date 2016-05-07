package com.relsellglobal.crk.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;


import com.relsellglobal.crk.app.CrkMainActivity;
import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.DBReaderRssItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.pojo.RSSItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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

    private ArrayList<ContactUsListItem> mListForContactUsData;

    private ArrayList<DBReaderRssItem> mListForRssData;




    public ArrayList<DBReaderRssItem> getmListForRssData() {
        return mListForRssData;
    }

    public void setmListForRssData(ArrayList<DBReaderRssItem> mListForRssData) {
        this.mListForRssData = mListForRssData;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    boolean debug = true;


    public ArrayList<ContactUsListItem> getmListForContactUsData() {
        return mListForContactUsData;
    }

    public void setmListForContactUsData(ArrayList<ContactUsListItem> mListForContactUsData) {
        this.mListForContactUsData = mListForContactUsData;
    }

    public ArrayList<ServicesListItem> getmListForServicesData() {
        return mListForServicesData;
    }

    public synchronized void  setmListForServicesData(ArrayList<ServicesListItem> mListForServicesData) {
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

    public String getServer() {

        String key = "server_address";
        String value = getDataFromPefs(CrkMainActivity.mDbPerfsFileName, key);
        if(value != null && !value.equalsIgnoreCase("")){
            return value;
        }
        return "relsellglobal.in";
    }

    public String constructUrl() {
        String result="";
        String key = "server_address";
        String value = getDataFromPefs(CrkMainActivity.mDbPerfsFileName, key);
        if(value != null && !value.equalsIgnoreCase("")){
            String key1 = "folderName";
            String value1 = getDataFromPefs(CrkMainActivity.mDbPerfsFileName, key1);
            if(value1 != null && !value1.equalsIgnoreCase("")){
                result = value.trim();
                int firstSlashIndex = result.indexOf("/");
                String tempResult = result.substring(0, firstSlashIndex);
                String tempRemaining = result.substring(firstSlashIndex+1);
                tempResult += ":" + getPort().trim()+"/" + tempRemaining+ "/"+value1.trim();
                result = tempResult;
            }else {
                result = value.trim();
                int firstSlashIndex = result.indexOf("/");
                String tempResult = result.substring(0, firstSlashIndex);
                String tempRemaining = result.substring(firstSlashIndex+1);
                tempResult += ":" + getPort().trim()+"/" + tempRemaining;
                result = tempResult;

            }
        } else {
            result = getServer() + ":" + getPort();
        }
        return result;
    }



    public String getPort() {

       /* if (isLocal) {
            return "80";
        }*/
        String key = "server_port";
        String value = getDataFromPefs(CrkMainActivity.mDbPerfsFileName, key);
        if(value != null && !value.equalsIgnoreCase("")){
            return value;
        }
        return  "80";
    }
    public void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ getApplicationContext().getPackageName() +"/databases/"+ QuotesProvider.DATABASE_NAME;
        String backupDBPath = QuotesProvider.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
