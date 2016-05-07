package com.relsellglobal.crk.app.smartsync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.relsellglobal.crk.app.CrkInsightTabListItemsCardAdapter;
import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.DrawerHeaderItem;
import com.relsellglobal.crk.app.pojo.DrawerRowItem;
import com.relsellglobal.crk.app.pojo.IRSSItem;
import com.relsellglobal.crk.app.pojo.RSSItemListToDrawerRowItemListConverter;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.pojo.TableMetaDataItem;
import com.relsellglobal.crk.app.rssreader.IRSSHandler;
import com.relsellglobal.crk.app.rssreader.RSSHandleXml;
import com.relsellglobal.crk.app.rssreader.RSSHandleXml2;
import com.relsellglobal.crk.app.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 * Created by anilkukreti on 28/04/16.
 */

public class SmartSyncRSSTableTask extends AsyncTask<Void, Integer, Boolean> {


    int categoryId;
    String categoryName;
    Context mContext;
    ArrayList<IRSSItem> mItemList;
    private String finalUrl="https://www.taxmann.com/rss/news.ashx";
    private IRSSHandler obj;
    private int xmlHandler = 1;

    Handler mHandlerForRSSData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                writeRssDataToDb(mItemList);
            }
        }
    };


    public void writeRssDataToDb(  ArrayList<IRSSItem> itemList) {
        System.out.println("Writing Data to Rss DB ");

        String where = QuotesProvider.RSSItemsTable.CATEGORY+"=?";
        String []whereArgs = new String[]{categoryName};

        mContext.getContentResolver().delete(QuotesProvider.RSSItemsTable.CONTENT_URI,where,whereArgs);

        // lets put header item here so it will help later on

        ContentValues valuesHeader = new ContentValues();
        valuesHeader.put(QuotesProvider.RSSItemsTable.DESC,"desc_val");
        valuesHeader.put(QuotesProvider.RSSItemsTable.CATEGORY,categoryName);
        valuesHeader.put(QuotesProvider.RSSItemsTable.CATEGORY_ID,categoryId);
        valuesHeader.put(QuotesProvider.RSSItemsTable.PUBDATE,"pub_val");
        valuesHeader.put(QuotesProvider.RSSItemsTable.TITLE,"title_val");
        valuesHeader.put(QuotesProvider.RSSItemsTable.LINK,"link_val");
        Uri uriHeader = mContext.getContentResolver().insert(QuotesProvider.RSSItemsTable.CONTENT_URI,
                valuesHeader);



        for (IRSSItem obj : itemList) {
            ContentValues values = new ContentValues();
            values.put(QuotesProvider.RSSItemsTable.DESC, obj.getDescription());
            values.put(QuotesProvider.RSSItemsTable.CATEGORY,categoryName);
            values.put(QuotesProvider.RSSItemsTable.CATEGORY_ID,categoryId);
            values.put(QuotesProvider.RSSItemsTable.PUBDATE, obj.getPubDate());
            values.put(QuotesProvider.RSSItemsTable.TITLE, obj.getTitle());
            values.put(QuotesProvider.RSSItemsTable.LINK, obj.getLink());
            Uri uri = mContext.getContentResolver().insert(QuotesProvider.RSSItemsTable.CONTENT_URI,
                    values);
        }

    }


    public void loadUIFromServer(int xmlParser) {
        mItemList = new ArrayList();

        if(!finalUrl.equalsIgnoreCase("")) {
            if (xmlParser == 1) {
                obj = new RSSHandleXml(finalUrl, mItemList, mHandlerForRSSData);
            } else if (xmlParser == 2) {
                obj = new RSSHandleXml2(finalUrl, mItemList, mHandlerForRSSData);
            }
            obj.fetchXML();
        }
    }



    public SmartSyncRSSTableTask(int categoryId,String categoryName, Context context,String rssUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        mContext = context;
        this.finalUrl = rssUrl;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        loadUIFromServer(xmlHandler);
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {

        // using handler here baby

    }




}