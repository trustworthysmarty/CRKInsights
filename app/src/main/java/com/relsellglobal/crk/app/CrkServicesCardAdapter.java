package com.relsellglobal.crk.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.relsellglobal.crk.app.pojo.ServicesListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by vrs on 3/9/15.
 */
public class CrkServicesCardAdapter extends RecyclerView.Adapter<CrkServicesCardAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<ServicesListItem> list = new ArrayList<>();
    ArrayList<ServicesListItem> modifiedHeaderlist = new ArrayList<>();
    int modifiedHeaderListCounter;
    public static final int HEADER = 1;
    public static final int CHILD = 2;
    private ArrayList<String> categoryArray;
    private int categoryCount;


    public CrkServicesCardAdapter(Context mContext, ArrayList<ServicesListItem> list) {
        this.mContext = mContext;
        this.list = list;
        categoryCountCalculate();
    }

    public void categoryCountCalculate() {
        for (ServicesListItem obj: list) {
            String categoryId = obj.getCategoryId();
            if(categoryId.equalsIgnoreCase("0")) {
                modifiedHeaderlist.add(obj);
                categoryCount++;
            }
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crk_services_listitem_header, parent, false);
        return new ViewHolder(itemView, HEADER);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int viewType = holder.getItemViewType();

        /*ArrayList<String> configStringAddressArray = null;
        String configFaxStr = null;
        String configEmailStr = null;
        String configPhoneStr = null;
        if (position == 0) {
            configStringAddressArray = getStringArrData(2);
            categoryArray = getStringArrData(3);

        } else if (position == 1) {

        }*/

        if (holder.headerTv != null) {
            if (modifiedHeaderListCounter < modifiedHeaderlist.size()) {
                ServicesListItem obj = modifiedHeaderlist.get(modifiedHeaderListCounter);
                if (obj.isHeader().equalsIgnoreCase("true")) {
                    holder.headerTv.setText(obj.getDescription());
                    modifiedHeaderListCounter++;
                }
                TextView tv = null;
                ArrayList<String> childList = new ArrayList();
                String categoryName = obj.getDescription();
                for (ServicesListItem obj1 : list) {
                    if (obj1.getCategory().equalsIgnoreCase(categoryName)) {
                        childList.add(obj1.getDescription());
                    }
                }
                for (int i = 0; i < childList.size(); i++) {
                    tv = new TextView(mContext);
                    tv.setTextColor(Color.BLACK);
                    String str = "\u2799 " + childList.get(i);
                    tv.setText(str);
                    holder.mDynamicAddressLayout.addView(tv);
                }
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return categoryCount;
    }


    public ArrayList<String> getStringArrData(int dataVar) {

        ArrayList<String> result = new ArrayList<>();
        if (dataVar == 2) {
            for (ServicesListItem obj : list) {
                result.add(obj.getDescription());
            }
        } else if (dataVar == 3) {
            for (ServicesListItem obj : list) {
                if (!result.contains(obj.getCategory())) {
                    result.add(obj.getCategory());
                }
            }
        }
        return result;

    }


    // viewholder class


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mDynamicAddressLayout;

        // header Ux element
        TextView headerTv;


        // child ux elements
        TextView childTv;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            headerTv = (TextView) itemView.findViewById(R.id.headoffice_tv);
            mDynamicAddressLayout = (LinearLayout)itemView.findViewById(R.id.address_layout);
        }
    }

}

