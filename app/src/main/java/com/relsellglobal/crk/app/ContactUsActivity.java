package com.relsellglobal.crk.app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.util.Utility;
import com.relsellglobal.crk.app.viewflipperscroller.SimpleFlipperFragment;

import java.util.ArrayList;


/**
 * Created by anilkukreti on 29/02/16.
 */
public class ContactUsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

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
        containerLayout = (LinearLayout) findViewById(R.id.container_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mCollapsingToobar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.mipmap.modified_arrow));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.contactus_txt));




        SimpleFlipperFragment fragment = new SimpleFlipperFragment();

        getSupportFragmentManager().beginTransaction().replace(mFrameLayout.getId(), fragment, "simple_fragment").commit();

        mCollapsingToobar.setExpandedTitleColor(Color.TRANSPARENT);
        mCollapsingToobar.setCollapsedTitleTextColor(Color.BLACK);
        getDataFromDB(3);    // contactus table







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }

    public void inflateUI() {
        LayoutInflater layoutInflater = getLayoutInflater();
        ArrayList<ContactUsListItem> modifiedList = Utility.getInstance().getmListForContactUsData();

        for (ContactUsListItem listItem : modifiedList) {
            View v = (View) layoutInflater.inflate(R.layout.crk_services_listitem_header, null);
            TextView headerTextView = (TextView) v.findViewById(R.id.headoffice_tv);
            LinearLayout addressLayout = (LinearLayout) v.findViewById(R.id.address_layout);
            if (listItem.isHeader().equalsIgnoreCase("true")) {
                headerTextView.setText(listItem.getDescription());
                TextView tv = null;
                ArrayList<String> childList = new ArrayList();
                String categoryName = listItem.getDescription();
                for (ContactUsListItem obj1 : modifiedList) {

                    if (obj1.getCategory().trim().equalsIgnoreCase(categoryName.trim())) {
                        childList.add(obj1.getDescription());
                    }
                }
                for (int i = 0; i < childList.size(); i++) {
                    tv = new TextView(ContactUsActivity.this);
                    float density = majicStarts();
                    String str = childList.get(i);
                    if (i == 0) {
                        tv.setTextColor(Color.parseColor("#cc0000"));
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv.setText(str);
                    } else if (i == 1) {
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv.setTextColor(Color.BLACK);
                        tv.setText(str);
                    } else {
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        tv.setTextColor(Color.BLACK);
                        if (str.contains("Phone no") || str.contains("Fax") || str.contains("Email")) {
                            String[] arr = str.split("=");
                            tv.setText(Html.fromHtml("<b>" + arr[0] + ":</b>" + " " + arr[1]));
                        } else {
                            tv.setText(str);
                        }
                    }


                    addressLayout.addView(tv);
                }
                containerLayout.addView(v);
            }
        }

    }

    public float majicStarts() {
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);

        int ratio;
        float width = 0;
        float height = 0;
        float density = 0;
        width = dm.widthPixels;
        height = dm.heightPixels;
        density = dm.density;

        return density;

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
        if (id == 3) {
            CONTENT_URI = QuotesProvider.ContactsUsTable.CONTENT_URI;
        }

        return new CursorLoader(this, CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (loader.getId() == 3) {
            ArrayList<ContactUsListItem> list = new ArrayList<>();
            if (data.getCount() != 0) {
                while (data.moveToNext()) {
                    String desc = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.DESC));
                    String categoryName = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.CATEGORY));
                    String categoryId = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.CATEGORY_ID));

                    String headerValue = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.ISHEADER));

                    ContactUsListItem qt = new ContactUsListItem();
                    qt.setDescription(desc);
                    qt.setCategory(categoryName);
                    qt.setCategoryId(categoryId);
                    qt.setHeader(headerValue);
                    list.add(qt);
                }
            }
            //if (Utility.getInstance().getmListForServicesData() == null) {
            Utility.getInstance().setmListForContactUsData(list);
            //}
            inflateUI();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
