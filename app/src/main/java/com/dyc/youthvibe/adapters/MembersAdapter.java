package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.MemberModels;
import com.dyc.youthvibe.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {

    private List<MemberModels> membersList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView memberName, memberDesg;
        CardView cardClicker;
        CircleImageView memberImage;
        ProgressBar progress_circular;

        public MyViewHolder(View view) {
            super(view);
            memberName =  view.findViewById(R.id.memberName);
            memberDesg = view.findViewById(R.id.memberDesg);
            cardClicker =  view.findViewById(R.id.cardClicker);
            memberImage =  view.findViewById(R.id.memberImage);
            progress_circular = view.findViewById(R.id.progress_circular);
        }
    }


    public MembersAdapter(List<MemberModels> membersList, Context context) {
        this.membersList = membersList;
        this.context = context;
    }

    @Override
    public MembersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_list_item, parent, false);

        return new MembersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MembersAdapter.MyViewHolder holder, int position) {
        final MemberModels list = membersList.get(position);

        holder.memberName.setText(list.getMemberName());
        holder.memberDesg.setText(list.getMemberDesg().substring(0,list.getMemberDesg().indexOf('@')));



        holder.cardClicker.setCardBackgroundColor(list.getColor());



        Picasso.get().
                load("https://youthvibe-2dd0f.firebaseapp.com/user/"+list.getImgUrl())
                .into(holder.memberImage, new Callback() {
                    @Override
                    public void onSuccess() {

                        holder.memberImage.setVisibility(View.VISIBLE);
                        holder.progress_circular.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        holder.cardClicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.getMemberName().equals("Aman Kumar")||list.getMemberName().equals("Kuldip Kumar Vashisht")||list.getMemberName().equals("Sorabh Lakhanpal"))
                    sendMail(list.getsLink());
                else if (list.getMemberDesg().substring(list.getMemberDesg().indexOf('@')+1).equals("CORE")){

                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+list.getsLink()));
                    context.startActivity(callIntent);


                }
                else {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(list.getsLink()));
                    context.startActivity(browserIntent);

                }


            }
        });
    }

    void sendMail(String email){


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Regarding YV");
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return membersList.size();
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
