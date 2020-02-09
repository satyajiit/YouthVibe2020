package com.dyc.youthvibe.vibeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dyc.youthvibe.R;
import com.dyc.youthvibe.activity.EventsActivity;
import com.dyc.youthvibe.activity.InformalsActivity;
import com.dyc.youthvibe.activity.WorkshopsActivity;
import com.dyc.youthvibe.utils.PopUtil;

public class vibeActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout events_lin;
    //LinearLayout scl_icrd, informals_crd;
    View events_call;
    //ScrollView cntct_view_original;




    CardView crd1;
   // CardView crd3,crd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibe);

        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpListeners();

    }


    void setUpListeners(){


events_lin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startEventsActivity();
    }
});


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

    void initUI(){


        toolbar = findViewById(R.id.toolbar);
        events_lin = findViewById(R.id.events_lin);



    }

    public void startEventsActivity() {

        startActivity(new Intent(this, EventsActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


public void startWorkshops(View view){

    startActivity(new Intent(this, WorkshopsActivity.class));
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

}


    public void startInformals(View view) {

        startActivity(new Intent(this, InformalsActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


    public void startSocialAct(View view) {

        new PopUtil(this,3).show();


    }



}
