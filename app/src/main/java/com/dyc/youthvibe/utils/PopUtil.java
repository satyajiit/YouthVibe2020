package com.dyc.youthvibe.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dyc.youthvibe.R;
import com.dyc.youthvibe.activity.MainActivity;
import com.dyc.youthvibe.activity.RegisterActivity;


public class PopUtil {

    private Context context;
    int type;
    String ver, url;


    public PopUtil(Context context, int type){

        this.context = context;
        this.type = type;


    }

    public PopUtil(Context context, int type, String ver, String url){


        this(context, type);
        this.ver = ver;
        this.url = url;

    }



    public void show(){

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (type == 1) {

            dialog.setContentView(R.layout.help_pop);
            Button callBtnPop = dialog.findViewById(R.id.callBtnPop);
            callBtnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:8559048841"));
                    context.startActivity(callIntent);
                    dialog.dismiss();

                }
            });

        }

        else if (type == 2) { //Register Clicked

            dialog.setContentView(R.layout.sign_up_pop);
            Button callBtnPop = dialog.findViewById(R.id.register);
            Button callBtnPop2 = dialog.findViewById(R.id.inApp);
            callBtnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://youthvibe.lpu.in/accounts"));
                    context.startActivity(browserIntent);
                    dialog.dismiss();

                }
            });


            callBtnPop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    context.startActivity(new Intent(context, RegisterActivity.class));
                    dialog.dismiss();

                }
            });


        }

        else if (type == 3) { //Coming Soon

            dialog.setContentView(R.layout.comin_soon_pop);
            Button callBtnPop = dialog.findViewById(R.id.ok);
            callBtnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();

                }
            });

        }

        else if (type == 4) { //Exit Confirm

            dialog.setContentView(R.layout.exit_confirm_pop);
            Button callBtnPop = dialog.findViewById(R.id.yes);
            Button callBtnPop2 = dialog.findViewById(R.id.no);
            //Button cancel = dialog.findViewById(R.id.no);

            callBtnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    ((AppCompatActivity)context).finish();

                }
            });

            callBtnPop2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();

                }
            });




        }
        else if (type == 5) { //No Internet

            dialog.setContentView(R.layout.no_internet_pop);
            Button callBtnPop = dialog.findViewById(R.id.ok);
            Button settings = dialog.findViewById(R.id.settings);

            dialog.setCanceledOnTouchOutside(false);

            dialog.setCancelable(false);

            callBtnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((AppCompatActivity)context).finish();

                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));




                }
            });

        }

        else if (type == 6) { //Welcome user

            dialog.setContentView(R.layout.new_update_pop);



            TextView tv = dialog.findViewById(R.id.text);
            TextView texted = dialog.findViewById(R.id.texted);

            texted.setText("Welcome");
            tv.setText("Hi , "+context.getSharedPreferences("YV",Context.MODE_PRIVATE).getString("name","user")
                    +"\nWelcome to the App.\nDon't forget to rate us on PlayStore!");
//
         final LinearLayout cBox = dialog.findViewById(R.id.layout);
         cBox.setVisibility(View.GONE);

            Button btn = dialog.findViewById(R.id.update);

            btn.setText("OK");

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    dialog.dismiss();


                }
            });


        }


        else if (type == 10) { //New Update

            dialog.setContentView(R.layout.new_update_pop);

            TextView tv = dialog.findViewById(R.id.text);

            tv.setText(context.getString(R.string.new_update));
            final LinearLayout cBox = dialog.findViewById(R.id.layout);
            cBox.setVisibility(View.GONE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            Button btn = dialog.findViewById(R.id.update);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);


                }
            });


        }


            dialog.show();


    }




}
