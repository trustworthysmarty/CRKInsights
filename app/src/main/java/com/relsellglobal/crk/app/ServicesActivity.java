package com.relsellglobal.crk.app;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.util.Utility;
import com.relsellglobal.crk.app.viewflipperscroller.SimpleFlipperFragment;

import java.util.ArrayList;


/**
 * Created by anilkukreti on 29/02/16.
 */
public class ServicesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    CrkServicesCardAdapter adapter;
    LinearLayout containerLayout;
    FrameLayout mFrameLayout;
    CollapsingToolbarLayout mCollapsingToobar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crk_contactus_list_main);
        containerLayout = (LinearLayout)findViewById(R.id.container_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mCollapsingToobar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.mipmap.modified_arrow));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.services_txt));



        getDataFromDB(2);    // services table



        SimpleFlipperFragment fragment = new SimpleFlipperFragment();

        getSupportFragmentManager().beginTransaction().replace(mFrameLayout.getId(), fragment, "simple_fragment").commit();

        mCollapsingToobar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToobar.setCollapsedTitleTextColor(Color.BLACK);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }




     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }

    public void inflateUI() {
        LayoutInflater layoutInflater = getLayoutInflater();
        ArrayList<ServicesListItem> modifiedList = Utility.getInstance().getmListForServicesData();

        for(ServicesListItem listItem : modifiedList){
            View v = (View)layoutInflater.inflate(R.layout.crk_services_listitem_header,null);
            TextView headerTextView = (TextView) v.findViewById(R.id.headoffice_tv);
            LinearLayout addressLayout = (LinearLayout)v.findViewById(R.id.address_layout);
            if(listItem.isHeader().equalsIgnoreCase("true")) {
                headerTextView.setText(listItem.getDescription());
                TextView tv = null;
                ArrayList<String> childList = new ArrayList();
                String categoryName = listItem.getDescription();
                for (ServicesListItem obj1 : modifiedList) {
                    if (obj1.getCategory().trim().equalsIgnoreCase(categoryName.trim())) {
                        childList.add(obj1.getDescription());
                    }
                }
                for (int i = 0; i < childList.size(); i++) {
                    tv = new TextView(ServicesActivity.this);
                    tv.setTextColor(Color.BLACK);
                    String str = "\u2799 " + childList.get(i);
                    tv.setText(str);
                    addressLayout.addView(tv);
                }
                containerLayout.addView(v);
            }
        }

    }

    public void getDataFromDB(int queryNo) {
        Bundle b = new Bundle();
        if (queryNo == 2) {
            b.putStringArray("projection", new String[]{
                    QuotesProvider.ServicesTable._ID,
                    QuotesProvider.ServicesTable.DESC,
                    QuotesProvider.ServicesTable.CATEGORY,
                    QuotesProvider.ServicesTable.ISHEADER,
                    QuotesProvider.ServicesTable.CATEGORY_ID
            });
            b.putString("selection", null);
            b.putStringArray("selectionArgs", null);
            b.putString("sortOrder", null);
        }
        Loader local = getLoaderManager().initLoader(queryNo, b, this);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = null;
        String[] projection = args.getStringArray("projection");
        String selection = args.getString("selection");
        String[] selectionArgs = args.getStringArray("selectionArgs");
        String sortOrder = args.getString("sortOrder");
        if (id == 2) {
            CONTENT_URI = QuotesProvider.ServicesTable.CONTENT_URI;
        }

        return new CursorLoader(this, CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (loader.getId() == 2) {
            ArrayList<ServicesListItem> list = new ArrayList<>();
            if (data.getCount() != 0) {
                while (data.moveToNext()) {
                    String desc = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.DESC));
                    String categoryName = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.CATEGORY));
                    String categoryId = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.CATEGORY_ID));

                    String headerValue = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.ISHEADER));

                    ServicesListItem qt = new ServicesListItem();
                    qt.setDescription(desc);
                    qt.setCategory(categoryName);
                    qt.setCategoryId(categoryId);
                    qt.setHeader(headerValue);
                    list.add(qt);
                }
            }
            //if (Utility.getInstance().getmListForServicesData() == null) {
                Utility.getInstance().setmListForServicesData(list);
            //}
            inflateUI();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}
