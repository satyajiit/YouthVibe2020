package com.dyc.youthvibe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dyc.youthvibe.BuildConfig;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.utils.PopUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONObject;

import java.io.File;

public class SplashActivity extends AppCompatActivity {



    LottieAnimationView load;

    TextView tvLoader;

    String[] links = {"MembersData.json","EventsList.json","RuleBooksData.json","EventsDataSchedule.json","mapData.json"};

    int filesDownloaded = 0;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvLoader = findViewById(R.id.tvLoader);
        load = findViewById(R.id.load);




        if(!isConnected()) {
            //show a No Internet Alert or Dialog

            new PopUtil(this,5).show();

        }

        else
                checkVersion();



        registerToUserSpecificTopic();


    }



    private void processLoggedUser() {


//        if (  1==1||getSharedPreferences("YV",MODE_PRIVATE).getLong("lastSync",0)+1800000 < System.currentTimeMillis() )
//            callLoginRequestAPI();
       // else {

            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

      //  }

    }

    void registerToUserSpecificTopic(){

        FirebaseMessaging.getInstance().subscribeToTopic(getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NONE_TEST"));

    }



    void fileDownloader(String name){



        StorageReference  islandRef = storageRef.child("data/"+name);

        File file = new File(this.getFilesDir(), name);

        islandRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                filesDownloaded++;

                if (filesDownloaded <= 4) {
                    fileDownloader(links[filesDownloaded]);
                    tvLoader.setText("Updated "+(filesDownloaded+1)+"/"+5+" files...");

                }
                else {

                    tvLoader.setText("Starting Up...");
                    lunchIntent();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }


//    private void processAfterDownload() {
//
//
//        getSharedPreferences("YV",MODE_PRIVATE).edit().putLong("lastSync",System.currentTimeMillis()).apply();
//
//        lunchIntent();
//
//    Log.d("LOGGED","CONGRATS");
//
//    }


    void lunchIntent(){


        tvLoader.setText("Starting Up...");

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Main-Activity. */

              //  if (isLoggedUser())
               // Intent mainIntent;

                if (isLoggedUserCheckFromSharedPrefs())
                    processLoggedUser();
                else
                     startActivity(new Intent(getApplicationContext(),LoginActivity.class));


                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, 900);

    }



    void checkVersion(){


        final Context context = this;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://your_url_for_update/update.json", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            Log.d("TESTED","TESTED");

                                JSONObject js = response.getJSONObject("update");

                                if (js.getInt("version")>(BuildConfig.VERSION_CODE))
                                    new PopUtil(context,10, js.getString("version"), js.getString("url")).show();
                               else if (js.getInt("code") > getSharedPreferences("YV",MODE_PRIVATE).getInt("code",0)) {

                                    getSharedPreferences("YV",MODE_PRIVATE).edit().putInt("code",js.getInt("code")).apply();
                                    tvLoader.setText("Initiating Update...");

                                    fileDownloader(links[0]);
                                    load.setVisibility(View.VISIBLE);
                                    tvLoader.setVisibility(View.VISIBLE);
                                }
                                else
                                    lunchIntent();



                        }
                        catch (Exception e){

                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });


      jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);





    }




    boolean isLoggedUserCheckFromSharedPrefs(){


       return  getSharedPreferences("YV",MODE_PRIVATE).getString("ID","XX").length()>10;


    }


    public boolean isConnected()  {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



}
