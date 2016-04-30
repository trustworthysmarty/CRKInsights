package com.relsellglobal.crk.app.rssreader;

/**
 * Created by anilkukreti on 05/02/16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.relsellglobal.crk.app.R;
import com.relsellglobal.crk.app.pojo.RSSItem;

import java.util.ArrayList;

public class RSSMainActivity extends Activity {
    EditText title,link,description;

    ArrayList<RSSItem> mList;

    Button b1,b2;
    private String finalUrl="http://feeds.pharmaguideline.com/pharmaguideline";
    private RSSHandleXml obj;

    Handler mHandlerForRSSData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                RSSItem localObj = mList.get(0);
                title.setText(localObj.getTitle());
                link.setText(localObj.getLink());
                description.setText(localObj.getDescription());
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_activity_main);

        mList = new ArrayList();

        title = (EditText) findViewById(R.id.editText);
        link = (EditText) findViewById(R.id.editText2);
        description = (EditText) findViewById(R.id.editText3);

        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obj = new RSSHandleXml(finalUrl,mList,mHandlerForRSSData);
                //obj.fetchXML();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent in=new Intent(RSSMainActivity.this,second.class);
                //startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}
