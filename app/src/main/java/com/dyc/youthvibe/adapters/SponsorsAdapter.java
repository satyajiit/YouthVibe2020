package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.MemberModels;
import com.dyc.youthvibe.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.MyViewHolder> {

    private List<MemberModels> sponsorsList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sponsorName;
        RelativeLayout clicker;
        ImageView sponsorImage;
        ProgressBar progressBar;
        TextView typeTv;

        public MyViewHolder(View view) {
            super(view);
            sponsorName =  view.findViewById(R.id.sponsorName);
            clicker =  view.findViewById(R.id.clicker);
            sponsorImage =  view.findViewById(R.id.sponsorImage);
            progressBar = view.findViewById(R.id.progressBar);
            typeTv = view.findViewById(R.id.sponsorType);

        }
    }


    public SponsorsAdapter(List<MemberModels> membersList, Context context) {
        this.sponsorsList = membersList;
        this.context = context;
    }

    @Override
    public SponsorsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sponsors_list_item, parent, false);

        return new SponsorsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SponsorsAdapter.MyViewHolder holder, int position) {
        final MemberModels list = sponsorsList.get(position);

        holder.sponsorName.setText(list.getMemberName());

        holder.typeTv.setText(list.getMemberDesg());

        Picasso.get().
                load("https://youthvibe-2dd0f.firebaseapp.com/sponsors/"+list.getMemberName()+".png")
                .into(holder.sponsorImage, new Callback() {
                    @Override
                    public void onSuccess() {

                        //holder.memberImage.setVisibility(View.VISIBLE);
                      holder.progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        holder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.getsLink()));
                context.startActivity(browserIntent);

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public int getItemCount() {
        return sponsorsList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
