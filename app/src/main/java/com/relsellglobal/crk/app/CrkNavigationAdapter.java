package com.relsellglobal.crk.app;

/**
 * Created by anil on 10/7/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.relsellglobal.crk.app.pojo.DrawerHeaderItem;
import com.relsellglobal.crk.app.pojo.DrawerRowItem;

import java.util.ArrayList;

/**
 * Created by relsell global on 28-12-2014.
 */
public class CrkNavigationAdapter extends RecyclerView.Adapter<CrkNavigationAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];

    private String name;
    private int profile;
    private String email;
    private ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    private ArrayList<DrawerRowItem> mDrawerRowItemList;
    private Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;
        LinearLayout ll;
        LinearLayout backLayout;
        LinearLayout linearll;
        TextView textView;
        de.hdodenhof.circleimageview.CircleImageView imageView;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);


            if (ViewType == TYPE_ITEM) {
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                backLayout = (LinearLayout) itemView.findViewById(R.id.back_layout);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = TYPE_ITEM;
            } else if (ViewType == TYPE_HEADER) {
                Holderid = TYPE_HEADER;
            }
        }

    }


    CrkNavigationAdapter(Context context, ArrayList<DrawerHeaderItem> drawerHeaderItemList, ArrayList<DrawerRowItem> drawerRowItemList) {

        mDrawerHeaderItemList = drawerHeaderItemList;
        mDrawerRowItemList = drawerRowItemList;
        mContext = context;

    }


    @Override
    public CrkNavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader;


        }

        return null;

    }


    @Override
    public void onBindViewHolder(CrkNavigationAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == TYPE_ITEM) {
            DrawerRowItem localObj = mDrawerRowItemList.get(position - 1);
            holder.textView.setText(localObj.getmTitle());
            if(localObj.ismActive()) {
               holder.backLayout.setBackgroundColor(Color.parseColor("#EEEEEE"));
            }else {
                holder.backLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            // holder.imageView.setImageResource(localObj.getmIcon());
        } else if (holder.Holderid == TYPE_HEADER) {
            //holder.Name.setText(Html.fromHtml(mContext.getString(R.string.torrins_title)));
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerHeaderItemList.size() + mDrawerRowItemList.size();
    }


    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                DrawerRowItem obj = mDrawerRowItemList.get(position - 1);
                return TYPE_ITEM;
        }
    }

}