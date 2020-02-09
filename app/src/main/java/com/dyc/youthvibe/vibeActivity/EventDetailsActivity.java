package com.dyc.youthvibe.vibeActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dyc.youthvibe.GetterSetter.EventNamesModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.activity.MapActivity;
import com.dyc.youthvibe.adapters.singleItemAdapter;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.utils.jsonLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    String eventName;
    TextView eventTV, teamSizeTV, firstPrizeTV, secondPrizeTV, typeTV, roundsTV, noteTV, timeTV, eventDataTV, locTV, themeTV, locTV2;
    ImageView eventImage;
    CardView noteCard, themeCard;
    String LatLng, Time, Date, Loc;



    HorizontalScrollView layer2;

    ProgressBar progressBar;

    CardView locCard2;

    View desing_layout, specification_layout, judgeLayout, extra_layout;

    RecyclerView rulesRecycler,guidelinesRecycler, criteriaRecycler, specificationRecycler, designRecycler, extraDetailsRecycler;
    private List<EventNamesModel> rulesList = new ArrayList<>();
    private List<EventNamesModel> guidesList = new ArrayList<>();
    private List<EventNamesModel> criteriaList = new ArrayList<>();
    private List<EventNamesModel> designList = new ArrayList<>();
    private List<EventNamesModel> extraList = new ArrayList<>();
    private List<EventNamesModel> specificationList = new ArrayList<>();
    private singleItemAdapter rulesAdapter, guidesADapter, criteriaAdapter, designAdapter, specificationAdapter,extraAdapter;


    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getEventNameFromIntent();

        loadNonAdapterViews();

        loadRules();

      loadGuidelines();


      loadCriteria();


        layer2 = findViewById(R.id.lay);


        new CountDownTimer(2000, 5) {

            public void onTick(long millisUntilFinished) {
                layer2.scrollTo((int) (2000 - millisUntilFinished), 0);
            }

            public void onFinish() {


                layer2.postDelayed(new Runnable() {
                    public void run() {
                        layer2.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                }, 100L);

            }
        }.start();




    }



    void getEventNameFromIntent(){

        eventName = getIntent().getStringExtra("eventName");

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

    public  String returnStringValue(String value) throws  Exception{

        return jsonObject.getJSONObject(eventName).get(value).toString();

    }

   void loadNonAdapterViews(){


        eventTV.setText(eventName);


       try {

           teamSizeTV.setText(returnStringValue("TeamSize"));
           firstPrizeTV.setText("₹"+returnStringValue("Prize1"));
           secondPrizeTV.setText("₹"+returnStringValue("Prize2"));
           roundsTV.setText(returnStringValue("Rounds"));
           typeTV.setText(returnStringValue("Type").toUpperCase());

           Time = returnStringValue("TIME");




           Date = returnStringValue("Date");



           LatLng = returnStringValue("LAT");

           Loc = returnStringValue("LOC");


           locTV.setText(Html.fromHtml("<font color='#D32F2F'>Venue: </font> "+Loc, Html.FROM_HTML_MODE_COMPACT));


           eventDataTV.setText(Date);

            if (jsonObject.getJSONObject(eventName).has("Theme")) {

                themeCard.setVisibility(View.VISIBLE);
                String temp = jsonObject.getJSONObject(eventName).get("Theme").toString();

                themeTV.setText( Html.fromHtml("<font color='#2E7D32'>Theme: </font> "+temp, Html.FROM_HTML_MODE_COMPACT));

            }

           if (jsonObject.getJSONObject(eventName).has("Specification"))
               loadSpecificationRules();

           if (jsonObject.getJSONObject(eventName).has("Design"))
               loadDesignRules();

           if (jsonObject.getJSONObject(eventName).has("Extra"))
               loadExtraRules();

           timeTV.setText(Time);

           if (jsonObject.getJSONObject(eventName).has("LOC2")){

               locCard2.setVisibility(View.VISIBLE);

               locTV2.setText(Html.fromHtml("<font color='#D32F2F'>Final Venue: </font> "+jsonObject.getJSONObject(eventName).get("LOC2").toString(), Html.FROM_HTML_MODE_COMPACT));


           }


           if (!returnStringValue("Note").equals("NA")){

               noteTV.setVisibility(View.VISIBLE);
               noteCard.setVisibility(View.VISIBLE);


               noteTV.setText( Html.fromHtml("<font color='#1E88E5'>Note: </font> "+returnStringValue("Note").replace("\n","<br>"), Html.FROM_HTML_MODE_COMPACT));


           }





       } catch (Exception e) {
           e.printStackTrace();
       }

       Picasso.get().
               load("https://youthvibe-2dd0f.web.app/details_page/"+eventName.replace(":","").replace("/","")+".jpg")
               .into(eventImage, new Callback() {
                   @Override
                   public void onSuccess() {

                       eventImage.setVisibility(View.VISIBLE);
                       progressBar.setVisibility(View.GONE);
                   }

                   @Override
                   public void onError(Exception e) {

                   }
               });






   }


    void initUI() {



        try {
            jsonObject = new jsonLoader(this, "RuleBooksData.json").loadJSONFromAsset();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.toolbar);
        eventImage = findViewById(R.id.eventImage);
        eventTV = findViewById(R.id.eventTV);
        rulesRecycler = findViewById(R.id.rulesRecycler);
        guidelinesRecycler = findViewById(R.id.guidelinesRecycler);
        criteriaRecycler =  findViewById(R.id.criteriaRecycler);

        noteCard =  findViewById(R.id.noteCard);

        noteTV = findViewById(R.id.noteTV);

        teamSizeTV = findViewById(R.id.teamSizeTV);
         firstPrizeTV = findViewById(R.id.firstPrizeTV);
          secondPrizeTV =  findViewById(R.id.secondPrizeTV);




        locTV =  findViewById(R.id.locTV);

        timeTV = findViewById(R.id.timeTV);

        themeCard = findViewById(R.id.themeCard);
        
        eventDataTV = findViewById(R.id.eventDataTV);

        themeTV = findViewById(R.id.themeTV);

            typeTV = findViewById(R.id.typeTV);
            roundsTV = findViewById(R.id.roundsTV);

            desing_layout = findViewById(R.id.desing_layout);
            specification_layout = findViewById(R.id.specification_layout);
            specificationRecycler = findViewById(R.id.specificationRecycler);
            designRecycler = findViewById(R.id.designRecycler);

        judgeLayout = findViewById(R.id.judgeLayout);

        progressBar = findViewById(R.id.progressBar);

        extra_layout= findViewById(R.id.extra_layout);

        extraDetailsRecycler = findViewById(R.id.extraDetailsRecycler);


        locCard2 = findViewById(R.id.locCard2);
        locTV2 = findViewById(R.id.locTV2);

    }


    void loadRules(){


        rulesAdapter = new singleItemAdapter(rulesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rulesRecycler.setLayoutManager(mLayoutManager);
        rulesRecycler.setItemAnimator(new DefaultItemAnimator());
        rulesRecycler.setAdapter(rulesAdapter);

            try {

                JSONArray array  = jsonObject.getJSONObject(eventName).getJSONArray("Rules");



                EventNamesModel list;

                for (int i=0; i<array.length(); i++){


                    list = new EventNamesModel(array.get(i).toString());
                    rulesList.add(list);


                }


            }
            catch (Exception e){

                //Show error dialog here

            }






            rulesAdapter.notifyDataSetChanged();
        }


    void loadGuidelines(){

        guidesADapter = new singleItemAdapter(guidesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        guidelinesRecycler.setLayoutManager(mLayoutManager);
        guidelinesRecycler.setItemAnimator(new DefaultItemAnimator());
        guidelinesRecycler.setAdapter(guidesADapter);



        try {

            JSONArray array  = jsonObject.getJSONObject(eventName).getJSONArray("Guides");



            EventNamesModel list;

            for (int i=0; i<array.length(); i++){


                list = new EventNamesModel(array.get(i).toString());
                guidesList.add(list);


            }


        }
        catch (Exception e){

            //Show error dialog here


        }








        guidesADapter.notifyDataSetChanged();


}


    void loadCriteria(){

        criteriaAdapter = new singleItemAdapter(criteriaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        criteriaRecycler.setLayoutManager(mLayoutManager);
        criteriaRecycler.setItemAnimator(new DefaultItemAnimator());
        criteriaRecycler.setAdapter(criteriaAdapter);

        try {

            if ( jsonObject.getJSONObject(eventName).has("Judge")) {

                JSONArray array = jsonObject.getJSONObject(eventName).getJSONArray("Judge");


                EventNamesModel list;

                for (int i = 0; i < array.length(); i++) {


                    list = new EventNamesModel(array.get(i).toString());
                    criteriaList.add(list);


                }

            }

            else judgeLayout.setVisibility(View.GONE);

        }
        catch (Exception e){

            judgeLayout.setVisibility(View.GONE);

        }








        criteriaAdapter.notifyDataSetChanged();


    }



    void loadDesignRules(){


        desing_layout.setVisibility(View.VISIBLE);
        designAdapter = new singleItemAdapter(designList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        designRecycler.setLayoutManager(mLayoutManager);
        designRecycler.setItemAnimator(new DefaultItemAnimator());
        designRecycler.setAdapter(designAdapter);



        try {

            JSONArray array  = jsonObject.getJSONObject(eventName).getJSONArray("Design");



            EventNamesModel list;

            for (int i=0; i<array.length(); i++){


                list = new EventNamesModel(array.get(i).toString());
                designList.add(list);


            }


        }
        catch (Exception e){

            //Show error dialog here


        }








        designAdapter.notifyDataSetChanged();


    }

    void loadSpecificationRules(){

        specification_layout.setVisibility(View.VISIBLE);
        specificationAdapter = new singleItemAdapter(specificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        specificationRecycler.setLayoutManager(mLayoutManager);
        specificationRecycler.setItemAnimator(new DefaultItemAnimator());
        specificationRecycler.setAdapter(specificationAdapter);



        try {

            JSONArray array  = jsonObject.getJSONObject(eventName).getJSONArray("Specification");



            EventNamesModel list;

            for (int i=0; i<array.length(); i++){


                list = new EventNamesModel(array.get(i).toString());
                specificationList.add(list);


            }


        }
        catch (Exception e){

            //Show error dialog here


        }








        specificationAdapter.notifyDataSetChanged();


    }

    void loadExtraRules(){

        extra_layout.setVisibility(View.VISIBLE);
        extraAdapter = new singleItemAdapter(extraList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        extraDetailsRecycler.setLayoutManager(mLayoutManager);
        extraDetailsRecycler.setItemAnimator(new DefaultItemAnimator());
        extraDetailsRecycler.setAdapter(extraAdapter);



        try {

            JSONArray array  = jsonObject.getJSONObject(eventName).getJSONArray("Extra");



            EventNamesModel list;

            for (int i=0; i<array.length(); i++){


                list = new EventNamesModel(array.get(i).toString());
                extraList.add(list);


            }


        }
        catch (Exception e){

            //Show error dialog here


        }








        extraAdapter.notifyDataSetChanged();


    }

    public void addToCalender(View view) {

        if (Time.equals("NA")){

            Time = "9:00 AM";

        }


        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");



        Calendar calendar = Calendar.getInstance();



        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);

        calendar.set(Calendar.HOUR, Integer.parseInt(Time.substring(0,Time.indexOf(':'))));



        if (Time.substring(Time.indexOf(' ')+1).equals("AM"))
        calendar.set(Calendar.AM_PM, Calendar.AM);
        else
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Date.substring(0,2)));
        calendar.set(Calendar.YEAR, 2019);

        long startTime = calendar.getTimeInMillis();

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

        intent.putExtra(CalendarContract.Events.TITLE, eventName);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventName+" - YouthVibe Event");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, Loc+" - Lovely Professional University");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=ONCE");

        startActivity(intent);

    }

    public void showOnMap(View view) {


        startActivity(new Intent(this, MapActivity.class).putExtra("LatLng",LatLng).putExtra("event",eventName).putExtra("loc",Loc));

    }
}


