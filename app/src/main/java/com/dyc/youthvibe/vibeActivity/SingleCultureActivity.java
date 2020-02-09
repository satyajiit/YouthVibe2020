package com.dyc.youthvibe.vibeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dyc.youthvibe.GetterSetter.EventNamesModel;
import com.dyc.youthvibe.GetterSetter.GetterSetterEnq;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.EventNamesAdapter;
import com.dyc.youthvibe.adapters.enqAdapter;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.utils.jsonLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SingleCultureActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView listRecycler;
    private List<EventNamesModel> eventsNameList = new ArrayList<>();
    private EventNamesAdapter mAdapter;


    String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);



        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadCategoryNameFromIntent();

        setListRecycler();


    }

    void loadCategoryNameFromIntent(){

        cat = getIntent().getStringExtra("cat");

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

        mAdapter = new EventNamesAdapter(eventsNameList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        listRecycler.setAdapter(mAdapter);

        prepareData();


    }

    void initUI() {


        toolbar = findViewById(R.id.toolbar);
        listRecycler = findViewById(R.id.listRecycler);


    }




    private void prepareData() {




        try {

            JSONArray array  = new jsonLoader(this, "EventsList.json").returnJSONArray(cat);



            EventNamesModel list;

            for (int i=0; i<array.length(); i++){


                list = new EventNamesModel(array.get(i).toString().toUpperCase());
                eventsNameList.add(list);


            }


        }
        catch (Exception e){

            //Show error dialog here

        }








        mAdapter.notifyDataSetChanged();
    }


}
