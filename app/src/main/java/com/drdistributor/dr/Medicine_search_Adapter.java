package com.drdistributor.dr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Medicine_search_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<Medicine_search_get_or_set> item_list;

    public Medicine_search_Adapter(Context context, List<Medicine_search_get_or_set> arraylist)
    {
        this.context = context;
        this.item_list = arraylist;
    }

    @Override
	public int getCount() {
		return item_list.size();
	}


    @Override
	public View getView(int position, View view, ViewGroup parent)
    {
		// TODO Auto-generated method stub

        LayoutInflater abc = ((Activity) context).getLayoutInflater();
		View itemView = abc.inflate(R.layout.medicine_search_item, null,true);
		final Medicine_search_get_or_set Search_medicine_get = item_list.get(position);

        ImageView item_image = itemView.findViewById(R.id.item_image);
        ImageView item_featured = itemView.findViewById(R.id.item_featured);
        ImageView item_out_of_stock = itemView.findViewById(R.id.item_out_of_stock);

        TextView item_name = itemView.findViewById(R.id.item_name);
        TextView item_company = itemView.findViewById(R.id.item_company);
        TextView item_expiry = itemView.findViewById(R.id.item_expiry);
        TextView item_stock = itemView.findViewById(R.id.item_stock);
        TextView item_price = itemView.findViewById(R.id.item_price);
        TextView item_description = itemView.findViewById(R.id.item_description);

        Picasso.with(context).load(Search_medicine_get.item_image()).into(item_image);
        if(Search_medicine_get.item_featured().equals("1")) {
            item_featured.setVisibility(View.VISIBLE);
        }
        else
        {
            item_featured.setVisibility(View.GONE);
        }

        item_name.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_name_color)+"'>"+Search_medicine_get.item_name()+"</font> <font color='"+context.getString(R.string.item_packing_color)+"';'><small>("+ Search_medicine_get.item_packing()+" Packing)</small></font>"));
        item_company.setText(Html.fromHtml("By : " +Search_medicine_get.item_company()));
        item_expiry.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_margin_color)+"'>Margin : "+Search_medicine_get.item_margin()+"% </font> | <font color='"+context.getString(R.string.item_expiry_color)+"'>Expiry : "+Search_medicine_get.item_expiry()+"</font>"));
        String item_scheme = Search_medicine_get.item_scheme();
        String scheme = "";
        if(!item_scheme.equals("0+0")) {
            scheme  = " | <font color='" + context.getString(R.string.item_scheme_color) + "'>Scheme : " + Search_medicine_get.item_scheme() + "</font>";
        }
        item_stock.setText(Html.fromHtml("Stock : " + Search_medicine_get.item_quantity() + scheme));

        int qty = Integer.valueOf(Search_medicine_get.item_quantity());
        if(Search_medicine_get.item_stock().equals("Available"))
        {
            item_stock.setText(Html.fromHtml("Available" + scheme));
        }

        item_image.setAlpha(0.9f);
        item_out_of_stock.setVisibility(View.GONE);
        if(qty==0)
        {
            item_image.setAlpha(0.5f);
            item_out_of_stock.setVisibility(View.VISIBLE);
            item_stock.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_out_of_stock_color)+"'>Out of stock</font>"));
        }

        String mrp = "MRP : ₹ "+String.format("%.2f",Float.parseFloat(Search_medicine_get.item_mrp()))+"/-";
        String ptr = "PTR : ₹ "+String.format("%.2f",Float.parseFloat(Search_medicine_get.item_ptr()))+"/-";
        String final_price = "*Approximate ~ : ₹ "+String.format("%.2f",Float.parseFloat(Search_medicine_get.item_price()))+"/-";
        item_price.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_mrp_color)+"'>"+mrp+"</font> | <font color='"+context.getString(R.string.item_ptr_color)+"'>"+ptr+"</font> | <b><font color='"+context.getString(R.string.item_price_color)+"'>"+final_price+"</font></b>"));

        item_description.setText(Search_medicine_get.item_description1());
        item_description.setVisibility(View.GONE);
        if(!Search_medicine_get.item_description1().equals(""))
        {
            item_description.setTextSize(15);
            item_description.setVisibility(View.VISIBLE);
        }

        if(!Search_medicine_get.similar_items().equals(""))
        {
            item_description.setText(Search_medicine_get.similar_items());
            item_description.setVisibility(View.VISIBLE);
            item_description.setTextSize(18);

            item_description.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent in = new Intent();
                    in.putExtra("item_code", Search_medicine_get.item_code());
                    in.putExtra("item_page_type","medicine_similar");
                    in.setClass(context, Medicine_category.class);
                    context.startActivity(in);
                }
            });
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