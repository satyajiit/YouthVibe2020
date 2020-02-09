package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dyc.youthvibe.GetterSetter.GetterSetterEnq;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.enqAdapter;
import com.dyc.youthvibe.utils.PopUtil;

import java.util.ArrayList;
import java.util.List;

public class EnquiryActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView listRecycler;
    private List<GetterSetterEnq> nameList = new ArrayList<>();
    private enqAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

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
            new PopUtil(this,1).show();

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

    void setListRecycler(){

        mAdapter = new enqAdapter(nameList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        listRecycler.setAdapter(mAdapter);

        prepareData();


    }

    private void prepareData() {

        GetterSetterEnq list = new GetterSetterEnq("Aman Kumar", "Event conveners", "8559048841");
        nameList.add(list);

        list = new GetterSetterEnq("Anupam Adarsh", "Event Co-Convener", "9915742902");
        nameList.add(list);

        list = new GetterSetterEnq("Piyush Kumar", "Registration Helpline", "8709483359");
        nameList.add(list);

        list = new GetterSetterEnq("Somu Barua", "Off-campus Relations Coordinator", "9575149766");
        nameList.add(list);

        list = new GetterSetterEnq("Kunwar Shailjakant", "Sponsorship & Marketing Cell", "7355295373");
        nameList.add(list);


        mAdapter.notifyDataSetChanged();
    }


    void initUI(){


        toolbar = findViewById(R.id.toolbar);
        listRecycler = findViewById(R.id.listRecycler);

    }


}
