package com.dyc.youthvibe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dyc.youthvibe.GetterSetter.BlogsModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.adapters.BlogsAdapter;
import com.dyc.youthvibe.utils.ImagePicker;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.vibeActivity.SingleCultureActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class OwnBlogsActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final int PICK_IMAGE_REQUEST = 134;
    FloatingActionButton fabAdd;
    ImageView postImage;
    TextView dummyImageText, emptyTV ;
    long time;
    ScrollView loaderShim;
    Uri filePath;
    Dialog dialog;
    String userPostMsg;
    NumberProgressBar number_progress_bar;

    RecyclerView blogsRecyclerView;
    private List<BlogsModel> blogsListList = new ArrayList<>();
    BlogsAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_blogs);

        initUI();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpListeners();

        setUpRecycler();
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



        toolbar = findViewById(R.id.toolbar);
        Typeface font = Typeface.createFromAsset(getAssets(), "cav.ttf");

        blogsRecyclerView = findViewById(R.id.blogsRecyclerView);

        fabAdd = findViewById(R.id.fabAdd);

        emptyTV = findViewById(R.id.emptyTV);

        loaderShim = findViewById(R.id.loaderShim);

        Toasty.Config.getInstance().setToastTypeface(font)
                .allowQueue(true) // optional (prevents several Toastys from queuing)
                .apply();


    }

    void setUpListeners(){


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddDialog();

            }
        });

    }
    private void showAddDialog() {

        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.setContentView(R.layout.pop_add_blog);


        final Button addBtnPop = dialog.findViewById(R.id.addBlog);
        CardView crdAddImage = dialog.findViewById(R.id.crdAddImage);
        number_progress_bar =  dialog.findViewById(R.id.number_progress_bar);
        postImage = dialog.findViewById(R.id.postImage);

        dummyImageText = dialog.findViewById(R.id.dummyImageText);
        final TextView dummyMsgText = dialog.findViewById(R.id.dummyMsgText);
        final EditText Msgtext = dialog.findViewById(R.id.Msgtext);



        Msgtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }
            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 1) {

                    dummyMsgText.setVisibility(View.GONE);


                }

                else dummyMsgText.setVisibility(View.VISIBLE);
            }
        });


        addBtnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userPostMsg = Msgtext.getText().toString();

                if ( filePath == null )
                    Toasty.error(OwnBlogsActivity.this,"Select a file first!").show();

                else if (userPostMsg.length() < 5)
                    Toasty.error(OwnBlogsActivity.this,"Too Small Message!").show();

                else {

                    uploadImage();
                    addBtnPop.setEnabled(false);
                    number_progress_bar.setVisibility(View.VISIBLE);

                }

            }
        });

        crdAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                SelectImage();

            }
        });



        dialog.show();



    }


    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                               getContentResolver(),
                                filePath);
                postImage.setImageBitmap(bitmap);
                dummyImageText.setVisibility(View.GONE);

                bmp = ImagePicker.getImageFromResult(this, resultCode, data);//your compressed bitmap here



            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }



    private void SelectImage()
    {


        time = System.currentTimeMillis();
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void uploadImage()
    {

        // Create a storage reference from our app




        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        if (filePath != null) {

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            // Code for showing progressDialog while uploading

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();





            // Defining the child of storageReference
            final StorageReference ref
                    = storageRef
                    .child(
                            "blogData/"
                                    + time+"_"+getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA")+".jpg");

            // adding listeners on upload

            // or failure of image
            ref.putBytes(byteArray)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {


                                    Toasty.info(OwnBlogsActivity.this,"Adding Your Blog...",Toasty.LENGTH_SHORT).show();

                                    pushToDB();


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            Toasty.error(OwnBlogsActivity.this, "Posting Failed!",Toasty.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());

                                    number_progress_bar.setProgress( (int)progress);


                                }
                            });
        }
    }



    void setUpRecycler() {


        adapter = new BlogsAdapter(blogsListList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        blogsRecyclerView.setLayoutManager(mLayoutManager);
        blogsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setHasStableIds(true);
        blogsRecyclerView.setAdapter(adapter);

        // if (blogsListList.size() == 0) {

        db.collection("BlogPosts")
                .whereEqualTo("id",getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA"))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            BlogsModel list;

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                list = new BlogsModel(document.getId(),
                                        document.getString("cllg"),
                                        document.getString("msg"),
                                        document.getString("userName"),
                                        document.getBoolean("validated"));
                                blogsListList.add(list);




                            }

                            adapter.notifyDataSetChanged();
                            if (adapter.getItemCount()==0) fallback();
                            loaderShim.setVisibility(View.GONE);

                        } else {

                            fallback();

                        }
                    }
                });
        //  }

        //  Log.d("TESTED",blogsListList.get(0).getMsg()+" ");



    }

    private void fallback() {

        blogsRecyclerView.setVisibility(View.GONE);
        emptyTV.setVisibility(View.VISIBLE);

    }

    private void pushToDB() {


        // Access a Cloud Firestore instance from your Activity


        Map<String, Object> data = new HashMap<>();
        data.put("userName", getSharedPreferences("YV",MODE_PRIVATE).getString("name","NA"));
        data.put("cllg", getSharedPreferences("YV",MODE_PRIVATE).getString("cllg","TBA"));
        data.put("id", getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA"));
        data.put("msg", userPostMsg);
        data.put("time", time);
        data.put("validated", false);


        db.collection("BlogPosts").document(time+"_"+getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA")+".jpg")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toasty.success(OwnBlogsActivity.this, "Your Post will be Validated Soon.",Toasty.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toasty.error(OwnBlogsActivity.this, "Please try again later!",Toasty.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });




    }


}
