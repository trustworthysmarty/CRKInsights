package com.relsellglobal.crk.app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.util.Utility;

import java.util.ArrayList;


/**
 * Created by anilkukreti on 29/02/16.
 */
public class ContactUsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    CrkServicesCardAdapter adapter;
    LinearLayout containerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crk_contactus_list_main);
        containerLayout = (LinearLayout)findViewById(R.id.container_layout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact us");

        inflateUI();



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
        ArrayList<ContactUsListItem> modifiedList = Utility.getInstance().getmListForContactUsData();

        for(ContactUsListItem listItem : modifiedList){
            View v = (View)layoutInflater.inflate(R.layout.crk_services_listitem_header,null);
            TextView headerTextView = (TextView) v.findViewById(R.id.headoffice_tv);
            LinearLayout addressLayout = (LinearLayout)v.findViewById(R.id.address_layout);
            if(listItem.isHeader().equalsIgnoreCase("true")) {
                headerTextView.setText(listItem.getDescription());
                TextView tv = null;
                ArrayList<String> childList = new ArrayList();
                String categoryName = listItem.getDescription();
                for (ContactUsListItem obj1 : modifiedList) {
                    if (obj1.getCategory().equalsIgnoreCase(categoryName)) {
                        childList.add(obj1.getDescription());
                    }
                }
                for (int i = 0; i < childList.size(); i++) {
                    tv = new TextView(ContactUsActivity.this);
                    float density = getResources().getDisplayMetrics().density;
                    String str = childList.get(i);
                    if(i == 0) {
                        tv.setTextColor(Color.parseColor("#cc0000"));
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(12*density);
                        tv.setText(str);
                    } else if(i == 1) {
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setTextSize(12*density);
                        tv.setTextColor(Color.BLACK);
                        tv.setText(str);
                    } else {
                        tv.setTextSize(10*density);
                        tv.setTextColor(Color.BLACK);
                        if(str.contains("Phone no") || str.contains("Fax") || str.contains("Email")) {
                            String []arr = str.split("=");
                            tv.setText(Html.fromHtml("<b>"+arr[0]+":</b>"+" "+arr[1]));
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


}
