package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateTVGuideAdapter extends BaseAdapter {

	private Context mContext;
	private List<Show> upComingShows;
	private Show nowPlaying;
	LayoutInflater inflater;
	private Channel channel;
	private boolean updateNowPlaying;
	private int progress;

	public UpdateTVGuideAdapter(Context context,
			List<Show> upComingShows, Channel channel, Show nowPlaying) {
		this.mContext = context;
		this.upComingShows = upComingShows;
		this.channel = channel;
		this.nowPlaying = nowPlaying;
		inflater = (LayoutInflater) this.mContext
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.updateNowPlaying = false;
	}
	
	public void updateNowPlaying(Show show){
		this.nowPlaying = show;
	}
	
	public void updateUpcoming(List<Show> shows){
		this.upComingShows = shows;
	}
	
	public void setProgress(int prog){
		this.progress = prog;
	}

	public void setUpdateFlag(boolean flag){
		this.updateNowPlaying = flag;
	}
	// ArrayList<String> itemList = new ArrayList<String>();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.upComingShows.size() + 1;
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
			convertView = inflater.inflate(R.layout.gridview_tvguide, null);
		}

		ImageButton imgBtn = (ImageButton) convertView.findViewById(R.id.image);
		TextView showTitle = (TextView) convertView
				.findViewById(R.id.ShowTitle);
		TextView showDuration = (TextView) convertView
				.findViewById(R.id.ShowTime);
		ProgressBar progBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
		
		if (this.updateNowPlaying){
			progBar.setProgress(this.progress);
			return convertView;
		}
		if (position == 0){
			ImageLoader.getInstance().displayImage(
					this.nowPlaying.getShowThumb(), imgBtn);
			showTitle.setText(this.nowPlaying.getShowTitle());
			showDuration.setText(this.nowPlaying.getShowTime());
		}
		else{
			position--;
			progBar.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					this.upComingShows.get(position).getShowThumb(), imgBtn);
			showTitle.setText(this.upComingShows.get(position).getShowTitle());
			showDuration.setText(this.upComingShows.get(position).getShowTime());
		}
		return convertView;
	}
}
