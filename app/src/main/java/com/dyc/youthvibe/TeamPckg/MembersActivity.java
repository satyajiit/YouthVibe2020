package com.dyc.youthvibe.TeamPckg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dyc.youthvibe.GetterSetter.MemberModels;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.MembersAdapter;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.utils.RandomColor;
import com.dyc.youthvibe.utils.jsonLoader;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MembersActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView listRecycler;
    private List<MemberModels> membersList = new ArrayList<>();
    private MembersAdapter mAdapter;
    String type;

    RandomColor rc = new RandomColor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        type = getIntent().getStringExtra("type");

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
    public void finish() {

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
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

    void setListRecycler() {

        mAdapter = new MembersAdapter(membersList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setHasStableIds(true);
        listRecycler.setAdapter(mAdapter);

        prepareData();

    }

    void initUI(){


        toolbar = findViewById(R.id.toolbar);
        listRecycler = findViewById(R.id.membersRecycler);


    }


    void prepareData(){


        JSONObject jsonObject;



        try {

            jsonObject = new jsonLoader(this, "MembersData.json").loadJSONFromAsset().getJSONObject(type);


            MemberModels list;




            Iterator<String> iter = jsonObject.keys();

            while (iter.hasNext()) {


                String key = iter.next();

                JSONObject js = jsonObject.getJSONObject(key);


                list = new MemberModels(key,js.getString("des")+"@"+type,js.getString("img"),js.getString("url"),rc.getColor());

                membersList.add(list);

            }




        }
        catch (Exception e){

            Log.d("GOPED",e.getMessage());


        }


        mAdapter.notifyDataSetChanged();



    }


}
