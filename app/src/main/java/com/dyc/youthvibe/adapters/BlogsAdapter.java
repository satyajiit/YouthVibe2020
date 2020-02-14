package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.BlogsModel;
import com.dyc.youthvibe.GetterSetter.FaqModel;
import com.dyc.youthvibe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.MyViewHolder> {

    private List<BlogsModel> blogsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView item_cllg, item_msg, item_name;
        ImageView item_image;
        ProgressBar progress_circular;

        public MyViewHolder(View view) {
            super(view);
            item_cllg =  view.findViewById(R.id.item_cllg);
            item_msg =  view.findViewById(R.id.item_msg);
            item_image =  view.findViewById(R.id.item_image);
            progress_circular =  view.findViewById(R.id.progress_circular);
            item_name = view.findViewById(R.id.item_name);
        }
    }


    public BlogsAdapter(List<BlogsModel> blogsList, Context context) {
        this.blogsList = blogsList;
        this.context = context;


    }

    @Override
    public BlogsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_blogs, parent, false);



        return new BlogsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BlogsAdapter.MyViewHolder holder, int position) {

        final BlogsModel list = blogsList.get(position);

        holder.item_msg.setText(list.getMsg());

        holder.item_name.setText(list.getName());

        holder.item_cllg.setText(list.getCllg());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        if (list.getValidate()!=null){

            holder.item_name.setText("STATUS");

            if (list.getValidate()) {
                holder.item_cllg.setText("APPROVED");
                holder.item_cllg.setTextColor(Color.parseColor("#388E3C"));

            }
            else {

                holder.item_cllg.setText("PENDING APPROVAL");
                holder.item_cllg.setTextColor(Color.parseColor("#D50000"));

            }

        }

        storageReference.child("blogData/"+list.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'


                Picasso.get().
                        load(uri)
                        .into(holder.item_image, new Callback() {
                            @Override
                            public void onSuccess() {

                                holder.item_image.setVisibility(View.VISIBLE);
                                holder.progress_circular.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError(Exception e) {



                            }
                        });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }

    @Override
    public int getItemCount() {
        return blogsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
}