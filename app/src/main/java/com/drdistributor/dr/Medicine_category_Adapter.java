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

public class Medicine_category_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<Medicine_category_get_or_set> livetvItems;

    public Medicine_category_Adapter(Context context, List<Medicine_category_get_or_set> arraylist)
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
		View itemView = abc.inflate(R.layout.medicine_category_item, null,true);
		final Medicine_category_get_or_set m = livetvItems.get(position);

        ImageView item_image = itemView.findViewById(R.id.item_image);
        ImageView item_featured = itemView.findViewById(R.id.item_featured);
        ImageView item_out_of_stock = itemView.findViewById(R.id.item_out_of_stock);

        TextView item_name = itemView.findViewById(R.id.item_name);
        TextView item_margin = itemView.findViewById(R.id.item_margin);
        TextView item_company = itemView.findViewById(R.id.item_company);

        TextView item_mrp = itemView.findViewById(R.id.item_mrp);
        TextView item_ptr = itemView.findViewById(R.id.item_ptr);
        TextView item_price = itemView.findViewById(R.id.item_price);

        item_featured.setVisibility(View.GONE);
        if(m.item_featured().equals("1")) {
            item_featured.setVisibility(View.VISIBLE);
        }

        item_out_of_stock.setVisibility(View.GONE);
        item_image.setAlpha(0.9f);
        if (m.item_quantity().equals("0")) {
            item_out_of_stock.setVisibility(View.VISIBLE);
            item_image.setAlpha(0.5f);
        }

        Picasso.with(context).load(m.item_image()).into(item_image);
        item_name.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_name_color)+"'>"+m.item_name()+"</font> <font color='"+context.getString(R.string.item_packing_color)+"';'><small>("+ m.item_packing()+" Pack)</small></font>"));
        item_margin.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_margin_color)+"'>Margin : "+m.item_margin()+"%</font>"));
        item_company.setText("By " + m.item_company());

        String mrp = "MRP : ₹ "+m.item_mrp()+"/-";
        String ptr = "PTR : ₹ "+m.item_ptr()+"/-";
        String price = "*Approximate ~  : ₹ "+m.item_price()+"/-";

        item_mrp.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_mrp_color)+"'>"+mrp+"</font>"));
        item_ptr.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_ptr_color)+"'>"+ptr+"</font>"));
        item_price.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_price_color)+"'><b>"+price+"</b></font>"));

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