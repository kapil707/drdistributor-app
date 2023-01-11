package com.drdistributor.dr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter0 extends RecyclerView.Adapter<RecyclerViewAdapter0.ViewHolder> {
    private ItemClickListener clickListener;
    Context context;

    List<Homepage_box_get_or_set> list_item;

    public RecyclerViewAdapter0(List<Homepage_box_get_or_set> arraylist, Context context){

        super();

        this.list_item = arraylist;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items0, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Homepage_box_get_or_set Homepage_box_get =  list_item.get(position);

        holder.NameTextView.setText(Homepage_box_get.item_company());
    }

    @Override
    public int getItemCount() {

        return list_item.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView NameTextView;

        public ViewHolder(View itemView) {

            super(itemView);

            NameTextView = (TextView) itemView.findViewById(R.id.name) ;
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
