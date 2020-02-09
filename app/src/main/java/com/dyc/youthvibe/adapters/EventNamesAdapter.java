package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.EventNamesModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.vibeActivity.EventDetailsActivity;

import java.util.List;

public class EventNamesAdapter extends RecyclerView.Adapter<EventNamesAdapter.MyViewHolder> {

    private List<EventNamesModel> eventsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        LinearLayout clicker;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
            clicker =  view.findViewById(R.id.clicker);
        }
    }


    public EventNamesAdapter(List<EventNamesModel> eventsList, Context context) {
        this.eventsList = eventsList;
        this.context = context;
    }

    @Override
    public EventNamesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_list, parent, false);

        return new EventNamesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventNamesAdapter.MyViewHolder holder, int position) {
        final EventNamesModel list = eventsList.get(position);
        holder.name.setText(list.getEventName());

        holder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, EventDetailsActivity.class);
                i.putExtra("eventName",list.getEventName());

            context.startActivity(i);

                ((AppCompatActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
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