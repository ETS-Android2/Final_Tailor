package com.hmdapp.finaltailor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hmdapp.finaltailor.Models.Payment;
import com.hmdapp.finaltailor.Models.payment_report;
import com.hmdapp.finaltailor.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterListRemainder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<payment_report> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, payment_report obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListRemainder(Context context, List<payment_report> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView remainderSum;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            name = v.findViewById(R.id.txt_name_remainder);
            remainderSum = v.findViewById(R.id.txt_count_remainder);
            lyt_parent = v.findViewById(R.id.lyt_parent_remainder);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remainder_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            payment_report payment = items.get(position);
            view.name.setText(payment.getName()+"   ");
            view.remainderSum.setText(payment.getAmount()+"");



            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}