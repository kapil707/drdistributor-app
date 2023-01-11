package com.drdistributor.dr;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class My_cart_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<My_cart_get_or_set> movieItems;

    public My_cart_Adapter(Context context, List<My_cart_get_or_set> arraylist)
    {
        this.context = context;
        this.movieItems = arraylist;
    }
 
    @Override
	public int getCount() {
		return movieItems.size();
	}
    
   
    @Override
	public View getView(int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater abc = ((Activity) context).getLayoutInflater();
        View itemView = abc.inflate(R.layout.my_cart_item, null, true);
        final My_cart_get_or_set My_cart_get = movieItems.get(position);

        ImageView item_image = itemView.findViewById(R.id.item_image);
        ImageView item_featured = itemView.findViewById(R.id.item_featured);
        ImageView item_out_of_stock = itemView.findViewById(R.id.item_out_of_stock);

        TextView item_name = itemView.findViewById(R.id.item_name);
        TextView item_company = itemView.findViewById(R.id.item_company);
        TextView item_expiry = itemView.findViewById(R.id.item_expiry);
        TextView item_stock = itemView.findViewById(R.id.item_stock);
        TextView item_price = itemView.findViewById(R.id.item_price);
        TextView item_datetime = itemView.findViewById(R.id.item_datetime);

        float subtotalcart = 0;
        String price = My_cart_get.item_price();
        String quantity = My_cart_get.item_quantity();
        subtotalcart = subtotalcart + (Float.parseFloat(price) * Float.parseFloat(quantity));

        Picasso.with(context).load(My_cart_get.item_image()).into(item_image);
        if(My_cart_get.item_featured().equals("1")) {
            item_featured.setVisibility(View.VISIBLE);
        }
        else
        {
            item_featured.setVisibility(View.GONE);
        }
        item_name.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_name_color)+"'>"+My_cart_get.item_name()+"</big></font> <font color='"+context.getString(R.string.item_packing_color)+"';'><small>("+ My_cart_get.item_packing()+" Packing)</small></font>"));
        item_expiry.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_margin_color)+"'>Margin : "+My_cart_get.item_margin()+"% </font>| <font color='"+context.getString(R.string.item_expiry_color)+"'>Expiry : "+My_cart_get.item_expiry()+"</font>"));
        item_company.setText("By " + My_cart_get.item_company());
        String item_scheme = My_cart_get.item_scheme();
        String scheme = "";
        if(!item_scheme.equals("0+0")) {
            scheme  = " | <font color='" + context.getString(R.string.item_scheme_color) + "'>Scheme : " + My_cart_get.item_scheme() + "</font>";
        }
        item_stock.setText(Html.fromHtml("Order quantity : " + My_cart_get.item_quantity() + scheme));
        item_datetime.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_date_time_color)+"'>"+My_cart_get.item_modalnumber()+"</font> | <font color='"+context.getString(R.string.item_date_time_color)+"'>"+My_cart_get.item_datetime()+"</font>"));
        item_price.setText(Html.fromHtml("<font color='"+context.getString(R.string.item_price2_color)+"'>"+"*Approximate ~ : ₹ " + String.format("%.2f",Float.parseFloat(My_cart_get.item_price()))+"/- </font> | <font color='"+context.getString(R.string.item_price_color)+"'> <b>Total : ₹ "+String.format("%.2f", subtotalcart)+"/- </b></font>"));

        ImageView medicine_cart_delete_btn = itemView.findViewById(R.id.medicine_cart_delete_btn);
        ImageView medicine_cart_edit_btn = itemView.findViewById(R.id.medicine_cart_edit_btn);

        medicine_cart_edit_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String item_code = My_cart_get.item_code();
                String item_image_s = My_cart_get.item_image();
                String item_name_s = My_cart_get.item_name();
                String item_packing_s = My_cart_get.item_packing();
                String item_expiry_s = My_cart_get.item_expiry();
                String item_company_s = My_cart_get.item_company();
                String item_quantity_s = My_cart_get.item_quantity();
                String item_price_s = My_cart_get.item_price();
                String item_scheme_s = My_cart_get.item_scheme();

                Intent in = new Intent();
                in.setClass(context, Medicine_details.class);
                in.putExtra("item_code", item_code);
                in.putExtra("item_image", item_image_s);
                in.putExtra("item_name", item_name_s);
                in.putExtra("item_packing", item_packing_s);
                in.putExtra("item_expiry", item_expiry_s);
                in.putExtra("item_company", item_company_s);
                in.putExtra("item_quantity", item_quantity_s);
                in.putExtra("item_price", item_price_s);
                in.putExtra("item_scheme", item_scheme_s);

                context.startActivity(in);
                //context.finish();
            }
        });

        medicine_cart_delete_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // Yes button clicked
                                try
                                {
                                    //Toast.makeText(context,"Delete "+m.med_item_name()+" Successfully", Toast.LENGTH_LONG).show();

                                    String item_code = My_cart_get.item_code();

                                    Intent in = new Intent();
                                    in.setClass(context, Medicine_delete.class);
                                    in.putExtra("item_code", item_code);

                                    context.startActivity(in);
                                }catch (Exception e) {
                                    // TODO: handle exception
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                // No button clicked
                                // do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete medicine "+My_cart_get.item_name()+"?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

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