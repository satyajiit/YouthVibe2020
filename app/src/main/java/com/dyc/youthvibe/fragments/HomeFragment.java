package com.dyc.youthvibe.fragments;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dyc.youthvibe.R;
import com.dyc.youthvibe.TeamPckg.TeamActivity;
import com.dyc.youthvibe.activity.EnquiryActivity;
import com.dyc.youthvibe.activity.FaqActivity;
import com.dyc.youthvibe.activity.MapActivity;
import com.dyc.youthvibe.activity.SponsorsActivity;
import com.dyc.youthvibe.vibeActivity.vibeActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //private ImageView fb_opener, ig_opener, yt_opener, link_opener;

    private LinearLayout vibeCrd, crdEnq, faq_lin, teamLinearClicker, mapLinearClicker, sponsorsCrd;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initUI(v);

        setClickListeners();

        return v;



    }

    void initUI(View v){

        vibeCrd = v.findViewById(R.id.vibeCrd);
        crdEnq = v.findViewById(R.id.crdEnq);
        faq_lin = v.findViewById(R.id.faq_lin);
        teamLinearClicker = v.findViewById(R.id.teamLinearClicker);
        mapLinearClicker = v.findViewById(R.id.mapLinearClicker);
        sponsorsCrd = v.findViewById(R.id.sponsorsCrd);

    }

    void startIntent(Class name){

        startActivity(new Intent(getActivity(),name));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    void setClickListeners(){

        //Card Listeners

        mapLinearClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */

                        startIntent(MapActivity.class);

                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {

                        Toast.makeText(getActivity(), "Permiission Error", Toast.LENGTH_SHORT).show();

                    }
                }).check();



            }
        });

        teamLinearClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(TeamActivity.class);
            }
        });

        vibeCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startIntent(vibeActivity.class);

            }
        });

        faq_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          startIntent(FaqActivity.class);

//                Fragment someFragment = new faqFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_layout, someFragment ); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();

            }
        });

        crdEnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(EnquiryActivity.class);
            }
        });

        sponsorsCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(SponsorsActivity.class);
            }
        });


//
//        fb_opener.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startBrowserIntent("https://www.facebook.com/YouthVibe");
//            }
//        });
//
//
//
//        yt_opener.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startBrowserIntent("https://www.youtube.com/user/YouthvibeLpu");
//            }
//        });
//
//
//        link_opener.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startBrowserIntent("https://www.linkedin.com/company/youthvibelpu");
//            }
//        });
//
//
//        ig_opener.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startBrowserIntent("https://www.instagram.com/youthvibe/");
//            }
//        });

    }


    void startBrowserIntent(String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);


    }


}
