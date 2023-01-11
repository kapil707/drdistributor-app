package com.drdistributor.dr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {
    private ItemClickListener clickListener;
    Context context;

    List<Homepage_box_get_or_set> list_item;

    public RecyclerViewAdapter2(List<Homepage_box_get_or_set> arraylist, Context context){

        super();

        this.list_item = arraylist;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items2, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Homepage_box_get_or_set Homepage_box_get =  list_item.get(position);

        holder.item_featured.setVisibility(View.GONE);
        if(Homepage_box_get.item_featured().equals("1")) {
            holder.item_featured.setVisibility(View.VISIBLE);
        }

        holder.item_out_of_stock.setVisibility(View.GONE);
        holder.item_image.setAlpha(0.9f);
        if (Homepage_box_get.item_quantity().equals("0")) {
            holder.item_out_of_stock.setVisibility(View.VISIBLE);
            holder.item_image.setAlpha(0.5f);
        }

        Picasso.with(context).load(Homepage_box_get.item_image()).into(holder.item_image);
        holder.item_name.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_name_color)+"'>"+Homepage_box_get.item_name()+"</font> <font color='"+context.getString(R.string.item_packing_color)+"';'><small>("+Homepage_box_get.item_packing()+" Pack)</small></font>"));
        holder.item_margin.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_margin_color)+"'>Margin : "+Homepage_box_get.item_margin()+"% </font>"));
        holder.item_company.setText("By " + Homepage_box_get.item_company());

        String mrp = "MRP : ₹ "+Homepage_box_get.item_mrp()+"/-";
        String ptr = "PTR : ₹ "+Homepage_box_get.item_ptr()+"/-";
        String price = "*Approximate ~ : ₹ "+Homepage_box_get.item_price()+"/-";

        holder.item_mrp.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_mrp_color)+"'>"+mrp+"</font>"));
        holder.item_ptr.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_ptr_color)+"'>"+ptr+"</font>"));
        holder.item_price.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_price_color)+"'><b>"+price+"</b></font>"));

    }

    @Override
    public int getItemCount() {

        return list_item.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView item_image,item_featured,item_out_of_stock;
        public TextView item_name, item_margin, item_company,item_mrp,item_ptr,item_price;

        public ViewHolder(View itemView) {

            super(itemView);

            item_image = itemView.findViewById(R.id.item_image);
            item_featured = itemView.findViewById(R.id.item_featured);
            item_out_of_stock = itemView.findViewById(R.id.item_out_of_stock);

            item_name = itemView.findViewById(R.id.item_name);
            item_margin = itemView.findViewById(R.id.item_margin);
            item_company = itemView.findViewById(R.id.item_company);

            item_mrp = itemView.findViewById(R.id.item_mrp);
            item_ptr = itemView.findViewById(R.id.item_ptr);
            item_price = itemView.findViewById(R.id.item_price);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}