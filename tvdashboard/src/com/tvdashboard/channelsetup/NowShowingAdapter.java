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
import android.widget.ProgressBar;
import android.widget.TextView;

public class NowShowingAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Show> nowShowingList;
	LayoutInflater inflater;
	private ArrayList<Channel> channelList;
	private ArrayList<Integer> progress;
	private boolean newShowAdded;

	public NowShowingAdapter(Context context, ArrayList<Show> nowShowing,
			ArrayList<Channel> channelList) {
		this.mContext = context;
		this.nowShowingList = nowShowing;
		this.channelList = channelList;
		this.progress = new ArrayList<Integer>();
		resetProgress(nowShowing.size());
		inflater = (LayoutInflater) this.mContext
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.newShowAdded = true;
	}

	public void resetProgress(int size) {
		this.progress = new ArrayList<Integer>();
		for (int i = 0; i < size; ++i)
			this.progress.add(0);
	}

	public void setNowPlayingShow(int pos, Show show) {
		this.nowShowingList.set(pos, show);
	}
	
	public void setNewShowAdded(boolean flag){
		this.newShowAdded = flag;
	}

	public void addProgress(int prog) {
		this.progress.add(prog);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.nowShowingList.size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_nowshowing, null);
		}

		ImageView imgView = (ImageView) convertView
				.findViewById(R.id.ChannelLogo);
		ImageButton imgBtn = (ImageButton) convertView.findViewById(R.id.image);
		TextView showTitle = (TextView) convertView
				.findViewById(R.id.ShowTitle);
		TextView showDuration = (TextView) convertView
				.findViewById(R.id.ShowTime);
		ProgressBar prgBar = (ProgressBar) convertView
				.findViewById(R.id.progressBar);

		prgBar.setProgress(this.progress.get(position));
		if (this.newShowAdded) {
			imgView.setImageBitmap(this.channelList.get(position)
					.getChannelIcon());
			ImageLoader.getInstance().displayImage(
					this.nowShowingList.get(position).getShowThumb(), imgBtn);
			showTitle.setText(this.nowShowingList.get(position).getShowTitle());
			showDuration.setText(this.nowShowingList.get(position)
					.getShowTime());
		}

		return convertView;
	}

}
