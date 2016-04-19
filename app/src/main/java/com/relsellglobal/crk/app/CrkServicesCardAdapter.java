package com.relsellglobal.crk.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by vrs on 3/9/15.
 */
public class CrkServicesCardAdapter extends RecyclerView.Adapter<CrkServicesCardAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<String> list = new ArrayList<>();

    public static final int HEADER = 1;
    public static final int CHILD = 2;


    public CrkServicesCardAdapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch(viewType) {
            case HEADER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.crk_services_listitem_header, parent, false);
                return new ViewHolder(itemView,HEADER);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int viewType = holder.getItemViewType();

        String []configStringAddressArray = null;
        String configFaxStr = null;
        String configEmailStr = null;
        String configPhoneStr = null;
        if(position == 0) {
            configStringAddressArray = new String[] {"Statutory Audits","Tax Audit","Internal Management Audits","Internal Control Review","Due-diligence","Systems Audit","Stock & Receivables Audit","Forensic & Investigative Audit","Fixed Assets Audit","TEV Study"
            };
            configFaxStr = "+91-40-23552304";
        } else if(position == 1) {
            configStringAddressArray = new String[] {"Statutory Audits","Tax Audit","Internal Management Audits","Internal Control Review","Due-diligence","Systems Audit","Stock & Receivables Audit","Forensic & Investigative Audit","Fixed Assets Audit","TEV Study"
            };
        }


        switch (viewType) {
            case HEADER:
                int k = configStringAddressArray.length;
                ArrayList<TextView> list = new ArrayList<>();
                TextView tv = null;
                for(int i=0;i<k;i++) {
                    tv = new TextView(mContext);
                    tv.setTextColor(Color.BLACK);
                    String str = "\u2799 "+configStringAddressArray[i];
                    tv.setText(str);
                    //list.add(tv);
                    holder.mDynamicAddressLayout.addView(tv);
                }

                break;
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 2;
    }




    // viewholder class



    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mDynamicAddressLayout;

        public ViewHolder(View itemView,int type) {
            super(itemView);
            switch(type) {
                case HEADER:
                    mDynamicAddressLayout = (LinearLayout) itemView.findViewById(R.id.address_layout);
                    break;
                default:
                    break;

            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            default:
                return HEADER;
        }
    }
}

