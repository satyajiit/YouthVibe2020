package com.dyc.youthvibe.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.TabAdapter;
import com.dyc.youthvibe.fragments.DaysScheduleFragments.DayFourFragment;
import com.dyc.youthvibe.fragments.DaysScheduleFragments.DayOneFragment;
import com.dyc.youthvibe.fragments.DaysScheduleFragments.DayThreeFragment;
import com.dyc.youthvibe.fragments.DaysScheduleFragments.DayTwoFragment;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter tabAdapter;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        initUI(view);


        tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager());
        tabAdapter.addFragment(new DayOneFragment(), "FEB 13");
        tabAdapter.addFragment(new DayTwoFragment(), "FEB 14");
        tabAdapter.addFragment(new DayThreeFragment(), "FEB 15");
        tabAdapter.addFragment(new DayFourFragment(), "FEB 16");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

   //     initPager();


        return view;
    }


//    void initPager(){
//
//
//        highLightCurrentTab(0);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//            @Override
//            public void onPageSelected(int position) {
//                highLightCurrentTab(position);
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });


//    }

//
//
//
//    private void highLightCurrentTab(int position) {
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            assert tab != null;
//            tab.setCustomView(null);
//           // tab.setCustomView(tabAdapter.getTabView(i));
//        }
//        TabLayout.Tab tab = tabLayout.getTabAt(position);
//        assert tab != null;
//        tab.setCustomView(null);
//        tab.setCustomView(tabAdapter.getSelectedTabView(position));
//    }


    void initUI(View view){

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);


    }
}
