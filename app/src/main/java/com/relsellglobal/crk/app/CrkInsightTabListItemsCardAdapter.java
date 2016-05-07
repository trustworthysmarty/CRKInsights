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


import com.relsellglobal.crk.app.pojo.DBReaderRssItem;
import com.relsellglobal.crk.app.pojo.DrawerRowItem;
import com.relsellglobal.crk.app.pojo.QuotesListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vrs on 3/9/15.
 */
public class CrkInsightTabListItemsCardAdapter extends RecyclerView.Adapter<CrkInsightTabListItemsCardAdapter.ViewHolder> {

    private Context mContext;
    List<DBReaderRssItem> list = new ArrayList<>();
    FragmentManager fragmentManager;
    int containerLayoutId;
    Typeface tf ;
    public static final int HEADER = 1;
    public static final int CHILD = 2;


    public CrkInsightTabListItemsCardAdapter(Context mContext, List<DBReaderRssItem> list, FragmentManager fm, int containerLayoutId) {
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
                        .inflate(R.layout.section_header_layout, parent, false);
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

                final DBReaderRssItem localObj = list.get(position);

                holder.cardHeading.setText(localObj.getTitle());
                holder.cardtitle.setText(localObj.getDescription());
                holder.publishedDateTV.setText(localObj.getPubDate());

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

    public DBReaderRssItem getItem(int i) {
        return list.get(i);
    }



    // viewholder class



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cardHeading;
        TextView cardtitle;
        TextView quoteAuthor;
        LinearLayout downloadButtonLayout;
        TextView publishedDateTV;

        public ViewHolder(View itemView,int type) {
            super(itemView);
            switch(type) {
                case CHILD:
                    cardHeading = (TextView)itemView.findViewById(R.id.cardttitle);
                    cardtitle = (TextView) itemView.findViewById(R.id.card_desc);
                    cardtitle.setTypeface(tf);
                    quoteAuthor = (TextView) itemView.findViewById(R.id.author);
                    downloadButtonLayout = (LinearLayout)itemView.findViewById(R.id.downloadBtnLayout);
                    publishedDateTV  = (TextView)itemView.findViewById(R.id.pubDate);
                    break;
                default:
                    break;

            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        DBReaderRssItem item = list.get(position);
        int res = item.isSectionHeader() ? HEADER : CHILD;
        return res;
    }
}

