package com.relsellglobal.crk.app;


import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.pojo.DrawerHeaderItem;
import com.relsellglobal.crk.app.pojo.DrawerRowItem;
import com.relsellglobal.crk.app.pojo.IRSSItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.pojo.RSSItemListToDrawerRowItemListConverter;
import com.relsellglobal.crk.app.rssreader.IRSSHandler;
import com.relsellglobal.crk.app.rssreader.RSSHandleXml;
import com.relsellglobal.crk.app.rssreader.RSSHandleXml2;

import java.util.ArrayList;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class CrkRSSFragment extends Fragment {


    private static CrkRSSFragment shomefragment;
    public static final String HOME_FRAGMENT_TAG="firstsectionfragment";


    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    CrkInsightTabListItemsCardAdapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager


    ListView mList;
    ArrayList<IRSSItem> mItemList;
    ArrayList<String> mStringList;

    ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    ArrayList<DrawerRowItem> mDrawerRowItemList;





    private String finalUrl="https://www.taxmann.com/rss/news.ashx";
    String category = "Statutory Warnings";
    String categoryId = "1";
    private String pageTitle = "Demo Page";
    private IRSSHandler obj;
    private int xmlHandler = 1;
    private LinearLayout mLinearLayoutForNoFeed;

    Handler mHandlerForRSSData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                RSSItemListToDrawerRowItemListConverter localObj = new RSSItemListToDrawerRowItemListConverter();
                localObj.setDrawerRowItemArrayList(mDrawerRowItemList);
                localObj.setRssItemArrayList(mItemList);
                writeRssDataToDb(mItemList);
                mDrawerRowItemList = localObj.getDrawerRowItemArrayList();
                if(mDrawerRowItemList != null && mDrawerRowItemList.size() != 0) {
                    mLinearLayoutForNoFeed.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }

        }
    };

    public void writeRssDataToDb(  ArrayList<IRSSItem> itemList) {
        System.out.println("Writing Data to Rss DB ");
        for (IRSSItem obj : itemList) {
            ContentValues values = new ContentValues();
            values.put(QuotesProvider.RSSItemsTable.DESC, obj.getDescription());
            values.put(QuotesProvider.RSSItemsTable.CATEGORY,category);
            values.put(QuotesProvider.RSSItemsTable.CATEGORY_ID,categoryId);
            values.put(QuotesProvider.RSSItemsTable.PUBDATE, obj.getPubDate());
            values.put(QuotesProvider.RSSItemsTable.TITLE, obj.getTitle());
            values.put(QuotesProvider.RSSItemsTable.LINK, obj.getLink());
            Uri uri = getContext().getContentResolver().insert(QuotesProvider.RSSItemsTable.CONTENT_URI,
                    values);
        }

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.pharma_first_section_fragment,null);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        mLinearLayoutForNoFeed = (LinearLayout)v.findViewById(R.id.no_feed_available_layout);



        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //addDummyDataListUIFrom();
        setRetainInstance(true);
        /*finalUrl = getArguments().getString("url");
        pageTitle = getArguments().getString("pageTitle");
        xmlHandler = getArguments().getInt("xmlHandler");
*/



         // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        loadUIFromServer(xmlHandler);


        //mAdapter = new FirstSectionAdapter(getActivity(), mDrawerHeaderItemList, mDrawerRowItemList);


        //mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        // mRecyclerView.addItemDecoration(new ListCustomDivider(this, R.drawable.listitem_divider));

        mLayoutManager = new LinearLayoutManager(getActivity());                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildLayoutPosition(child);


                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            /**
             * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
             * intercept touch events with
             * { ViewGroup#onInterceptTouchEvent(MotionEvent)}.
             *
             * @param disallowIntercept True if the child does not want the parent to
             *                          intercept touch events.
             *                          ViewParent#requestDisallowInterceptTouchEvent(boolean)
             */
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public void loadUIFromServer(int xmlParser) {
        mItemList = new ArrayList();
        mDrawerHeaderItemList = new ArrayList<DrawerHeaderItem>();
        mDrawerRowItemList = new ArrayList<DrawerRowItem>();
        if(!finalUrl.equalsIgnoreCase("")) {
            if (xmlParser == 1) {
                obj = new RSSHandleXml(finalUrl, mItemList, mHandlerForRSSData);
            } else if (xmlParser == 2) {
                obj = new RSSHandleXml2(finalUrl, mItemList, mHandlerForRSSData);
            }
            obj.fetchXML();
        }
       // mAdapter = new FirstSectionAdapter(getActivity(), mDrawerHeaderItemList, mDrawerRowItemList);
        /*mAdapter = new CrkInsightTabListItemsCardAdapter(getActivity(), mDrawerRowItemList, getActivity().getSupportFragmentManager(),1);
        mRecyclerView.setAdapter(mAdapter);*/
    }




}
