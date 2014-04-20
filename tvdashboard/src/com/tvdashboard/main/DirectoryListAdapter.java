package com.tvdashboard.main;

import java.util.ArrayList;

import com.orient.menu.directorylist.DirectoryItems;
import com.tvdashboard.database.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectoryListAdapter extends ArrayAdapter<DirectoryItems>{
	private ArrayList<DirectoryItems> entries;
	private Activity activity;

	public DirectoryListAdapter(Activity a, int textViewResourceId,	ArrayList<DirectoryItems> entries) {
		super(a, textViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
	}

	public static class ViewHolder {
		public ImageView icon;
		public TextView item;
	}
	
	

	@Override
	public int getCount() {
		return super.getCount();
	}



	@Override
	public DirectoryItems getItem(int position) {
		return super.getItem(position);
	}



	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.dir_list_row, null);
			holder = new ViewHolder();
			holder.icon = (ImageView)v.findViewById(R.id.icon);
			holder.item = (TextView) v.findViewById(R.id.title);
			v.setTag(holder);
		} else
			holder = (ViewHolder) v.getTag();
		
//			file = new File(Environment.getExternalStorageDirectory().toString());
//			File temp_file = new File(file, entries.get(position).getTitle());

//			if (!temp_file.isFile()) {
//				holder.icon.setImageResource(R.drawable.folder);
//			}
//			else
//			{
//				holder.icon.setImageResource(R.drawable.file);
//			}
			holder.icon.setImageResource(R.drawable.folder);
			holder.item.setText(entries.get(position).getTitle());
		
		return v;
	}

}