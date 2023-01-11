package com.drdistributor.dr;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class My_order_details_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<My_order_details_get_or_set> livetvItems;

    public My_order_details_Adapter(Context context, List<My_order_details_get_or_set> arraylist)
    {
        this.context = context;
        this.livetvItems = arraylist;
    }
 
    @Override
	public int getCount() {
		return livetvItems.size();
	}
    
   
    @Override
	public View getView(int position, View view, ViewGroup parent) 
    {
		// TODO Auto-generated method stub
       
        LayoutInflater abc = ((Activity) context).getLayoutInflater();
		View itemView = abc.inflate(R.layout.my_order_details_item, null,true);
		final My_order_details_get_or_set My_order_details_get = livetvItems.get(position);

        ImageView item_image = itemView.findViewById(R.id.item_image);
        ImageView item_featured_img = itemView.findViewById(R.id.item_featured_img);
        ImageView item_out_of_stock_img = itemView.findViewById(R.id.item_out_of_stock_img);

        TextView item_name = itemView.findViewById(R.id.item_name);
        TextView item_expiry = itemView.findViewById(R.id.item_expiry);
        TextView item_company = itemView.findViewById(R.id.item_company);
        TextView item_quantity = itemView.findViewById(R.id.item_quantity);
        TextView item_date_time = itemView.findViewById(R.id.item_date_time);
        TextView item_price = itemView.findViewById(R.id.item_price);

        String item_scheme = My_order_details_get.item_scheme();
        String scheme = "";
        if(!item_scheme.equals("0+0")) {
            scheme  = " | <font color='" + context.getString(R.string.item_scheme_color) + "'>Scheme : " + item_scheme + "</font>";
        }

        Picasso.with(context).load(My_order_details_get.item_image()).into(item_image);
        item_name.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_name_color)+"'>"+My_order_details_get.item_name()+"</font> <font color='"+context.getString(R.string.item_packing_color)+"';'><small>("+ My_order_details_get.item_packing()+" Packing)</small></font>"));
        item_expiry.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_expiry_color)+"'>Expiry : "+My_order_details_get.item_expiry()+"</font>"));
        item_company.setText(Html.fromHtml("By : " +My_order_details_get.item_company()));
        item_quantity.setText(Html.fromHtml("Order quantity : " +My_order_details_get.item_quantity() + scheme));
        item_price.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_price2_color)+"'>Price : ₹ " +My_order_details_get.item_price()+"/-</font> | Total : ₹ "+My_order_details_get.item_quantity_price()+"/-"));
        item_date_time.setText(Html.fromHtml(My_order_details_get.item_modalnumber()+" | "+My_order_details_get.item_date_time()));
        return itemView;
    }

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}