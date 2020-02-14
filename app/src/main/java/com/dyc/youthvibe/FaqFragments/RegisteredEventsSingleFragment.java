package com.dyc.youthvibe.FaqFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dyc.youthvibe.GetterSetter.RegisteredModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.RegisteredEventsAdapter;
import com.dyc.youthvibe.utils.jsonLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RegisteredEventsSingleFragment extends Fragment{


    RecyclerView faqListRecycler;
    private List<RegisteredModel> faqList = new ArrayList<>();
    String data;
    RegisteredEventsAdapter adapter;
    TextView emptyTV;

    public RegisteredEventsSingleFragment(String data) {
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
        emptyTV= v.findViewById(R.id.emptyTV);




    }

    void setUpRecycler(){



        adapter = new RegisteredEventsAdapter(faqList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        faqListRecycler.setLayoutManager(mLayoutManager);
        faqListRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        faqListRecycler.setAdapter(adapter);

        if (faqList.size()==0) {

            JSONObject jsonObject;
            JSONArray jsonArray;

            RegisteredModel registeredModel;

            try {

                jsonObject = new jsonLoader(getActivity(), "profile.json").loadJSONFromAsset();
                jsonArray = jsonObject.getJSONArray(data);


                if (data.equals("solo_events"))

                    for (int i = 0; i < jsonArray.length(); i++) {


                        registeredModel = new RegisteredModel(jsonArray.getJSONObject(i).getString("name"), "0", "0");

                        faqList.add(registeredModel);

                    }

                else if (data.equals("workshops"))

                    for (int i = 0; i < jsonArray.length(); i++) {


                        registeredModel = new RegisteredModel(jsonArray.getJSONObject(i).getString("name"), "0", "0");

                        faqList.add(registeredModel);

                    }

                else

                    for (int i = 0; i < jsonArray.length(); i++) {


                        registeredModel = new RegisteredModel(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("team_name"),
                                jsonArray.getJSONObject(i).getString("team_code"));

                        faqList.add(registeredModel);

                    }

            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
            }


        }

        if (adapter.getItemCount()==0)
            emptyTV.setVisibility(View.VISIBLE);

        adapter.notifyDataSetChanged();




    }




}
