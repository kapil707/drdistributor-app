package com.drdistributor.dr;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class My_invoice_details_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<My_invoice_details_get_or_set> livetvItems;

    public My_invoice_details_Adapter(Context context, List<My_invoice_details_get_or_set> arraylist)
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
		View itemView = abc.inflate(R.layout.my_invoice_details_item, null,true);
		final My_invoice_details_get_or_set My_invoice_details_get = livetvItems.get(position);

        LinearLayout my_invoice_LinearLayout = itemView.findViewById(R.id.my_invoice_LinearLayout);
        LinearLayout item_delete_title = itemView.findViewById(R.id.item_delete_title);

        ImageView item_image = itemView.findViewById(R.id.item_image);
        ImageView item_featured_img = itemView.findViewById(R.id.item_featured_img);
        ImageView item_out_of_stock_img = itemView.findViewById(R.id.item_out_of_stock_img);

        TextView item_name = itemView.findViewById(R.id.item_name);
        TextView item_expiry = itemView.findViewById(R.id.item_expiry);
        TextView item_company = itemView.findViewById(R.id.item_company);
        TextView item_quantity = itemView.findViewById(R.id.item_quantity);
        TextView item_date_time = itemView.findViewById(R.id.item_date_time);
        TextView item_price = itemView.findViewById(R.id.item_price);
        TextView item_description1 = itemView.findViewById(R.id.item_description1);

        String item_delete_title_s = My_invoice_details_get.item_delete_title();
        if(item_delete_title_s.equals(""))
        {
            String item_scheme = My_invoice_details_get.item_scheme();
            String scheme = "";
            if(!item_scheme.equals("0+0")) {
                scheme  = " | <font color='" + context.getString(R.string.item_scheme_color) + "'>Scheme : " + item_scheme + "</font>";
            }

            Picasso.with(context).load(My_invoice_details_get.item_image()).into(item_image);
            item_name.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_name_color)+"'>"+My_invoice_details_get.item_name()+"</font> <font color='"+context.getString(R.string.item_packing_color)+"';'><small>("+ My_invoice_details_get.item_packing()+" Packing)</small></font>"));
            item_expiry.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_expiry_color)+"'>Expiry : "+My_invoice_details_get.item_expiry()+"</font>"));
            item_company.setText(Html.fromHtml("By : " +My_invoice_details_get.item_company()));
            item_quantity.setText(Html.fromHtml("Order quantity : " +My_invoice_details_get.item_quantity() + scheme));
            item_price.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_price2_color)+"'>Price : ₹ " +My_invoice_details_get.item_price()+"/-</font> | Total : ₹ "+My_invoice_details_get.item_quantity_price()+"/-"));
            item_date_time.setText(Html.fromHtml(My_invoice_details_get.item_modalnumber()+" | "+My_invoice_details_get.item_date_time()));

            item_description1.setVisibility(View.GONE);
            String item_description1_s = My_invoice_details_get.item_description1();
            if(!item_description1_s.equals("")) {
                item_description1.setText(My_invoice_details_get.item_description1());
                item_description1.setVisibility(View.VISIBLE);
            }

            my_invoice_LinearLayout.setVisibility(View.VISIBLE);
            item_delete_title.setVisibility(View.GONE);
        }
        else{
            my_invoice_LinearLayout.setVisibility(View.GONE);
            item_delete_title.setVisibility(View.VISIBLE);
        }
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