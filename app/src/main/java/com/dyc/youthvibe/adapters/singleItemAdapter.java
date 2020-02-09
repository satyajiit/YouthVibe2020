package com.dyc.youthvibe.adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.EventNamesModel;
import com.dyc.youthvibe.R;

import java.util.List;

public class singleItemAdapter extends RecyclerView.Adapter<singleItemAdapter.MyViewHolder> {

    private List<EventNamesModel> itemsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
        }
    }


    public singleItemAdapter(List<EventNamesModel> itemsList) {
        this.itemsList = itemsList;
    }
    @Override
    public singleItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_list, parent, false);

        return new singleItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(singleItemAdapter.MyViewHolder holder, int position) {
        final EventNamesModel list = itemsList.get(position);

        if (!list.getEventName().contains("Round "))
        holder.name.setText(Html.fromHtml("<font color='#00897B'>"+(position+1)+".</font> "+list.getEventName().replace("\n","<br>"), Html.FROM_HTML_MODE_COMPACT));
        else    holder.name.setText(list.getEventName());


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
        return itemsList.size();
    }
}