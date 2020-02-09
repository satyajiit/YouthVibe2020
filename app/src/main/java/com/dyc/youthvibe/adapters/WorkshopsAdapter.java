package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.MemberModels;
import com.dyc.youthvibe.GetterSetter.WorkShopsModel;
import com.dyc.youthvibe.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WorkshopsAdapter extends RecyclerView.Adapter<WorkshopsAdapter.MyViewHolder> {

    private List<WorkShopsModel> workShopsModelListList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView workshopName, registrationFee, startDate, endDate, days, venue;
        ImageView workshopImage;
        ProgressBar progressBar;
        Button register;

        public MyViewHolder(View view) {
            super(view);
            workshopName =  view.findViewById(R.id.workshopName);
            registrationFee =  view.findViewById(R.id.registrationFee);
            startDate =  view.findViewById(R.id.startDate);
            days = view.findViewById(R.id.days);
            endDate = view.findViewById(R.id.endDate);

            venue = view.findViewById(R.id.venue);

            workshopImage = view.findViewById(R.id.workshopImage);

            register = view.findViewById(R.id.register);

            progressBar = view.findViewById(R.id.progressBar);

        }
    }


    public WorkshopsAdapter(List<WorkShopsModel> workShopsModelListList, Context context) {
        this.workShopsModelListList = workShopsModelListList;
        this.context = context;
    }

    @Override
    public WorkshopsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_workshops, parent, false);

        return new WorkshopsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WorkshopsAdapter.MyViewHolder holder, int position) {
        final WorkShopsModel list = workShopsModelListList.get(position);



        holder.workshopName.setText(list.getWorkshopName());


        if (list.getDays().equals("NA")){

            holder.register.setText("Watch Clip");
            holder.registrationFee.setText("Date: "+list.getRegistrationFee());
            holder.startDate.setText("Time: "+list.getStartDate());
            holder.endDate.setText("Venue: "+list.getEndDate());
            holder.days.setVisibility(View.GONE);
            holder.venue.setVisibility(View.GONE);


        }
        else {

            holder.registrationFee.setText("Registration Fee: " + list.getRegistrationFee());

            holder.startDate.setText("Start Date: " + list.getStartDate());

            holder.endDate.setText("End Date: " + list.getEndDate());

            holder.days.setText("Days: " + list.getDays());

            holder.venue.setText("Venue: " + list.getVenue());

        }

        Picasso.get().
                load("https://youthvibe-2dd0f.firebaseapp.com/workshops/"+list.getWorkshopName().replace(":","")+".jpg")
                .into(holder.workshopImage, new Callback() {
                    @Override
                    public void onSuccess() {

                        holder.workshopImage.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });



        holder.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.getURL().equals("NA"))
                    Toast.makeText(context, "Not Available Yet!", Toast.LENGTH_SHORT).show();

                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.getURL()));
                    context.startActivity(browserIntent);

                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public int getItemCount() {
        return workShopsModelListList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
