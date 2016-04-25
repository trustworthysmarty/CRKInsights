package com.relsellglobal.crk.app.viewflipperscroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.relsellglobal.crk.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anil on 21/6/15.
 */
public class MyAdapterViewFlipper extends BaseAdapter{

    private ArrayList<Integer> mList;
    private Context mContext;

    public MyAdapterViewFlipper(ArrayList<Integer> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }



    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = null;
        ViewHolder holder = null;
        if(mContext != null) {
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.adapateritem,null);
               // convertView = imageView = new ImageView(mContext);
                imageView = (ImageView)convertView.findViewById(R.id.imgView);
                //imageView.setLayoutParams(layoutParams);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.imgView = imageView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imgView.setImageResource(mList.get(position));
        }
        return convertView;

    }
    static class ViewHolder {
        ImageView imgView;
    }


}
