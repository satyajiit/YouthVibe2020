package com.dyc.youthvibe.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.graphics.PorterDuff;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyc.youthvibe.BuildConfig;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.activity.LoginActivity;
import com.dyc.youthvibe.activity.MainActivity;
import com.dyc.youthvibe.activity.OwnBlogsActivity;
import com.dyc.youthvibe.activity.RegisteredEventsActivity;
import com.dyc.youthvibe.utils.jsonLoader;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Stack;

import me.ydcool.lib.qrmodule.encoding.QrGenerator;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    TextView bugClick, logout, mobileTV, dobTV, fatherTV, mailTV, name_user, cllg_user, yv_id, soloTV, groupTV, workshopsTV,
            daysTV, amountTV, durationTV;

    LinearLayout accommodationLin;

    ImageView qrImage;

    LinearLayout clicker2, clicker1, clicker3;

    String mobileUser, username, uid;

    JSONObject jsonObject;

    Button ownPosts;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        initUI(v);

        Bitmap qrCode = null;



        try {
             qrCode = new QrGenerator.Builder()
                    .content("http://youthvibe.lpu.in/qr-code/"+ jsonObject.getString("qrhash"))
                    .qrSize(300)
                    .margin(2)
                    .color(Color.BLACK)
                    .bgColor(Color.WHITE)
                    .ecc(ErrorCorrectionLevel.H)
                    .overlay(getContext(),R.drawable.icon_small)
                    .overlaySize(75)
                    .overlayAlpha(255)
                   .overlayXfermode(PorterDuff.Mode.SRC_ATOP)
                    .encode();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        qrImage.setImageBitmap(qrCode);


        fetchDetails();

        setUpListeners();

        setUpTextViews();

        return v;
    }

    void fetchDetails(){

        uid = getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA");
        mobileUser = getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("mobile","NA");
        username = getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("name","NA");

    }

    void initUI(View view){

        qrImage = view.findViewById(R.id.qrImage);
        bugClick = view.findViewById(R.id.bugClick);
        mobileTV = view.findViewById(R.id.mobileTV);
        dobTV = view.findViewById(R.id.dobTV);
        fatherTV = view.findViewById(R.id.fatherTV);
        mailTV = view.findViewById(R.id.mailTV);
        ownPosts = view.findViewById(R.id.ownPosts);
        logout = view.findViewById(R.id.logout);
        name_user = view.findViewById(R.id.name_user);
        cllg_user = view.findViewById(R.id.cllg_user);
        yv_id = view.findViewById(R.id.yv_id);
        clicker1 = view.findViewById(R.id.clicker1);
        clicker2 = view.findViewById(R.id.clicker2);
        clicker3 = view.findViewById(R.id.clicker3);
        soloTV = view.findViewById(R.id.soloTV);
        groupTV = view.findViewById(R.id.groupTV);
        workshopsTV = view.findViewById(R.id.workshopsTV);

        daysTV = view.findViewById(R.id.daysTV);
        durationTV = view.findViewById(R.id.durationTV);
        amountTV = view.findViewById(R.id.amountTV);

        accommodationLin = view.findViewById(R.id.accommodationLin);


        try {
            jsonObject = new jsonLoader(getActivity(), "profile.json").loadJSONFromAsset();
        } catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }

    }

void setUpListeners(){

    ownPosts.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            startActivity(new Intent(getActivity(), OwnBlogsActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");

                FirebaseMessaging.getInstance().unsubscribeFromTopic(getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NONE_TEST"));
                getActivity().getSharedPreferences("YV", MODE_PRIVATE).edit().remove("ID").apply();
                getActivity().getSharedPreferences("YV", MODE_PRIVATE).edit().remove("email").apply();
                getActivity().getSharedPreferences("YV", MODE_PRIVATE).edit().remove("pass").apply();
                getActivity().getSharedPreferences("YV", MODE_PRIVATE).edit().remove("lastSync").apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().finish();

            }
        });

        clicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (soloTV.getText().equals("0"))
                    Toast.makeText(getActivity(), "You have registered zero Solo events.", Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(getActivity(), RegisteredEventsActivity.class).putExtra("start", 1));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            }
        });

    clicker2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (groupTV.getText().equals("0"))
                Toast.makeText(getActivity(), "You have registered zero Group events.", Toast.LENGTH_SHORT).show();
            else {
                startActivity(new Intent(getActivity(), RegisteredEventsActivity.class).putExtra("start", 2));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            }
    });

    clicker3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (workshopsTV.getText().equals("0"))
                Toast.makeText(getActivity(), "You have registered zero  Workshops.", Toast.LENGTH_SHORT).show();
            else {
                startActivity(new Intent(getActivity(), RegisteredEventsActivity.class).putExtra("start", 3));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        }
    });



    bugClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bugMsgBody = "Bug Title : \n\nSteps to reproduce : \n\n\n\nDoes the bug has anything to do with the user privacy? \n\n[YES/NO]\n\n[Attach Some Screenshots if Possible]   \n" +
                        "\nSent by : "+username+" \nYV ID: "+uid+"\nMobile: "
                        +  mobileUser+  "\nVersion Name: "
                        + BuildConfig.VERSION_NAME
                        +"\n Version Code: "+BuildConfig.VERSION_CODE+
                        "\n Android OS: "+ Build.VERSION.RELEASE
                        +"\nDevice: "+Build.MANUFACTURER+" "+android.os.Build.MODEL;


                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("*/*");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {
                        "satyajiit0@gmail.com"
                });
                i.putExtra(Intent.EXTRA_SUBJECT, "Bug Report from YV Android App");
                i.putExtra(Intent.EXTRA_TEXT, bugMsgBody);



                try {
                    startActivity(createEmailOnlyChooserIntent(i, "Send via email"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "No email clients installed.", Toast.LENGTH_SHORT).show();
                }



            }
        });


}



    public Intent createEmailOnlyChooserIntent(Intent source,
                                               CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "satyajiit0@gmail.com", null));
        List<ResolveInfo> activities = getActivity().getPackageManager()
                .queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }

    void setUpTextViews(){

        try {

            name_user.setText(jsonObject.getString("name"));

            if (jsonObject.getString("college").contains("-"))
            cllg_user.setText(jsonObject.getString("college").toUpperCase().substring(0,jsonObject.getString("college").indexOf('-')).trim());
            else
                cllg_user.setText(jsonObject.getString("college").toUpperCase().trim());

            yv_id.setText(jsonObject.getString("yvnumber"));

            mobileTV.setText("Mobile: "+jsonObject.get("contact").toString());

            fatherTV.setText("Father's Name: "+jsonObject.getString("father"));

            mailTV.setText("Email: "+jsonObject.getString("email"));

            dobTV.setText("DOB: "+jsonObject.getString("DOB"));

            groupTV.setText(String.valueOf(jsonObject.getJSONArray("team_events").length()));

            soloTV.setText(String.valueOf(jsonObject.getJSONArray("solo_events").length()));

            workshopsTV.setText(String.valueOf(jsonObject.getJSONArray("workshops").length()));


            if (!jsonObject.isNull("accomodation")) {
                daysTV.setText("Days Registered: " + jsonObject.getJSONObject("accomodation").getString("days"));
                amountTV.setText("Amount Paid: Rs" + jsonObject.getJSONObject("accomodation").getInt("ammount"));
                durationTV.setText("Duration: " + jsonObject.getJSONObject("accomodation").getString("from") + " - " + jsonObject.getJSONObject("accomodation").getString("to"));
            }

            else accommodationLin.setVisibility(View.GONE);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}




