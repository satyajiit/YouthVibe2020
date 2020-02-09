package com.dyc.youthvibe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.dyc.youthvibe.GetterSetter.MessagesModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.MessagesAdapter;
import com.dyc.youthvibe.utils.PopUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {


    Toolbar toolbar;
    MessagesModel list;
    RecyclerView msgRecycler;
    ScrollView shimmerLayout;
    private List<MessagesModel> messagesList = new ArrayList<>();
    private MessagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListRecycler();

        removePrefs();

    }

    void removePrefs(){

        getSharedPreferences("YV",MODE_PRIVATE).edit().putInt("notSeen",0).apply();

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
            new PopUtil(this, 1).show();

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
        msgRecycler = findViewById(R.id.msgRecycler);
        shimmerLayout = findViewById(R.id.shimmerLayout);

    }
    void setListRecycler() {

        mAdapter = new MessagesAdapter(messagesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        msgRecycler.setLayoutManager(mLayoutManager);
        msgRecycler.setItemAnimator(new DefaultItemAnimator());
        msgRecycler.setAdapter(mAdapter);

        prepareData();

    }

    void prepareData() {

// Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference msgRef = db.collection("messages");

        msgRef.orderBy("TIME", Query.Direction.DESCENDING)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()) {


                        if (document.getId().contains("_xxx@$zQ121")) {
                            list = new MessagesModel(document.getString("TITLE"), document.getString("MSG"), document.getString("AUTHOR"), document.getLong("TIME"));
                            messagesList.add(list);
                            //Log.d("TAG", document.getId() + " => " + document.getString("AUTHOR"));
                        }
                    }
                    shimmerLayout.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();

                }
                else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });



    }


    }