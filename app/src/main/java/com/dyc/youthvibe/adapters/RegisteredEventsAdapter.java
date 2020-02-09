package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.RegisteredModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.utils.jsonLoader;
import com.dyc.youthvibe.vibeActivity.EventDetailsActivity;
import com.dyc.youthvibe.vibeActivity.vibeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

public class RegisteredEventsAdapter extends RecyclerView.Adapter<RegisteredEventsAdapter.MyViewHolder> {

    private List<RegisteredModel> eventsList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName, teamName, teamId;

        CardView cardClicker;


        public MyViewHolder(View view) {
            super(view);
            eventName = view.findViewById(R.id.eventName);
            teamId = view.findViewById(R.id.teamId);
            teamName = view.findViewById(R.id.teamName);
            cardClicker  = view.findViewById(R.id.cardClicker);
        }

    }

    public RegisteredEventsAdapter(List<RegisteredModel> eventsList, Context context) {
        this.eventsList = eventsList;
        this.context = context;

    }

    @Override
    public RegisteredEventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_registered_events, parent, false);

        return new RegisteredEventsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RegisteredEventsAdapter.MyViewHolder holder, int position) {

        final RegisteredModel list = eventsList.get(position);

        holder.eventName.setText(list.getEvent_name());


        if (list.getTeam_id().equals("0")){
            holder.teamId.setVisibility(View.GONE);
            holder.teamName.setVisibility(View.GONE);
        }
        else {

            holder.teamName.setText(list.getTeam_name());

            holder.teamId.setText(list.getTeam_id());

        }

        holder.cardClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        if (checkIfExist(list.getEvent_name().toUpperCase())){

                            Intent i = new Intent(context, EventDetailsActivity.class);
                            i.putExtra("eventName",list.getEvent_name().toUpperCase());

                            context.startActivity(i);

                            ((AppCompatActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }

                        else {

                            Intent i = new Intent(context, vibeActivity.class);


                            context.startActivity(i);

                            ((AppCompatActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }



            }
        });




    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }




    @Override
    public int getItemCount() {
        return eventsList.size();
    }


boolean checkIfExist(String name){

JSONObject jsonObject = null;
    try {
        jsonObject = new jsonLoader(context, "RuleBooksData.json").loadJSONFromAsset();
    } catch (JSONException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    return jsonObject.has(name);

}

}