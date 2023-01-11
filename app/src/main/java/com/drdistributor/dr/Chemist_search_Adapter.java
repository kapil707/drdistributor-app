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

public class Chemist_search_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private List<Chemist_search_get_or_set> movieItems;

    public Chemist_search_Adapter(Context context, List<Chemist_search_get_or_set> arraylist)
    {
        this.context = context;
        this.movieItems = arraylist;
    }
 
    @Override
	public int getCount() {
		return movieItems.size();
	}
    
   
    @Override
	public View getView(int position, View view, ViewGroup parent) 
    {
		// TODO Auto-generated method stub
       
        LayoutInflater abc = ((Activity) context).getLayoutInflater();
		View itemView = abc.inflate(R.layout.chemist_search_item, null,true);
		final Chemist_search_get_or_set Select_chemist_get = movieItems.get(position);

        ImageView chemist_image = itemView.findViewById(R.id.chemist_image);
        TextView chemist_name = itemView.findViewById(R.id.chemist_name);
        TextView chemist_altercode = itemView.findViewById(R.id.chemist_altercode);
        TextView chemist_message = itemView.findViewById(R.id.chemist_message);

        Picasso.with(context).load(Select_chemist_get.chemist_image()).into(chemist_image);
        chemist_name.setText(Select_chemist_get.chemist_name());
        chemist_altercode.setText("Code : "+Select_chemist_get.chemist_altercode());

        chemist_message.setVisibility(View.GONE);
        if(!Select_chemist_get.user_cart().equals("0"))
        {
            chemist_message.setVisibility(View.VISIBLE);
            chemist_message.setText(Html.fromHtml("Order "+Select_chemist_get.user_cart()+" items | Total ₹ "+Select_chemist_get.user_cart_total()+"/-"));
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