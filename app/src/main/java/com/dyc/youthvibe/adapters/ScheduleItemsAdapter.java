package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.ScheduleModel;
import com.dyc.youthvibe.R;
import com.dyc.youthvibe.activity.MapActivity;
import com.dyc.youthvibe.utils.jsonLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class ScheduleItemsAdapter extends RecyclerView.Adapter<ScheduleItemsAdapter.MyViewHolder> {

    private List<ScheduleModel> eventsList;
    private Context context;
    int DAY;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName, timePost, timedTV, eventLocation;
        ImageView eventImage;
        ProgressBar progress_circular;
        CardView cardClicker;


        public MyViewHolder(View view) {
            super(view);
            eventName =  view.findViewById(R.id.eventName);
            timedTV =  view.findViewById(R.id.timedTV);
            timePost =  view.findViewById(R.id.timePost);
            eventLocation =  view.findViewById(R.id.eventLocation);
            eventImage = view.findViewById(R.id.eventImage);
            progress_circular = view.findViewById(R.id.progress_circular);
            cardClicker = view.findViewById(R.id.cardClicker);
        }
    }


    public ScheduleItemsAdapter(List<ScheduleModel> eventsList, Context context, int DAY) {
        this.eventsList = eventsList;
        this.context = context;
        this.DAY = DAY;
    }

    @Override
    public ScheduleItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_schedule_list_item, parent, false);

        return new ScheduleItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScheduleItemsAdapter.MyViewHolder holder, int position) {
        final ScheduleModel list = eventsList.get(position);

        holder.eventName.setText(list.getEventName());
        holder.eventLocation.setText(list.getEventLocation());

        holder.cardClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObject;

                try {

                    jsonObject = new jsonLoader(context, "mapData").loadJSONFromAsset();


                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {




                            if (key.toLowerCase().contains(list.getEventLocation().toLowerCase())){

                                Log.d("TEST",jsonObject.getJSONObject(key).get("LAT").toString()+","+jsonObject.getJSONObject(key).get("LAT").toString());

                                context.startActivity(new Intent(context, MapActivity.class)
                                        .putExtra("LatLng",jsonObject.getJSONObject(key).get("LAT").toString()+","+jsonObject.getJSONObject(key).get("LNG").toString())
                                        .putExtra("event",list.getEventName())
                                        .putExtra("loc",list.getEventLocation()));

                                break;

                            }




                        } catch (Exception e) {

                            Log.d("LOGGED",e.getMessage());

                        }
                    }




                }
                catch (Exception e){

                    //Show error dialog here


                }









            }
        });


        if (list.getTime().equals("0")){

            holder.timedTV.setText("NA");
            holder.timePost.setText("NA");

        }
        else {

            if( Integer.parseInt(list.getTime()) >= 12 ){

                int temp = Integer.parseInt(list.getTime())-12;


                holder.timePost.setText("PM");
                if (temp!=0)
                holder.timedTV.setText(String.valueOf(temp));
                else holder.timedTV.setText(list.getTime());
            }
            else{
                holder.timePost.setText("AM");
            holder.timedTV.setText(list.getTime());
            }

        }



        Picasso.get().
                load("https://youthvibe-2dd0f.web.app/event_schedule_pics/"+list.getEventName().replace("/","")+".jpg")
                .into(holder.eventImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.eventImage.setVisibility(View.VISIBLE);
                        holder.progress_circular.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


//
//        holder.addToCalender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                addToCalender(list.getTime(),list.getEventName(),list.getEventLocation());
//
//
//            }
//        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }



//    public void addToCalender(String Time, String eventName, String Loc) {
//
//
//
//        if (Time.equals("0")){
//
//            Time = "9";
//
//        }
//
//
//        Intent intent = new Intent(Intent.ACTION_INSERT);
//        intent.setType("vnd.android.cursor.item/event");
//
//
//
//        Calendar calendar = Calendar.getInstance();
//
//
//
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, 0);
//
//
//
//
//
//        if (Integer.parseInt(Time)>=12) {
//            calendar.set(Calendar.AM_PM, Calendar.PM);
//            int temp = Integer.parseInt(Time)-12;
//
//            if (temp!=0)
//                calendar.set(Calendar.HOUR, temp);
//            else
//                calendar.set(Calendar.HOUR, Integer.parseInt(Time));
//
//        }
//        else {
//            calendar.set(Calendar.AM_PM, Calendar.AM);
//            calendar.set(Calendar.HOUR, Integer.parseInt(Time));
//        }
//
//        calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
//        calendar.set(Calendar.DAY_OF_MONTH, DAY);
//        calendar.set(Calendar.YEAR, 2019);
//
//        long startTime = calendar.getTimeInMillis();
//
//        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
//        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
//
//        intent.putExtra(CalendarContract.Events.TITLE, eventName);
//        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventName+" - YouthVibe Event");
//        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, Loc+" - Lovely Professional University");
//        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=ONCE");
//
//        context.startActivity(intent);
//
//    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}