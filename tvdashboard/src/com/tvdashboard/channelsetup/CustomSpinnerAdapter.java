package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.database.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String>{
	
	private Context activity;
    private List<String> data;
    public Resources res;
    LayoutInflater inflater;
    
    public CustomSpinnerAdapter(Context context, int textViewResourceId, List<String> values, Resources resLocal ) 
    {
    	super (context, textViewResourceId, values);
    	activity = context;
    	data     = values;
    	res      = resLocal;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {
 
        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_region_row, parent, false);
         
        /***** Get each Model object from Arraylist ********/
//        tempValues = null;
//        tempValues = (SpinnerModel) data.get(position);
         
        TextView name = (TextView)row.findViewById(R.id.name);
        ImageView image = (ImageView)row.findViewById(R.id.image);
        name.setText(data.get(position));
        
        return row;
      }

}
