package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.utils.PopUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar ;
    TextInputEditText editTextEmail, editTextPassword;
    Button cirLoginButton;
    Typeface font;
    ImageView lv2;
    RelativeLayout lay;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBar;
    int errorCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);



        initUI();


        setSupportActionBar(toolbar);


        animate(lv2, new int[]{R.drawable.ic_girl_login, R.drawable.ic_login_page_icon}, 0,true);







    }


    private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {

        //imageView <-- The View which displays the images
        //images[] <-- Holds R references to the images to display
        //imageIndex <-- index of the first image to show in images[]
        //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

        int fadeInDuration = 500; // Configure time values here
        int timeBetween = 2500;
        int fadeOutDuration = 1000;

        imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
        imageView.setImageResource(images[imageIndex]);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        imageView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (images.length - 1 > imageIndex) {
                    animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
                }
                else {
                    if (forever){
                        animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
                    }
                }
            }
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
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

    void initUI() {


        toolbar = findViewById(R.id.toolbar);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        cirLoginButton = findViewById(R.id.cirLoginButton);
         font = Typeface.createFromAsset(getAssets(), "cav.ttf");
        lay = findViewById(R.id.lay);
        lv2 = findViewById(R.id.lv2);
        progressBar = findViewById(R.id.progressBar);


    }

    public void startLoginValidation(View view) {

//            if (editTextEmail.getText().toString().contains("test")&&editTextPassword.getText().toString().contains("test"))
//                startHomeActivity();
            
            if (isValidEmail(editTextEmail.getText())&&isValidPass(editTextPassword.getText().toString()))
                    callLoginRequestAPI();
            else
                setSnackBar("Invalid Data Entered.");
            

    }

    public void viewRegisterClicked(View view) {

        new PopUtil(this,2) .show();

    }

    private void callLoginRequestAPI() {

        cirLoginButton.setText("");
        cirLoginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(this);



        String url = "YOUR API";
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

                            try {

                                Log.d("TESTED",new JSONObject(response)
                                        .getString("yvnumber"));

                                getSharedPreferences("YV",MODE_PRIVATE).edit().putString("ID",new JSONObject(response)
                                        .getString("yvnumber")).apply();
                                getSharedPreferences("YV",MODE_PRIVATE).edit().putString("name",new JSONObject(response)
                                        .getString("name")).apply();

                                getSharedPreferences("YV",MODE_PRIVATE).edit().putString("cllg",new JSONObject(response)
                                        .getString("college")).apply();

                                getSharedPreferences("YV",MODE_PRIVATE).edit().putString("mobile",new JSONObject(response)
                                        .getString("contact")).apply();

                                getSharedPreferences("YV",MODE_PRIVATE).edit().putString("email",editTextEmail.getText().toString()).apply();


                                getSharedPreferences("YV",MODE_PRIVATE).edit().putString("pass",editTextPassword.getText().toString()).apply();

                            } catch (JSONException e) {
                                errorCode = 1;
                                e.printStackTrace();
                                setSnackBar("Database Error 405");
                            }

                            try (FileOutputStream fos = openFileOutput("profile.json", MODE_PRIVATE)) {
                                fos.write(response.getBytes());
                            } catch (IOException e) {
                                errorCode = 1;
                                e.printStackTrace();
                                setSnackBar("Write Error 406");
                            }


                            if (errorCode != 1)
                                    startHomeActivity();
                            else  setSnackBar("Error CODE 402");

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
                        setSnackBar("Connection Error CODE 401");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("password", editTextPassword.getText().toString());
                params.put("email", editTextEmail.getText().toString());

                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.add(postRequest);

    }

    private void loginFail() {

        setSnackBar("Incorrect Credentials.");
        progressBar.setVisibility(View.GONE);
        cirLoginButton.setEnabled(true);
        cirLoginButton.setText("LOGIN");

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    
    public static boolean isValidPass(String pass){
        return pass.length() >= 2;
    }


    void startHomeActivity(){


        FirebaseMessaging.getInstance().subscribeToTopic("all"); //Registered for topic


        startActivity(new Intent(this,MainActivity.class).putExtra("fromLogin",true));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }

    public  void setSnackBar(String snackTitle) {
        Snackbar snackbar = Snackbar.make(lay, snackTitle, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Your Action
            }
        });
        snackbar.show();
        View view = snackbar.getView();
        view.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        TextView txtv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        TextView tv2 = view.findViewById(com.google.android.material.R.id.snackbar_action);
        txtv.setTypeface(font);
        tv2.setTypeface(font);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        setSnackBar("Please click BACK again to exit");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void startForgotPassword(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://youthvibe.lpu.in/forgot-password"));
        startActivity(browserIntent);


    }





}
