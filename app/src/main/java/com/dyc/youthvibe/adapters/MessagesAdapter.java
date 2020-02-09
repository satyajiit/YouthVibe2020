package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.MessagesModel;
import com.dyc.youthvibe.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessagesAdapter  extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<MessagesModel> messagesList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView msgTV, titleTV, authorTimeTV;

        public MyViewHolder(View view) {
            super(view);
            msgTV =  view.findViewById(R.id.msgTV);
            titleTV = view.findViewById(R.id.titleTV);
            authorTimeTV = view.findViewById(R.id.authorTimeTV);

        }
    }


    public MessagesAdapter(List<MessagesModel> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_list_view, parent, false);

        return new MessagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessagesAdapter.MyViewHolder holder, int position) {

        final MessagesModel list = messagesList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy hh:mm a");
        String dateString = formatter.format(new Date(list.getTime()));


        holder.authorTimeTV.setText(list.getAuthor()+" ("+dateString.toUpperCase()+")");

        holder.titleTV.setText(list.getTitle());
        holder.msgTV.setText(list.getMsg().replace("%name%",context.getSharedPreferences("YV",MODE_PRIVATE).getString("name","user")).replace("\n","\n"));




    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
