package com.tvdashboard.channelsetup;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UpcomingAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Show> upComingShows;
	LayoutInflater inflater;
	private ArrayList<Channel> channelList;
	
	public UpcomingAdapter(Context context, ArrayList<Show> upComingShows, ArrayList<Channel> channelList){
		this.mContext = context;
		this.upComingShows = upComingShows;
		this.channelList = channelList;
		inflater = (LayoutInflater) this.mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
//	ArrayList<String> itemList = new ArrayList<String>();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.upComingShows.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_upcoming, null);
        }
        
        ImageView imgView = (ImageView) convertView.findViewById(R.id.ChannelLogo);
        ImageButton imgBtn = (ImageButton) convertView.findViewById(R.id.image);
        TextView showTitle = (TextView) convertView.findViewById(R.id.ShowTitle);
        TextView showDuration = (TextView) convertView.findViewById(R.id.ShowTime);
        
        imgView.setImageBitmap(this.channelList.get(position).getChannelIcon());
        ImageLoader.getInstance().displayImage(this.upComingShows.get(position).getShowThumb(), imgBtn);
        showTitle.setText(this.upComingShows.get(position).getShowTitle());
        showDuration.setText(this.upComingShows.get(position).getShowTime());
        
        return convertView;
    }

}
