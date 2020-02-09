package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.GetterSetterEnq;
import com.dyc.youthvibe.R;

import java.util.List;

public class enqAdapter extends RecyclerView.Adapter<enqAdapter.MyViewHolder> {

    private List<GetterSetterEnq> contactList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desg;
        LinearLayout clicker;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
            desg =view.findViewById(R.id.desg);
            clicker = view.findViewById(R.id.clicker);
        }
    }


    public enqAdapter(List<GetterSetterEnq> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enquiry_item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GetterSetterEnq list = contactList.get(position);
        holder.name.setText(list.getName());
        holder.desg.setText(list.getDesg());

        holder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+list.getNumber()));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
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