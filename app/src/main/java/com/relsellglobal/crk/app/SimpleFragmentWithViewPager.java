package com.relsellglobal.crk.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.relsellglobal.crk.app.customcomponents.CustomLinearLayoutDots;


/**
 * Created by anilkukreti on 29/02/16.
 */
public class SimpleFragmentWithViewPager extends Fragment {

    ViewPager mViewPager;

    CustomPagerAdapterForImageScroller mAdapter;


    boolean imageNotChange;
    int imageCounter;


    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageCounter++;
            if(imageCounter > mAdapter.getCount()) {
                imageCounter = 0;
            }
            mViewPager.setCurrentItem(imageCounter,true);

        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.image_scroller,container,false);

        mViewPager = (ViewPager)v.findViewById(R.id.pager);

        return v ;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new CustomPagerAdapterForImageScroller(getActivity());
        mViewPager.setCurrentItem(imageCounter,true);
        mViewPager.setAdapter(mAdapter);
        new ImageChangerTask().execute();

    }

    public class ImageChangerTask extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            while(isImageChanging()) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    myHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


    public boolean isImageChanging() {
        return imageNotChange == false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageNotChange = true;
    }



}
