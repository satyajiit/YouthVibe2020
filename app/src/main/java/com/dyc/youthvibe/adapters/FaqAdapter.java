package com.dyc.youthvibe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dyc.youthvibe.GetterSetter.FaqModel;
import com.dyc.youthvibe.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder> {

    private List<FaqModel> faqList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView faqTv, ansTv;

        public MyViewHolder(View view) {
            super(view);
            faqTv =  view.findViewById(R.id.faqTv);
            ansTv =  view.findViewById(R.id.ansTv);
        }
    }


    public FaqAdapter(List<FaqModel> faqList, Context context) {
        this.faqList = faqList;
        this.context = context;
    }

    @Override
    public FaqAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_faq, parent, false);

        return new FaqAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FaqAdapter.MyViewHolder holder, int position) {

        final FaqModel list = faqList.get(position);

        holder.faqTv.setText(list.getQuestion());

        holder.ansTv.setText(list.getAns());
    }

    @Override
    public int getItemCount() {
        return faqList.size();
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