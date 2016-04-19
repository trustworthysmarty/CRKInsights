package com.relsellglobal.crk.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class SimpleFragment extends Fragment {

    ImageView mImageView;



    int []imageArr = {R.mipmap.banner_one_o,R.mipmap.banner_two_o,R.mipmap.banner_three_o};

    int arrCount;

    boolean imageNotChange;

    Animation fadeIn,fadeOut;
    AnimationSet animation;

    CustomLinearLayoutDots dotsLayout;


    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView != null && imageArr != null && getActivity() != null) {
                arrCount++;
                if (arrCount == imageArr.length) {
                    arrCount = 0;
                }
                if(arrCount >=0 && arrCount < imageArr.length) {
                    mImageView.startAnimation(fadeOut);
                    dotsLayout.setmDifficultyLevel(arrCount);
                    mImageView.setImageDrawable(getActivity().getResources().getDrawable(imageArr[arrCount]));
                    mImageView.startAnimation(fadeIn);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.simple_fragment_main,container,false);
        mImageView  = (ImageView)v.findViewById(R.id.imageView);
        dotsLayout = (CustomLinearLayoutDots)v.findViewById(R.id.dots_layout);

        return v ;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dotsLayout.setmDifficultyLevel(arrCount);
        fadeIn= AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        fadeOut= AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        new ImageChangerTask().execute();
        //mImageView.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.banner_one));
    }

    public class ImageChangerTask extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            while(isImageChanging()) {
                try {
                    Thread.sleep(4000);
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
