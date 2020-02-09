package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dyc.youthvibe.GetterSetter.MemberModels;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.SponsorsAdapter;
import com.dyc.youthvibe.utils.PopUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SponsorsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView sponsorsRecycler;
    private List<MemberModels> sponsorsList = new ArrayList<>();
    private SponsorsAdapter mAdapter;
    ScrollView shimmer_view_container;

    TextView sponsorPageTv;

    LottieAnimationView animationLottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);

        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setListRecycler();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);


        return true;
    }


    @Override
    public void finish() {

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {

            // notify.startAnimation(anim);
            new PopUtil(this, 1).show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish(); //Terminate the current Activity
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    void initUI() {

        sponsorPageTv = findViewById(R.id.sponsorPageTv);
        animationLottie = findViewById(R.id.animationLottie);
        toolbar = findViewById(R.id.toolbar);
        sponsorsRecycler = findViewById(R.id.sponsorsRecycler);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        sponsorPageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callSponsorTeam();

            }
        });


    }

    void setListRecycler() {

        mAdapter = new SponsorsAdapter(sponsorsList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sponsorsRecycler.setLayoutManager(mLayoutManager);
        sponsorsRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setHasStableIds(true);
        sponsorsRecycler.setAdapter(mAdapter);


        prepareData();

    }


    void prepareData(){


        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://youthvibe-2dd0f.firebaseapp.com/sponsors/data.json",
                        null,
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        MemberModels list;


        try {


                                 Iterator<String> iter = response.keys();

                                  while (iter.hasNext()) {


                            String key = iter.next();

                                      JSONObject js = response.getJSONObject(key);


                          list = new MemberModels(key, js.getString("type"), "", js.getString("link"), 0);

                                        sponsorsList.add(list);

                                             }

            mAdapter.notifyDataSetChanged();
                                  shimmer_view_container.setVisibility(View.GONE);
                                  if (mAdapter.getItemCount()==0){

                                      fallback();
                                  }

                        }

        catch (Exception e) {


                    Log.d("LOGGEd",e.getMessage());

                    fallback();


        }

                }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        fallback();

                    }
                });


        queue.add(jsonObjectRequest);


    }



    private void fallback() {

    sponsorPageTv.setVisibility(View.VISIBLE);
    animationLottie.setVisibility(View.VISIBLE);
        shimmer_view_container.setVisibility(View.GONE);


    }


    public void callSponsorTeam() {


        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:7355295373"));
        startActivity(callIntent);


    }




}