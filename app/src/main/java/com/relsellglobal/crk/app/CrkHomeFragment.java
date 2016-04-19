package com.relsellglobal.crk.app;

/**
 * Created by anilkukreti on 01/03/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.relsellglobal.crk.app.pojo.QuotesListItem;
import com.relsellglobal.crk.app.util.Utility;

import java.util.ArrayList;
import java.util.List;


public class CrkHomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    CrkHomeListItemsCardAdapter adapter;
    int categoryNo;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.crk_home_fragment_list_main,container,false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        return v;
    }

    private void initializeData() {
        /*flowers = new ArrayList<>();
        flowers.add(new Flower("Either you run the day or the day runs you.", R.drawable.image2));
        flowers.add(new Flower("Good, better, best. Never let it rest. 'Til your good is better and your better is best.", R.drawable.images3));
        flowers.add(new Flower("When you reach the end of your rope, tie a knot in it and hang on.", R.drawable.images4));
        flowers.add(new Flower("Accept the challenges so that you can feel the exhilaration of victory.", R.drawable.images6));
        flowers.add(new Flower("Life is 10% what happens to you and 90% how you react to it.", R.drawable.images7));
        flowers.add(new Flower("In order to succeed, we must first believe that we can.", R.drawable.images10));
        flowers.add(new Flower("Failure will never overtake me if my determination to succeed is strong enough.", R.drawable.images11));
        flowers.add(new Flower("What you do today can improve all your tomorrows.", R.drawable.images12));
        flowers.add(new Flower("A creative man is motivated by the desire to achieve, not by the desire to beat others.", R.drawable.images14));
        flowers.add(new Flower("The secret of getting ahead is getting started.", R.drawable.images17));
        flowers.add(new Flower("Either you run the day or the day runs you.", R.drawable.images18));
        flowers.add(new Flower("Good, better, best. Never let it rest. 'Til your good is better and your better is best.", R.drawable.index));*/

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        //this.containerId = getArguments().getInt("containerId");
        categoryNo = getArguments().getInt("categoryNo");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        //initializeData();
        ArrayList<QuotesListItem> list = Utility.getInstance().getmListForStationaryQuotesData();
        ArrayList<QuotesListItem> modifiedList = new ArrayList();
        if(list != null) {
            for(QuotesListItem localObj : list) {
                int localCategoryId = Integer.parseInt(localObj.getCategoryId());
                if(localCategoryId == categoryNo) {
                    modifiedList.add(localObj);
                }
            }
        }
        if(modifiedList != null) {
            adapter = new CrkHomeListItemsCardAdapter(getActivity(), modifiedList, getActivity().getSupportFragmentManager(), categoryNo);
            mRecyclerView.setAdapter(adapter);
        }


    }


}
