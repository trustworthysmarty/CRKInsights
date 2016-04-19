package com.relsellglobal.crk.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


/**
 * Created by anilkukreti on 29/02/16.
 */
public class ContactUsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    CrkContactusCardAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crk_contactus_list_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact us");
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

       /* mImageView  = (ImageView)findViewById(R.id.imageView);
        quoteTv = (TextView)findViewById(R.id.quote_text);
        quoteText = getIntent().getExtras().getString("quoteText");
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "BOD_BLAR.TTF");
        quoteTv.setTypeface(tf);
        quoteTv.setText(quoteText);*/
        ArrayList<String> modifiedList = new ArrayList<>();
        adapter = new CrkContactusCardAdapter(this, modifiedList);
        mRecyclerView.setAdapter(adapter);



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




}
