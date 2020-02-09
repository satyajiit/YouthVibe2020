package com.dyc.youthvibe.FaqFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.FaqModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.FaqAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FaqOneRegistration extends Fragment {


    RecyclerView faqListRecycler;
    private List<FaqModel> faqList = new ArrayList<>();
    FaqAdapter adapter;
    JSONObject jsonObject;
    String data;

    public FaqOneRegistration(String data) {
        // Required empty public constructor

        this.data = data;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.faq_single_fragment, container, false);

        initUI(view);


        setUpRecycler();


        return view;
    }


    void initUI(View v){

        faqListRecycler = v.findViewById(R.id.faqListRecycler);


        try {
            jsonObject = new JSONObject(loadJSONFromAsset()).getJSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    void setUpRecycler(){



        adapter = new FaqAdapter(faqList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        faqListRecycler.setLayoutManager(mLayoutManager);
        faqListRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        faqListRecycler.setAdapter(adapter);



    if (faqList.size()==0) {


        try {


            FaqModel list;


            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {


                    list = new FaqModel(key, jsonObject.getString(key));

                    faqList.add(list);

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


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("faqdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



}
