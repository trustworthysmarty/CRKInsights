package com.relsellglobal.crk.app;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.customcomponents.NonSwipableViewPager;
import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.DrawerHeaderItem;
import com.relsellglobal.crk.app.pojo.DrawerRowItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.pojo.TableMetaDataItem;
import com.relsellglobal.crk.app.smartsync.SmartSyncTableTask;
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
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by anilkukreti on 09/04/16.
 */

public class CrkModifiedMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;
    AppBarLayout mAppBarLayout;

    NavigationView mNavigationView;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    ArrayList<DrawerRowItem> mDrawerRowItemList;


    public static final int SERVICES_ACTIVITY_START_CODE = 9001;
    public static final int CONTACTUS_ACTIVITY_START_CODE = 9002;

    CollapsingToolbarLayout collapsingToolbarLayout;
    //FloatingActionButton floatingActionButton;
    int mutedColor = R.attr.colorPrimary;
    RecyclerView recyclerView;
    FrameLayout mFrameLayout;
    ViewPager mContentFrameLayoutTwo;


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private NonSwipableViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    boolean searchOpen;
    int menuPrevPosition;
    public static String mDbPerfsFileName = "crk_db_perfs";
    private CrkAlarmReceiver mAlarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crk_activity_modified_main_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mContentFrameLayoutTwo = (ViewPager) findViewById(R.id.viewpager);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar.setTitle("");
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mAlarmReceiver = new CrkAlarmReceiver();


        SimpleFragment fragment = new SimpleFragment();

        getSupportFragmentManager().beginTransaction().replace(mFrameLayout.getId(), fragment, "simple_fragment").commit();


        // initial table sync check but only when there is network connection available
        // new SyncTableTask(null, 0).execute();




       /* SimpleFragmentDisplayingList fragment2 = new SimpleFragmentDisplayingList();

        Bundle b = new Bundle();
        b.putInt("containerId", mContentFrameLayoutTwo.getId());
        fragment2.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(mContentFrameLayoutTwo.getId(), fragment2, "simple_fragment_two").commit();*/





      /* mNavigationView.setItemBackground(this.getResources().getDrawable(R.drawable.bg_menu_item_selector));


        mNavigationView.setItemTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{}
                },
                new int[]{
                        Color.rgb(0, 0, 0),
                        Color.rgb(0, 0, 0),
                        Color.rgb(0, 0, 0)
                }
        ));

*/


        addDummyDataListUIFrom();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size


        mAdapter = new CrkNavigationAdapter(this, mDrawerHeaderItemList, mDrawerRowItemList);


        //mRecyclerView.addItemDecoration(new TorrinsListCustomDividerForDrawer(this, R.drawable.listitem_divider));

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        //mRecyclerView.addItemDecoration(new ListCustomDivider(this, R.drawable.listitem_divider,false,false));

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);


        viewPager = (NonSwipableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);


        setSupportActionBar(toolbar);


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };
        Drawer.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    onBackPressed();
                } else {
                    Drawer.openDrawer(Gravity.LEFT);
                }
            }
        });



      /*  mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                // now running for first item only
                if (id == R.id.homescreen) {
                    if (Drawer.isDrawerOpen(Gravity.LEFT)) {
                        Drawer.closeDrawer(Gravity.LEFT);
                    }
                    viewPager.setCurrentItem(0);
                    // instructer viewpager to show relevant time
                } else if (id == R.id.homescreen_pub) {
                    if (Drawer.isDrawerOpen(Gravity.LEFT)) {
                        Drawer.closeDrawer(Gravity.LEFT);
                    }
                    // instructer viewpager to show relevant time
                    viewPager.setCurrentItem(1);
                } else if (id == R.id.contactus) {
                    Bundle b = new Bundle();
                    b.putString("quoteText", "Anil");
                    selectItem(4, b);
                } else if (id == R.id.services) {
                    // inflate services here
                    Bundle b = new Bundle();
                    b.putString("quoteText", "Anil");
                    selectItem(5, b);
                }

                return true;
            }
        });


        mNavigationView.setCheckedItem(0);*/


        String dbcreatedValue = Utility.getInstance().getDataFromPefs(mDbPerfsFileName, "dbcreated");
        if ((dbcreatedValue == null) || (dbcreatedValue != null && dbcreatedValue.equalsIgnoreCase("false"))) {
            addDummyDataToDBForSearch();
            addDummyDataToDBForServices();
            //new SyncTableTask(null, 1).execute();  sync services table when sync request is made from background
            addDummyDataToDBForContactUs();


            Utility.getInstance().writeToPrefs(mDbPerfsFileName, "dbcreated", "true");

        } //else {

        /// call db method
        getDataFromDB(1);
        getDataFromDB(2);    // services table
        getDataFromDB(3);    // contact us table


        //   }

        final GestureDetector mGestureDetector = new GestureDetector(CrkModifiedMainActivity.this, new GestureDetector.SimpleOnGestureListener() {

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
                    for (int i = 0; i < mDrawerRowItemList.size(); i++) {
                        DrawerRowItem obj = mDrawerRowItemList.get(i);
                        if (i + 1 == position) {
                            obj.setmActive(true);
                        } else {
                            obj.setmActive(false);
                        }
                    }


                    mAdapter.notifyDataSetChanged();


                    if (position == 1) {
                        if (Drawer.isDrawerOpen(Gravity.LEFT)) {
                            Drawer.closeDrawer(Gravity.LEFT);
                        }
                        viewPager.setCurrentItem(0);

                    } else if (position == 2) {
                        if (Drawer.isDrawerOpen(Gravity.LEFT)) {
                            Drawer.closeDrawer(Gravity.LEFT);
                        }

                        viewPager.setCurrentItem(1);
                    } else {
                        Bundle b = new Bundle();
                        b.putString("quoteText", "Anil");
                        selectItem(position, b);
                    }
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


    }


    public void addDummyDataListUIFrom() {
        mDrawerHeaderItemList = new ArrayList<DrawerHeaderItem>();
        mDrawerRowItemList = new ArrayList<DrawerRowItem>();
        DrawerHeaderItem obj = new DrawerHeaderItem();
        obj.setName("Relsell Global");
        obj.setEmail("relsellglobal@gmail.com");
        obj.setProfile(R.mipmap.steve_jobs);
        mDrawerHeaderItemList.add(obj);


        DrawerRowItem obj1 = new DrawerRowItem();
        obj1.setmActive(true);
        obj1.setmTitle("Insights");
        obj1.setmSectionHeader(false);
        obj1.setmIcon(R.mipmap.menu);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Publications");
        obj1.setmIcon(R.mipmap.menu);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Services");
        obj1.setmIcon(R.mipmap.menu);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Contact us");
        obj1.setmIcon(R.mipmap.menu);
        mDrawerRowItemList.add(obj1);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crk_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            // start sync request here
            // lets start with services tab

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new SmartSyncTableTask(null, 2, CrkModifiedMainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new SmartSyncTableTask(null, 2, CrkModifiedMainActivity.this).execute();
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        if (Utility.getInstance().isDebug()) {
            search.setVisible(false);   // when needed i will check sync by making this true
        } else {
            search.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (int i = 0; i < mDrawerRowItemList.size(); i++) {
            DrawerRowItem obj = mDrawerRowItemList.get(i);
            if (i == 0) {
                obj.setmActive(true);
            } else {
                obj.setmActive(false);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void selectItem(int position, Bundle b) {
        Bundle args = new Bundle();

        switch (position) {
            case 4:
                Intent i = new Intent(CrkModifiedMainActivity.this, ContactUsActivity.class);
                i.putExtras(b);
                startActivityForResult(i, CONTACTUS_ACTIVITY_START_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                Drawer.closeDrawer(Gravity.LEFT);
                break;
            case 3:
                Intent i1 = new Intent(CrkModifiedMainActivity.this, ServicesActivity.class);
                i1.putExtras(b);
                startActivityForResult(i1, SERVICES_ACTIVITY_START_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                Drawer.closeDrawer(Gravity.LEFT);
                break;
            default:
                mAppBarLayout.setExpanded(false, true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAlarmReceiver != null) {
            mAlarmReceiver.setAlarm(CrkModifiedMainActivity.this);
        }

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            mAppBarLayout.setExpanded(true, true);
            if (Drawer.isDrawerOpen(Gravity.LEFT)) {
                Drawer.closeDrawer(Gravity.LEFT);
            } else {
                super.onBackPressed();
            }
        }
        mDrawerToggle.syncState();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


    /*    SlidingTabsBasicFragment slidingTabsBasicFragment = new SlidingTabsBasicFragment();
        adapter.addFragment(slidingTabsBasicFragment, "Insights");
        ArrayList<String> list = new ArrayList<>();
        list.add("Latest News");
        list.add("Statuory Happenings");
        list.add("Case Laws");*/
        // list.add("Articles");
        // list.add("News");
        //slidingTabsBasicFragment.initializeSlidingTabs(list);

        CrkRSSFragment rssfragment = new CrkRSSFragment();
        adapter.addFragment(rssfragment,"Rss Fragment");


        CrkHomeFragment fragment = new CrkHomeFragment();
        Bundle b1 = new Bundle();
        b1.putInt("categoryNo", 1);
        fragment.setArguments(b1);
        adapter.addFragment(fragment, "Publications");

        viewPager.setAdapter(adapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = null;
        String[] projection = args.getStringArray("projection");
        String selection = args.getString("selection");
        String[] selectionArgs = args.getStringArray("selectionArgs");
        String sortOrder = args.getString("sortOrder");
        if (id == 1) {
            CONTENT_URI = QuotesProvider.QuotesTable.CONTENT_URI;
        } else if (id == 2) {
            CONTENT_URI = QuotesProvider.ServicesTable.CONTENT_URI;
        } else if (id == 3) {
            CONTENT_URI = QuotesProvider.ContactsUsTable.CONTENT_URI;
        }
        return new CursorLoader(this, CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (loader.getId() == 1) {
            ArrayList<QuotesListItem> list = new ArrayList<>();
            if (data.getCount() != 0) {
                while (data.moveToNext()) {
                    String desc = data.getString(data.getColumnIndexOrThrow(QuotesProvider.QuotesTable.DESC));
                    String author = data.getString(data.getColumnIndexOrThrow(QuotesProvider.QuotesTable.AUTHOR));
                    String categoryId = data.getString(data.getColumnIndexOrThrow(QuotesProvider.QuotesTable.CATEGORY_ID));
                    QuotesListItem qt = new QuotesListItem();
                    qt.setDescription(desc);
                    qt.setAuthor(author);
                    qt.setCategoryId(categoryId);
                    list.add(qt);
                }
            }
            if (Utility.getInstance().getmListForStationaryQuotesData() == null) {
                Utility.getInstance().setmListForStationaryQuotesData(list);
            }
        } else if (loader.getId() == 2) {
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
            if (Utility.getInstance().getmListForServicesData() == null) {
                Utility.getInstance().setmListForServicesData(list);
            }
        } else if (loader.getId() == 3) {
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
            if (Utility.getInstance().getmListForContactUsData() == null) {
                Utility.getInstance().setmListForContactUsData(list);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void getDataFromDB(int queryNo) {
        Bundle b = new Bundle();
        if (queryNo == 1) {
            b.putStringArray("projection", new String[]{
                    QuotesProvider.QuotesTable._ID,
                    QuotesProvider.QuotesTable.DESC,
                    QuotesProvider.QuotesTable.AUTHOR,
                    QuotesProvider.QuotesTable.CATEGORY_ID
            });
            b.putString("selection", null);
            b.putStringArray("selectionArgs", null);
            b.putString("sortOrder", null);
        } else if (queryNo == 2) {
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
        } else if (queryNo == 3) {
            b.putStringArray("projection", new String[]{
                    QuotesProvider.ContactsUsTable._ID,
                    QuotesProvider.ContactsUsTable.DESC,
                    QuotesProvider.ContactsUsTable.CATEGORY,
                    QuotesProvider.ContactsUsTable.ISHEADER,
                    QuotesProvider.ContactsUsTable.CATEGORY_ID
            });
            b.putString("selection", null);
            b.putStringArray("selectionArgs", null);
            b.putString("sortOrder", null);
        }
        Loader local = getLoaderManager().initLoader(queryNo, b, this);
    }


    public void addDummyDataToDBForSearch() {
/*
        ArrayList<CourierListItem> arrayList = new ArrayList<CourierListItem>();

        arrayList.add(addDummyDataDBHelper("Hello Brother"));
        arrayList.add(addDummyDataDBHelper("bello Brother"));
        arrayList.add(addDummyDataDBHelper("Lolo Brother"));
        arrayList.add(addDummyDataDBHelper("Hi Brother"));
        arrayList.add(addDummyDataDBHelper("Rye Brother"));


        for (CourierListItem obj : arrayList) {
            ContentValues values = new ContentValues();
            values.put(SearchQuotesProvider.SearchTable.NAME, obj.getmCourierName());
            Uri uri = getContentResolver().insert(SearchQuotesProvider.SearchTable.CONTENT_URI,
                    values);
        }*/


        /////////////////////////////////////////////////

        // add quoteslist item

        ArrayList<QuotesListItem> arrayList1 = new ArrayList<>();
        QuotesListItem qt = new QuotesListItem();
        qt.setDescription("If you want to achieve greatness stop asking for permission.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("Anonymous");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Things work out best for those who make the best of how things work out.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("John Wooden");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("To live a creative life, we must lose our fear of being wrong.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("Anonymous");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("If you are not willing to risk the usual you will have to settle for the ordinary.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("Jim Rohn");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Trust because you are willing to accept the risk, not because it's safe or certain.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("Anonymous");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Take up one idea. Make that one idea your life--think of it, dream of it, live on that idea. Let the brain, muscles, nerves, every part of your body, be full of that idea, and just leave every other idea alone. This is the way to success.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("Swami Vivekanand");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("All our dreams can come true if we have the courage to pursue them.");
        qt.setCategory("Motivational");
        qt.setCategoryId("1");
        qt.setAuthor("Walt Disney");
        arrayList1.add(qt);

        /// add inspiration quotes

        qt = new QuotesListItem();
        qt.setDescription("The best preparation for tomorrow is doing your best today");
        qt.setCategory("Motivational");
        qt.setCategoryId("2");
        qt.setAuthor("H. Jackson Brown, Jr");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Helen Keller");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("I can't change the direction of the wind, but I can adjust my sails to always reach my destination.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Jimmy Dean");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Start by doing what's necessary; then do what's possible; and suddenly you are doing the impossible.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Francis of Assisi");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Perfection is not attainable, but if we chase perfection we can catch excellence.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Vince Lombardi");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Helen Keller");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("We must let go of the life we have planned, so as to accept the one that is waiting for us.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Joseph Campbell");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Try to be a rainbow in someone's cloud.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Maya Angelou");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Nothing is impossible, the word itself says 'I'm possible'.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("Audrey Hepburn");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("We know what we are, but know not what we may be.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("2");
        qt.setAuthor("William Shakespeare");
        arrayList1.add(qt);

        //////////////////////////////////////////

        qt = new QuotesListItem();
        qt.setDescription("If opportunity doesn't knock, build a door.");
        qt.setCategory("Others");
        qt.setCategoryId("3");
        qt.setAuthor("Milton Berle");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("The best preparation for tomorrow is doing your best today.");
        qt.setCategory("Others");
        qt.setCategoryId("3");
        qt.setAuthor("H. Jackson Brown, Jr.");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("A single act of kindness throws out roots in all directions, and the roots spring up and make new trees.");
        qt.setCategory("Others");
        qt.setCategoryId("3");
        qt.setAuthor("Amelia Earhart");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("Guard well within yourself that treasure, kindness. Know how to give without hesitation, how to lose without regret, how to acquire without meanness.");
        qt.setCategory("Others");
        qt.setCategoryId("3");
        qt.setAuthor("George Sand");
        arrayList1.add(qt);
        qt = new QuotesListItem();
        qt.setDescription("I don't want to get to the end of my life and find that I just lived the length of it. I want to have lived the width of it as well.");
        qt.setCategory("Inspirational");
        qt.setCategoryId("3");
        qt.setAuthor("Diane Ackerman");
        arrayList1.add(qt);


        for (QuotesListItem obj : arrayList1) {
            ContentValues values = new ContentValues();
            values.put(QuotesProvider.QuotesTable.DESC, obj.getDescription());
            values.put(QuotesProvider.QuotesTable.CATEGORY, obj.getCategory());
            values.put(QuotesProvider.QuotesTable.CATEGORY_ID, obj.getCategoryId());
            values.put(QuotesProvider.QuotesTable.AUTHOR, obj.getAuthor());
            Uri uri = getContentResolver().insert(QuotesProvider.QuotesTable.CONTENT_URI,
                    values);
        }


    }

    public void addDummyDataToDBForServices() {

        ArrayList<ServicesListItem> arrayList1 = new ArrayList<>();
        ServicesListItem qt = new ServicesListItem();
        qt.setDescription("Audit And Assurance Services");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Statutory Audits");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Tax Audit");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Internal Management Audits");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Internal Control Review");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Due diligence");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("System Audit");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Stock & Receivables Audit");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Forensic And Investigative");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Fixed Assets Audit");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("TEV Study");
        qt.setCategory("Audit And Assurance Services");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Direct And Indirect Taxation");
        qt.setCategory("Main");
        qt.setHeader("true");
        qt.setCategoryId("0");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Corporate and personal tax compliance including income-tax assessments, Appeals before the Commissioner (Appeals) and the Income-tax Appellate Tribunal");
        qt.setCategory("Direct And Indirect Taxation");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("International and Domestic Tax Planning");
        qt.setCategory("Direct And Indirect Taxation");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Filing of Income-tax and Wealth-tax returns of residents and non-residents individuals, domestic and foreign companies and other entities");
        qt.setCategory("Direct And Indirect Taxation");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Company Law Matters");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Formation of Indian and Offshore Companies");
        qt.setCategory("Company Law Matters");
        qt.setCategoryId("3");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription(" Advising on various matters under the Companies Act, 1956 including appearance before the Company Law Board");
        qt.setCategory("Company Law Matters");
        qt.setCategoryId("3");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Assisting in Winding-up of companies/striking off the name from the Registrar of Companies under the Act.");
        qt.setCategory("Company Law Matters");
        qt.setCategoryId("3");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("  Maintenance of statutory records and registers as per Indian Companies Act.");
        qt.setCategory("Company Law Matters");
        qt.setCategoryId("3");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Accounting & Related Consultancy");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Design, implementation and review of accounting manuals including those of urban local bodies.");
        qt.setCategory("Accounting & Related Consultancy");
        qt.setCategoryId("4");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Advice on various accounting issues including those related to Indian GAAPs, International GAAPs and US GAAPs.");
        qt.setCategory("Accounting & Related Consultancy");
        qt.setCategoryId("4");
        arrayList1.add(qt);


        qt = new ServicesListItem();
        qt.setDescription("Business/Knowledge Process Outsourcing");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);


        qt = new ServicesListItem();
        qt.setDescription("Book keeping and preparation of final accounts.");
        qt.setCategory("Business/Knowledge Process Outsourcing");
        qt.setCategoryId("5");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Payroll Processing.");
        qt.setCategory("Business/Knowledge Process Outsourcing");
        qt.setCategoryId("5");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Preparation of management accounts and management information systems.");
        qt.setCategory("Business/Knowledge Process Outsourcing");
        qt.setCategoryId("5");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Fixed Assets verification and completion of records.");
        qt.setCategory("Business/Knowledge Process Outsourcing");
        qt.setCategoryId("5");
        arrayList1.add(qt);


        qt = new ServicesListItem();
        qt.setDescription("Foreign Exchange Management Act 1999, (FEMA)");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Advice on various foreign exchange matters under the Act including in connection with that stemming from inbound investment into India and outbound investment outside India.");
        qt.setCategory("Foreign Exchange Management Act 1999, (FEMA)");
        qt.setCategoryId("6");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Obtaining various approvals under the said Act from the Reserve Bank of India (RBI) and providing assistance in complying with requirement prescribed by the RBI.");
        qt.setCategory("Foreign Exchange Management Act 1999, (FEMA)");
        qt.setCategoryId("6");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Corporate Advisory Services");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);


        qt = new ServicesListItem();
        qt.setDescription("Share Valuations.");
        qt.setCategory("Corporate Advisory Services");
        qt.setCategoryId("7");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Mergers, Demergers and Acquisitions.");
        qt.setCategory("Corporate Advisory Services");
        qt.setCategoryId("7");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Capital Restructuring.");
        qt.setCategory("Corporate Advisory Services");
        qt.setCategoryId("7");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Personal Advisory Services");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);

        qt = new ServicesListItem();
        qt.setDescription("Personal financial planning.");
        qt.setCategory("Personal Advisory Services");
        qt.setCategoryId("8");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Insurance and pension planning.");
        qt.setCategory("Personal Advisory Services");
        qt.setCategoryId("8");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Formation of trusts.");
        qt.setCategory("Personal Advisory Services");
        qt.setCategoryId("8");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Acting as arbitrator.");
        qt.setCategory("Personal Advisory Services");
        qt.setCategoryId("8");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Wills.");
        qt.setCategory("Personal Advisory Services");
        qt.setCategoryId("8");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Project Finance");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Project report preparation for availing term loans and working capital loans from banks and financial institutions.");
        qt.setCategory("Project Finance");
        qt.setCategoryId("9");
        arrayList1.add(qt);


        for (ServicesListItem obj : arrayList1) {
            ContentValues values = new ContentValues();
            values.put(QuotesProvider.ServicesTable.DESC, obj.getDescription());
            values.put(QuotesProvider.ServicesTable.CATEGORY, obj.getCategory());
            values.put(QuotesProvider.ServicesTable.ISHEADER, "" + obj.isHeader());
            values.put(QuotesProvider.ServicesTable.CATEGORY_ID, obj.getCategoryId());
            Uri uri = getContentResolver().insert(QuotesProvider.ServicesTable.CONTENT_URI,
                    values);
        }


    }

    public void addDummyDataToDBForContactUs() {

        ArrayList<ServicesListItem> arrayList1 = new ArrayList<>();
        ServicesListItem qt = new ServicesListItem();
        qt.setDescription("Head Office - Hyderabad");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("CRK & ASSOCIATES");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Chartered Accountants");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Plot No 3A, Navodaya Colony,");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Road No 14, Banjara Hills,");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Hyderabad-500034");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Andhra Pradesh, India");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Phone no=+91-40-23552305");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Fax=+91-40-23552304");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Email=info@crkandassociates.com");
        qt.setCategory("Head Office - Hyderabad");
        qt.setCategoryId("1");
        arrayList1.add(qt);


        qt = new ServicesListItem();
        qt.setDescription("Branch - Bangalore");
        qt.setCategory("Main");
        qt.setCategoryId("0");
        qt.setHeader("true");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("CRK & ASSOCIATES");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Chartered Accountants");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("No.157, New No.2,");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("21st Cross, Kaggadasapura");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("C V Raman Nagar,");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Bangalore - 560093");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Karnataka, India");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);
        qt = new ServicesListItem();
        qt.setDescription("Email=info@crkandassociates.com");
        qt.setCategory("Branch - Bangalore");
        qt.setCategoryId("2");
        arrayList1.add(qt);


        for (ServicesListItem obj : arrayList1) {
            ContentValues values = new ContentValues();
            values.put(QuotesProvider.ServicesTable.DESC, obj.getDescription());
            values.put(QuotesProvider.ServicesTable.CATEGORY, obj.getCategory());
            values.put(QuotesProvider.ServicesTable.ISHEADER, "" + obj.isHeader());
            values.put(QuotesProvider.ServicesTable.CATEGORY_ID, obj.getCategoryId());
            Uri uri = getContentResolver().insert(QuotesProvider.ContactsUsTable.CONTENT_URI,
                    values);
        }
    }


    public class SyncTableTask extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;
        int syncTableVar;
        int postDataVar;

        HashMap<String, String> hmVars;


        public SyncTableTask(HashMap<String, String> hm, int syncTableVar) {
            this.hmVars = hm;
            this.syncTableVar = syncTableVar;

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

                                Cursor c = getContentResolver().query(QuotesProvider.MetaDataTable.CONTENT_URI, new String[]{QuotesProvider.MetaDataTable.NAME, QuotesProvider.MetaDataTable.UPDATE_TIME}, QuotesProvider.MetaDataTable.NAME + "=?", new String[]{obj.getmTableName()}, null);

                                ContentValues values = new ContentValues();
                                values.put(QuotesProvider.MetaDataTable.NAME, obj.getmTableName());
                                values.put(QuotesProvider.MetaDataTable.CREATE_TIME, obj.getmCreateTime());
                                values.put(QuotesProvider.MetaDataTable.UPDATE_TIME, "" + obj.getmUpdatedTime());
                                if (c == null || c.getCount() == 0) {
                                    Uri uri = getContentResolver().insert(QuotesProvider.MetaDataTable.CONTENT_URI,
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
                                            System.out.println("Database needs a sync");
                                            // we can trigger a sync request from here
                                        }


                                    }


                                    int r = getContentResolver().update(QuotesProvider.MetaDataTable.CONTENT_URI,
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
                                Uri uri = getContentResolver().insert(QuotesProvider.ServicesTable.CONTENT_URI,
                                        values);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } else if (syncTableVar == 3) {

                    } else if (syncTableVar == 4) {

                    } else if (syncTableVar == 5) {

                    }

                    getDataFromDB(1);
                    getDataFromDB(2);    // services table
                    getDataFromDB(3);


                }
            } else {

                if (mConnectionCode >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    Toast.makeText(CrkModifiedMainActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();


                } else if (mConnectionCode >= HttpURLConnection.HTTP_BAD_REQUEST && mConnectionCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {

                    Toast.makeText(CrkModifiedMainActivity.this, "Client Side Error", Toast.LENGTH_LONG).show();


                } else if (mConnectionCode >= HttpURLConnection.HTTP_MULT_CHOICE && mConnectionCode < HttpURLConnection.HTTP_BAD_REQUEST) {

                    Toast.makeText(CrkModifiedMainActivity.this, "Server Page is temp moved", Toast.LENGTH_LONG).show();

                }
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlarmReceiver != null) {
            mAlarmReceiver.cancelAlarm(CrkModifiedMainActivity.this);
        }
    }


}