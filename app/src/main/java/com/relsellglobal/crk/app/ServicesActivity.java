package com.relsellglobal.crk.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.util.Utility;

import java.util.ArrayList;


/**
 * Created by anilkukreti on 29/02/16.
 */
public class ServicesActivity extends AppCompatActivity {

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
        getSupportActionBar().setTitle("Services");
        //mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

       /* LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);*/


        /*ArrayList<ServicesListItem> modifiedList = Utility.getInstance().getmListForServicesData();
        if(modifiedList != null) {
            adapter = new CrkServicesCardAdapter(this, modifiedList);
            mRecyclerView.setAdapter(adapter);
        } else {
            // wait for populating data from db  or put a synchronize block and get data
        }*/

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
                    if (obj1.getCategory().equalsIgnoreCase(categoryName)) {
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


}
