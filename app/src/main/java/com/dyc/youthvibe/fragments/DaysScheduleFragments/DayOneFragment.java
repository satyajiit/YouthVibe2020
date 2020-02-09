package com.dyc.youthvibe.fragments.DaysScheduleFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyc.youthvibe.GetterSetter.ScheduleModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.ScheduleItemsAdapter;
import com.dyc.youthvibe.utils.jsonLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayOneFragment extends Fragment {


    RecyclerView eventsListRecycler;
    private List<ScheduleModel> eventsList = new ArrayList<>();
    ScheduleItemsAdapter adapter;

   // int sessionFlag = 0;

    public DayOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_one, container, false);

        initUI(view);


        setUpRecycler();


        return view;
    }


    void initUI(View v){

        eventsListRecycler = v.findViewById(R.id.eventsList);

    }

    void setUpRecycler(){


        adapter = new ScheduleItemsAdapter(eventsList, getActivity().getApplicationContext(), 13);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        eventsListRecycler.setLayoutManager(mLayoutManager);
        eventsListRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        eventsListRecycler.setAdapter(adapter);


        JSONObject jsonObject;


        if (eventsList.size() == 0) {

            try {

                jsonObject = new jsonLoader(getActivity(), "EventsDataSchedule.json").loadJSONFromAsset().getJSONObject("DAY1");


                ScheduleModel list;


                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {


                        list = new ScheduleModel(key, jsonObject.getJSONArray(key).get(1).toString(), jsonObject.getJSONArray(key).get(0).toString());

                        eventsList.add(list);

                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }


            } catch (Exception e) {

                //Show error dialog here


            }


        }


        adapter.notifyDataSetChanged();




    }


}
