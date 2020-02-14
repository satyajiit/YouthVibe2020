package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dyc.youthvibe.FaqFragments.RegisteredEventsSingleFragment;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.TabAdapter;
import com.dyc.youthvibe.utils.PopUtil;
import com.google.android.material.tabs.TabLayout;

public class RegisteredEventsActivity extends AppCompatActivity {


    Toolbar toolbar;
    //  BottomNavigationView navigation;

    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter tabAdapter;

    TextView emptyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_events);


        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
            new PopUtil(this,1).show();

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

    void initUI(){

        //  navigation =  findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);




        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new RegisteredEventsSingleFragment("solo_events"), "SOLO EVENTS");
        tabAdapter.addFragment(new RegisteredEventsSingleFragment("team_events"), "GROUP EVENTS");
        tabAdapter.addFragment(new RegisteredEventsSingleFragment("workshops"), "WORKSHOPS");
        viewPager.setAdapter(tabAdapter);


        viewPager.setCurrentItem(getIntent().getIntExtra("start",1)-1);


        tabLayout.setupWithViewPager(viewPager);


    }






}
