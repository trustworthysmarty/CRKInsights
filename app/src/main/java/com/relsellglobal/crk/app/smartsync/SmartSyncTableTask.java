package com.relsellglobal.crk.app.smartsync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.pojo.TableMetaDataItem;
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

public class SmartSyncTableTask extends AsyncTask<Void, Integer, Boolean> {


    StringBuffer responseString = new StringBuffer("");
    int mConnectionCode;
    int syncTableVar;
    int postDataVar;
    Context mContext;

    HashMap<String, String> hmVars;


    public SmartSyncTableTask(HashMap<String, String> hm, int syncTableVar, Context context) {
        this.hmVars = hm;
        this.syncTableVar = syncTableVar;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {

            URL url = new URL("http://" + Utility.getInstance().constructUrl() + "/DeliveryS/CRKInsightsServer/mobilecheck.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");


            connection.setDoInput(true);


            String postData = getPostData(hmVars, syncTableVar);

            if (postData.substring(postData.length() - 1).equalsIgnoreCase("&")) {
                postData = postData.substring(0, (postData.length() - 1));
            }

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData);
            writer.flush();
            writer.close();
            os.close();

            mConnectionCode = connection.getResponseCode();

            if (mConnectionCode == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";

                while ((line = rd.readLine()) != null) {
                    responseString.append(line);
                }
                return true;
            }
        } catch (IOException e) {
            Log.v("Message", e.getMessage());
            e.printStackTrace();
            return false;

        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {


        if (success) {
            if (responseString != null && !responseString.toString().equalsIgnoreCase("")) {


                if (Utility.getInstance().isDebug()) {
                    Log.v("TAG", "" + responseString.toString());
                }

                if (syncTableVar == 0) {

                    // this is for checking update for all db  tables

                    JSONObject jsonObj;

                    try {

                        jsonObj = new JSONObject(responseString.toString());
                        JSONObject listObj = jsonObj.getJSONObject("list");
                        JSONArray dataArray = listObj.getJSONArray("data");

                        ArrayList<TableMetaDataItem> arrayList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject obj = dataArray.getJSONObject(i);


                            String tableName = obj.getString("Name");
                            String tableCreateTime = obj.getString("Create_time");
                            String tableUpdateTime = obj.getString("Update_time");

                            if (tableName.equalsIgnoreCase("SERVICES")) {
                                tableName = QuotesProvider.SERVICES_TABLE_NAME;
                            } else if (tableName.equalsIgnoreCase("USER")) {
                                tableName = "usertable";
                            }

                            TableMetaDataItem metaDataItem = new TableMetaDataItem();
                            metaDataItem.setmTableName(tableName);
                            metaDataItem.setmCreateTime(tableCreateTime);
                            metaDataItem.setmUpdatedTime(tableUpdateTime);
                            arrayList.add(metaDataItem);
                        }

                        for (TableMetaDataItem obj : arrayList) {

                            // first check db

                            Cursor c = mContext.getContentResolver().query(QuotesProvider.MetaDataTable.CONTENT_URI, new String[]{QuotesProvider.MetaDataTable.NAME, QuotesProvider.MetaDataTable.UPDATE_TIME}, QuotesProvider.MetaDataTable.NAME + "=?", new String[]{obj.getmTableName()}, null);

                            ContentValues values = new ContentValues();
                            values.put(QuotesProvider.MetaDataTable.NAME, obj.getmTableName());
                            values.put(QuotesProvider.MetaDataTable.CREATE_TIME, obj.getmCreateTime());
                            values.put(QuotesProvider.MetaDataTable.UPDATE_TIME, "" + obj.getmUpdatedTime());
                            if (c == null || c.getCount() == 0) {
                                Uri uri = mContext.getContentResolver().insert(QuotesProvider.MetaDataTable.CONTENT_URI,
                                        values);
                                if (uri == null) {
                                    Log.i("TAG", "DB Uri is null");
                                } else {
                                    Log.i("TAG", "DB Uri is ok");
                                }
                            } else {
                                // do update

                                while (c.moveToNext()) {

                                    String updateTime = c.getString(c.getColumnIndexOrThrow(QuotesProvider.MetaDataTable.UPDATE_TIME));

                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH);
                                    Date d1 = df.parse(updateTime);

                                    Date d2 = df.parse(obj.getmUpdatedTime());  // this is date from server

                                    System.out.println(d1);
                                    System.out.println(d2);

                                    if (d2.after(d1)) {
                                        Log.i("TAG","Database needs a sync");
                                        // we can trigger a sync request from here
                                    }


                                }


                                int r = mContext.getContentResolver().update(QuotesProvider.MetaDataTable.CONTENT_URI,
                                        values, QuotesProvider.MetaDataTable.NAME + "=?", new String[]{obj.getmTableName()});
                                if (r >= 1) {
                                    Log.i("TAG", "row updated for " + obj.getmTableName());
                                }

                            }
                        }


                            /*if (Utility.getInstance().isDebug()) {
                                Utility.getInstance().exportDB();
                            }*/


                    } catch (JSONException e) {
                        e.printStackTrace();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (syncTableVar == 1) {

                    // this is for getting data in db for services table


                    JSONObject jsonObj;


                    mContext.getContentResolver().delete(QuotesProvider.ServicesTable.CONTENT_URI,null,null);



                    try {

                        jsonObj = new JSONObject(responseString.toString());
                        JSONObject listObj = jsonObj.getJSONObject("list");
                        JSONArray dataArray = listObj.getJSONArray("data");

                        ArrayList<ServicesListItem> arrayList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject obj = dataArray.getJSONObject(i);

                            String serviceID = obj.getString("ID");
                            String description = obj.getString("DESCRIPTION");
                            String categoryid = obj.getString("CATEGORYID");
                            String categoryName = obj.getString("CATEGORYNAME");

                            ServicesListItem servicesListItem = new ServicesListItem();
                            servicesListItem.setId(serviceID);
                            servicesListItem.setDescription(description);
                            servicesListItem.setCategoryId(categoryid);
                            servicesListItem.setCategory(categoryName);
                            if (categoryid.equalsIgnoreCase("0")) {
                                servicesListItem.setHeader("true");
                            }
                            arrayList.add(servicesListItem);
                        }

                        for (ServicesListItem obj : arrayList) {
                            ContentValues values = new ContentValues();
                            values.put(QuotesProvider.ServicesTable.DESC, obj.getDescription());
                            values.put(QuotesProvider.ServicesTable.CATEGORY, obj.getCategory());
                            values.put(QuotesProvider.ServicesTable.ISHEADER, "" + obj.isHeader());
                            values.put(QuotesProvider.ServicesTable.CATEGORY_ID, obj.getCategoryId());
                            Uri uri = mContext.getContentResolver().insert(QuotesProvider.ServicesTable.CONTENT_URI,
                                    values);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                } else if (syncTableVar == 3) {

                } else if (syncTableVar == 4) {

                } else if (syncTableVar == 5) {

                }


            }
        } else {

            if (mConnectionCode >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
                Toast.makeText(mContext, "Internal Server Error", Toast.LENGTH_LONG).show();


            } else if (mConnectionCode >= HttpURLConnection.HTTP_BAD_REQUEST && mConnectionCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {

                Toast.makeText(mContext, "Client Side Error", Toast.LENGTH_LONG).show();


            } else if (mConnectionCode >= HttpURLConnection.HTTP_MULT_CHOICE && mConnectionCode < HttpURLConnection.HTTP_BAD_REQUEST) {

                Toast.makeText(mContext, "Server Page is temp moved", Toast.LENGTH_LONG).show();

            }
        }


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    public String getPostDataStr(HashMap<String, String> map) {
        String result = "";

        Set<String> myset = map.keySet();
        int setSize = myset != null ? myset.size() : 0;
        int i = 0;

        for (String key : myset) {
            String value = map.get(key);
            result += key + "=" + value;
            if (i <= setSize - 1)
                result += "&";
            i++;
        }
        return result;
    }

    public String getPostData(HashMap<String, String> incomingHm, int syncDataVariable) throws UnsupportedEncodingException {

        String result = "";

        if (syncDataVariable == 0) {
            result = "controlVar=19";
        } else if (syncDataVariable == 1) {
            result = "controlVar=18";
        } else {

            Set<String> keys = incomingHm.keySet();
            HashMap<String, String> map = new HashMap<String, String>();
            for (String key : keys) {
                map.put(key, URLEncoder.encode(incomingHm.get(key), "UTF-8"));
            }
            result = getPostDataStr(map);
            if (Utility.getInstance().isDebug()) {
                System.out.println(result);
            }
        }


        return result;
    }

}