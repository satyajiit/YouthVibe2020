package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dyc.youthvibe.R;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.vibeActivity.AestheticActivity;
import com.dyc.youthvibe.vibeActivity.AgastyaActivity;
import com.dyc.youthvibe.vibeActivity.SingleCultureActivity;

public class EventsActivity extends AppCompatActivity {


    Toolbar toolbar;
    Intent i;
    Button bck_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);

        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpListeners();



    }

    private void setUpListeners() {

        bck_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }


    public void startAestheticActivity(View view) {

        startActivity(new Intent(this, AestheticActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }



    void start(String cat) {

        i.putExtra("cat", cat);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


    public void startSocial(View view) {

        start("SOCIAL");

    }

    public void startSports(View view) {
        start("SPORTS");
    }

    public void startTech(View view) {


        startActivity(new Intent(this, AgastyaActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }


    public void startLiterary(View view) {


        start("LITERARY");


    }


    public void startMediaa(View view) {

        start("MEDIA");
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

    void initUI() {


        i = new Intent(this, SingleCultureActivity.class);
        toolbar = findViewById(R.id.toolbar);
        bck_btn = findViewById(R.id.bck_btn);


    }


}