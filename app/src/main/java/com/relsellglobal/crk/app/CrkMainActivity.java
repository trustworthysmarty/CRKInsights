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
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.relsellglobal.crk.app.contentproviders.QuotesProvider;
import com.relsellglobal.crk.app.customcomponents.NonSwipableViewPager;
import com.relsellglobal.crk.app.pojo.ContactUsListItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.pojo.ServicesListItem;
import com.relsellglobal.crk.app.util.Utility;
import com.relsellglobal.crk.app.viewflipperscroller.SimpleFlipperFragment;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by anilkukreti on 09/04/16.
 */

public class CrkMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;
    AppBarLayout mAppBarLayout;

    NavigationView mNavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crk_activity_main_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mContentFrameLayoutTwo = (ViewPager) findViewById(R.id.viewpager);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar.setTitle("");

        String dbcreatedValue = Utility.getInstance().getDataFromPefs(mDbPerfsFileName,"dbcreated");
        if((dbcreatedValue == null) || (dbcreatedValue != null && dbcreatedValue.equalsIgnoreCase("false"))) {
            addDummyDataToDBForSearch();
            addDummyDataToDBForServices();
            addDummyDataToDBForContactUs();
            Utility.getInstance().writeToPrefs(mDbPerfsFileName,"dbcreated","true");
        }

        SimpleFragment fragment = new SimpleFragment();

        getSupportFragmentManager().beginTransaction().replace(mFrameLayout.getId(), fragment, "simple_fragment").commit();


       /* SimpleFragmentDisplayingList fragment2 = new SimpleFragmentDisplayingList();

        Bundle b = new Bundle();
        b.putInt("containerId", mContentFrameLayoutTwo.getId());
        fragment2.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(mContentFrameLayoutTwo.getId(), fragment2, "simple_fragment_two").commit();*/

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

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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


        mNavigationView.setCheckedItem(0);

        mNavigationView.setItemBackground(this.getResources().getDrawable(R.drawable.bg_menu_item_selector));


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


        getDataFromDB(1);
        getDataFromDB(2);
        getDataFromDB(3);

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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.search_cancel) {

            searchOpen = false;
            invalidateOptionsMenu();
        } else if (id == R.id.search) {

            searchOpen = true;
            invalidateOptionsMenu();


            //SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            // if (searchView != null) {


               /* searchView.setQueryRefinementEnabled(true);



                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });*/
            // }
            return true;
        }

        // Associate searchable configuration with the SearchView


        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        MenuItem searchMenuCancel = menu.findItem(R.id.search_cancel);
        if (searchOpen) {
            search.setVisible(false);
            searchMenuCancel.setVisible(true);
        } else {
            search.setVisible(true);
            searchMenuCancel.setVisible(false);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNavigationView.setCheckedItem(R.id.homescreen);
    }

    public void selectItem(int position, Bundle b) {
        Bundle args = new Bundle();

        switch (position) {
            case 4:
                Intent i = new Intent(CrkMainActivity.this, ContactUsActivity.class);
                i.putExtras(b);
                startActivityForResult(i, CONTACTUS_ACTIVITY_START_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                Drawer.closeDrawer(Gravity.LEFT);
                break;
            case 5:
                Intent i1 = new Intent(CrkMainActivity.this, ServicesActivity.class);
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


        SlidingTabsBasicFragment slidingTabsBasicFragment = new SlidingTabsBasicFragment();
        adapter.addFragment(slidingTabsBasicFragment, "Insights");
        ArrayList<String> list = new ArrayList<>();
        list.add("Latest News");
        list.add("Statuory Happenings");
        list.add("Case Laws");
        list.add("Articles");
        list.add("News");
        slidingTabsBasicFragment.initializeSlidingTabs(list);


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


        if(loader.getId() == 1) {
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
        } else if(loader.getId() == 2) {
            ArrayList<ServicesListItem> list = new ArrayList<>();
            if (data.getCount() != 0) {
                while (data.moveToNext()) {
                    String desc = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.DESC));
                    String categoryName = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.CATEGORY));
                    String categoryId = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.CATEGORY_ID));

                    String headerValue =  data.getString(data.getColumnIndexOrThrow(QuotesProvider.ServicesTable.ISHEADER));

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
        }  else if(loader.getId() == 3) {
            ArrayList<ContactUsListItem> list = new ArrayList<>();
            if (data.getCount() != 0) {
                while (data.moveToNext()) {
                    String desc = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.DESC));
                    String categoryName = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.CATEGORY));
                    String categoryId = data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.CATEGORY_ID));

                    String headerValue =  data.getString(data.getColumnIndexOrThrow(QuotesProvider.ContactsUsTable.ISHEADER));

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
        } else if(queryNo == 2) {
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
        } else if(queryNo == 3) {
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
            values.put(QuotesProvider.ServicesTable.ISHEADER, ""+obj.isHeader());
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
            values.put(QuotesProvider.ServicesTable.ISHEADER, ""+obj.isHeader());
            values.put(QuotesProvider.ServicesTable.CATEGORY_ID, obj.getCategoryId());
            Uri uri = getContentResolver().insert(QuotesProvider.ContactsUsTable.CONTENT_URI,
                    values);
        }


    }


}