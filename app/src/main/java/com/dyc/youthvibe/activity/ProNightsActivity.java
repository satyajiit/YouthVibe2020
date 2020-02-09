package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dyc.youthvibe.GetterSetter.WorkShopsModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.WorkshopsAdapter;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.utils.jsonLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProNightsActivity extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView listRecycler;
    private List<WorkShopsModel> workShopsModelList = new ArrayList<>();
    private WorkshopsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops);

        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListRecycler();

    }


    @Override
    public void finish() {

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);


        return true;
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


        toolbar = findViewById(R.id.toolbar);
        listRecycler = findViewById(R.id.listRecycler);


    }

    void setListRecycler(){

        mAdapter = new WorkshopsAdapter(workShopsModelList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        listRecycler.setAdapter(mAdapter);

        prepareData();


    }


    private void prepareData() {




        try {

            JSONObject jsonObject  = new jsonLoader(this, "EventsList.json").loadJSONFromAsset().getJSONObject("PRO_NIGHTS");




            WorkShopsModel list;

            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {


                    JSONObject temp =  jsonObject.getJSONObject(key);
                    list = new WorkShopsModel(key,
                            temp.getString("date"),
                            temp.getString("time"),
                            temp.getString("venue"),
                            "NA",
                            "NA",
                            temp.getString("link"));

                    workShopsModelList.add(list);

                } catch (JSONException e) {
                    // Something went wrong!
                }
            }



        }
        catch (Exception e){

            //Show error dialog here

        }








        mAdapter.notifyDataSetChanged();
    }



}