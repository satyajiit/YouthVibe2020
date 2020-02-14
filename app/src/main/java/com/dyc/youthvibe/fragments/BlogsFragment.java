package com.dyc.youthvibe.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dyc.youthvibe.BuildConfig;
import com.dyc.youthvibe.GetterSetter.BlogsModel;
import com.dyc.youthvibe.GetterSetter.ScheduleModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.activity.MainActivity;
import com.dyc.youthvibe.adapters.BlogsAdapter;
import com.dyc.youthvibe.adapters.ScheduleItemsAdapter;
import com.dyc.youthvibe.utils.ImagePicker;
import com.dyc.youthvibe.utils.PopUtil;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class BlogsFragment extends Fragment {

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

    public BlogsFragment() {
        // Required empty public constructor
    }

    public static BlogsFragment newInstance() {
        return new BlogsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blogs, container, false);

        initUI(v);

        setUpListeners();

        setUpRecycler();

        return v;



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

       dialog = new Dialog(getActivity());
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
                        Toasty.error(getActivity(),"Select a file first!").show();

                    else if (userPostMsg.length() < 5)
                        Toasty.error(getActivity(),"Too Small Message!").show();

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

    private void initUI(View v) {

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "cav.ttf");

        blogsRecyclerView = v.findViewById(R.id.blogsRecyclerView);

        fabAdd = v.findViewById(R.id.fabAdd);

        emptyTV = v.findViewById(R.id.emptyTV);

        loaderShim = v.findViewById(R.id.loaderShim);

        Toasty.Config.getInstance().setToastTypeface(font)
    .allowQueue(true) // optional (prevents several Toastys from queuing)
    .apply();



    }



    // Select Image method
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

    // Override onActivityResult method
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
                                getActivity().getContentResolver(),
                                filePath);
                postImage.setImageBitmap(bitmap);
                dummyImageText.setVisibility(View.GONE);

                 bmp = ImagePicker.getImageFromResult(getActivity(), resultCode, data);//your compressed bitmap here



            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
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
                                    + time+"_"+getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA")+".jpg");

            // adding listeners on upload

            // or failure of image
            ref.putBytes(byteArray)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {


                                   Toasty.info(getActivity(),"Adding Your Blog...",Toasty.LENGTH_SHORT).show();

                                   pushToDB();


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                           Toasty.error(getActivity(), "Posting Failed!",Toasty.LENGTH_SHORT).show();

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


        adapter = new BlogsAdapter(blogsListList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        blogsRecyclerView.setLayoutManager(mLayoutManager);
        blogsRecyclerView.setItemAnimator(new DefaultItemAnimator());
     adapter.setHasStableIds(true);
        blogsRecyclerView.setAdapter(adapter);

        // if (blogsListList.size() == 0) {

             db.collection("BlogPosts")
                     .whereEqualTo("validated", true)
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
                                             document.getString("userName"));
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
        data.put("userName", getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("name","NA"));
        data.put("cllg", getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("cllg","TBA"));
        data.put("id", getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA"));
        data.put("msg", userPostMsg);
        data.put("time", time);
        data.put("validated", false);


        db.collection("BlogPosts").document(time+"_"+getActivity().getSharedPreferences("YV",MODE_PRIVATE).getString("ID","NA")+".jpg")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toasty.success(getActivity(), "Your Post will be Validated Soon.",Toasty.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toasty.error(getActivity(), "Please try again later!",Toasty.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });




    }


}





