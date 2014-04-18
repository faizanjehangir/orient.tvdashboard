package com.tvdashboard.channelsetup;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.channelsetup.UpdateTVGuideAdapter.ViewHolder;
import com.tvdashboard.database.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpcomingAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Show> upComingShows;
	LayoutInflater inflater;
	private ArrayList<Channel> channelList;
	private boolean isTvGuideUpcoming;
	
	public UpcomingAdapter(Context context, ArrayList<Show> upComingShows, ArrayList<Channel> channelList, boolean isTvGuideUpcoming){
		this.mContext = context;
		this.upComingShows = upComingShows;
		this.channelList = channelList;
		this.isTvGuideUpcoming = isTvGuideUpcoming;
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
	
	static class ViewHolder {

		public ImageView channelLogo;
		public TextView showTitle;
		public TextView showTime;
		public ImageButton image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
        if (convertView == null) {
        	convertView = inflater.inflate(R.layout.gridview_upcoming, null);
            holder = new ViewHolder();
            holder.channelLogo = (ImageView) convertView.findViewById(R.id.ChannelLogo);
			holder.image = (ImageButton) convertView.findViewById(R.id.image);
			holder.showTitle = (TextView) convertView
					.findViewById(R.id.ShowTitle);
			holder.showTime = (TextView) convertView
					.findViewById(R.id.ShowTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
          
        if (!isTvGuideUpcoming)
        	holder.channelLogo.setImageBitmap(this.channelList.get(position).getChannelIcon());
        
        ImageLoader.getInstance().displayImage(this.upComingShows.get(position).getShowThumb(), holder.image);
        holder.showTitle.setText(this.upComingShows.get(position).getShowTitle());
        holder.showTime.setText(this.upComingShows.get(position).getShowTime());
        
        return convertView;
    }

}
