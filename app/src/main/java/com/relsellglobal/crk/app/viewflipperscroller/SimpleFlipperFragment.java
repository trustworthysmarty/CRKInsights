package com.relsellglobal.crk.app.viewflipperscroller;

import android.app.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.relsellglobal.crk.app.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SimpleFlipperFragment extends Fragment {
    AdapterViewFlipper mFlipper;

    private ArrayList<Integer> banners;

    private List<String> itemsTry;
    MyAdapterViewFlipper mAdapter;
    public LruCache mMemoryCache;
    //private DiskLruCache mDiskLruCache;
    private static final String TAG = "SimpleFlipperFragment";
    private Activity mCaller;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        banners = new ArrayList<>();
        banners.add(R.mipmap.banner_one_o);
        banners.add(R.mipmap.banner_two_o);
        banners.add(R.mipmap.banner_three_o);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Resources resources = getResources();
        Bundle b = this.getArguments();
        mAdapter = new MyAdapterViewFlipper(banners,getActivity());
        mFlipper.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.adapterviewfilpper, null, false);
        mFlipper = (AdapterViewFlipper)v.findViewById(R.id.flipper);
        mFlipper.setAutoStart(true);
        mFlipper.setFlipInterval (3000);

        return v;
    }






}



