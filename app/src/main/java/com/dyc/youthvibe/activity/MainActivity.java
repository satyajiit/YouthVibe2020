package com.dyc.youthvibe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.fragments.BlogsFragment;
import com.dyc.youthvibe.fragments.HomeFragment;
import com.dyc.youthvibe.fragments.ProfileFragment;
import com.dyc.youthvibe.fragments.ScheduleFragment;
import com.dyc.youthvibe.utils.PopUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    FloatingActionButton fab;

    Dialog dialog;

    Fragment fragment = null;
    BottomNavigationView navigation;

    SharedPreferences sharedPreferences;

    int mCartItemCount = 10;

    TextView textCartItemCount;

    SharedPreferences.OnSharedPreferenceChangeListener listener;


    Toolbar toolbar ;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {




            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = HomeFragment.newInstance();

                    break;
                case R.id.navigation_sch:
                    fragment = ScheduleFragment.newInstance();

                    break;
                case R.id.navigation_profile:
                    fragment = ProfileFragment.newInstance();

                    break;

                case R.id.navigation_blogs:
                    fragment = BlogsFragment.newInstance();

                    break;

            }

            if (fragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }


            return true;

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        if (Build.VERSION.SDK_INT > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            new PopUtil(this, 4).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();


        setSupportActionBar(toolbar);



        //Set the main fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, HomeFragment.newInstance()); //Set fragment as default
        fragmentTransaction.commit();


        if (getIntent()!=null){

            if (getIntent().getBooleanExtra("fromLogin",false))
                new PopUtil(this,6).show();

        }



        setUpListeners();

        checkConnection();

        if (getSharedPreferences("YV",MODE_PRIVATE).getLong("lastSync",0)+1800000 < System.currentTimeMillis()&& !getIntent().getBooleanExtra("fromLogin", false)){

            dialog.show();
            fetchID();

        }


    }


    void fetchID(){




        final String email = getSharedPreferences("YV",MODE_PRIVATE).getString("email","0").trim();
        final String pass = getSharedPreferences("YV",MODE_PRIVATE).getString("pass","0").trim();

            RequestQueue queue = Volley.newRequestQueue(this);




            String url = "YOUR_API"; //Sync user profile
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response

                            if (response.equals("failed")){

                                loginFail();

                            }
                            else {

                                getSharedPreferences("YV",MODE_PRIVATE).edit().putLong("lastSync",System.currentTimeMillis()).apply();


                                dialog.dismiss();


                                try (FileOutputStream fos = openFileOutput("profile.json", MODE_PRIVATE)) {
                                    fos.write(response.getBytes());
                                } catch (IOException e) {

                                    e.printStackTrace();
                                    loginFail();

                                }






                            }

                            Log.d("Response", response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.getMessage());
                            loginFail();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("password", pass);
                    params.put("email", email);

                    return params;
                }
            };
            postRequest.setShouldCache(false);
            queue.add(postRequest);

        }






    private void loginFail() {

        Toast.makeText(this, "Security Error!", Toast.LENGTH_SHORT).show();
        getSharedPreferences("YV",MODE_PRIVATE).edit().remove("email").apply();
        getSharedPreferences("YV",MODE_PRIVATE).edit().remove("pass").apply();
        getSharedPreferences("YV",MODE_PRIVATE).edit().remove("ID").apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NONE_TEST"));

        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }


    void initUI() {

        navigation =  findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);


         dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.sync_pop);



    }

    private void checkConnection() {

        if(!isConnected()) {
            //show a No Internet Alert or Dialog

            new PopUtil(this,5).show();

        }
    }

    public boolean isConnected()  {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.notify);

        //View actionView = MenuItemCompat.getActionView(menuItem);
        View actionView = menuItem.getActionView();
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);

        readFromPref();

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    void readFromPref(){

        sharedPreferences = getSharedPreferences("YV",MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notify) {

           startActivity(new Intent(this, NotificationsActivity.class));

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            return true;

        }



        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {


        mCartItemCount = getSharedPreferences("YV",MODE_PRIVATE).getInt("notSeen",0);

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    void setUpListeners(){


        //Btm Nav Listener
        navigation.setSelectedItemId(R.id.navigation_home); //sets Library as the default item
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener); //Set the Click Listener for Nav Bar

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // Implementation
                Log.d("LOGGED",key);
                setupBadge();
            }
        };


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });

    }

}
