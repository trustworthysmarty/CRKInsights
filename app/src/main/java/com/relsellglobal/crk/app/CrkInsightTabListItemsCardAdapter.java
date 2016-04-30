package com.relsellglobal.crk.app;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.relsellglobal.crk.app.pojo.DrawerRowItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vrs on 3/9/15.
 */
public class CrkInsightTabListItemsCardAdapter extends RecyclerView.Adapter<CrkInsightTabListItemsCardAdapter.ViewHolder> {

    private Context mContext;
    List<DrawerRowItem> list = new ArrayList<>();
    FragmentManager fragmentManager;
    int containerLayoutId;
    Typeface tf ;
    public static final int HEADER = 1;
    public static final int CHILD = 2;


    public CrkInsightTabListItemsCardAdapter(Context mContext, List<DrawerRowItem> list, FragmentManager fm, int containerLayoutId) {
        this.mContext = mContext;
        this.list = list;
        this.fragmentManager = fm;
        this.containerLayoutId = containerLayoutId;
        this.tf = Typeface.createFromAsset(mContext.getAssets(), "BOD_BLAR.TTF");

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch(viewType) {
            case HEADER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.crk_homefragment_listitem, parent, false);
                return new ViewHolder(itemView,HEADER);
            case CHILD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.crk_insights_listitem, parent, false);
                return new ViewHolder(itemView,CHILD);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int viewType = holder.getItemViewType();

        switch (viewType) {
            case CHILD:

                final DrawerRowItem localObj = list.get(position);

                holder.cardtitle.setText(localObj.getmTitle());


                holder.downloadButtonLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });



                break;
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public DrawerRowItem getItem(int i) {
        return list.get(i);
    }



    // viewholder class



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardtitle;
        TextView quoteAuthor;
        LinearLayout downloadButtonLayout;

        public ViewHolder(View itemView,int type) {
            super(itemView);
            switch(type) {
                case CHILD:
                    cardtitle = (TextView) itemView.findViewById(R.id.cardtitle);
                    cardtitle.setTypeface(tf);
                    quoteAuthor = (TextView) itemView.findViewById(R.id.author);
                    downloadButtonLayout = (LinearLayout)itemView.findViewById(R.id.downloadBtnLayout);
                    break;
                default:
                    break;

            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return CHILD;
            default:
                return CHILD;
        }
    }
}

