package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.List;

import com.mstar.tv.service.aidl.EN_MEMBER_SERVICE_TYPE;
import com.mstar.tv.service.interfaces.ITvServiceServer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;
import com.tvos.common.TvManager;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.TvOsType.EnumInputSource;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateTVGuideAdapter extends BaseAdapter {

	private Context mContext;
	private List<Show> shows;
	LayoutInflater inflater;
	private Channel channel;
	private int progress;
	private boolean updateNowPlaying;

	public UpdateTVGuideAdapter(Context context, List<Show> shows,
			Channel channel) {
		this.mContext = context;
		this.shows = shows;
		this.channel = channel;
		this.updateNowPlaying = false;
		inflater = (LayoutInflater) this.mContext
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateShows(Show nowComing, List<Show> upComing) {
		this.shows = new ArrayList<Show>();
		this.shows.add(nowComing);
		for (int i = 0; i < upComing.size(); ++i)
			this.shows.add(upComing.get(i));
	}

	// ArrayList<String> itemList = new ArrayList<String>();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.shows.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUpdateNowPlayingBool(boolean flag) {
		this.updateNowPlaying = flag;
	}

	public void updateNowPlayingProgress(int prog) {
		this.progress = prog;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	static class ViewHolder {

		public TextView showTitle;
		public TextView showDuration;
		public ImageButton image;
		public ProgressBar progBar;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.gridview_tvguide, null);
			holder = new ViewHolder();
			holder.image = (ImageButton) convertView.findViewById(R.id.image);
			holder.showTitle = (TextView) convertView
					.findViewById(R.id.ShowTitle);
			holder.showDuration = (TextView) convertView
					.findViewById(R.id.ShowTime);
			holder.progBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			convertView.setTag(holder);

			holder.image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.v("channel", channel.getChannelName());
					ComponentName localComponentName = new ComponentName(
							"mstar.tvsetting.ui",
							"mstar.tvsetting.ui.RootActivity");

					try {
						TvManager
								.setInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
						ITvServiceServer localITvServiceServer = ITvServiceServer.Stub
								.asInterface(ServiceManager
										.checkService("tv_services"));
						try {
							localITvServiceServer.getCommonManager();
							localITvServiceServer
									.getChannelManager()
									.programSel(
											Integer.valueOf(channel
													.getChannelNum()),
											EN_MEMBER_SERVICE_TYPE.E_SERVICETYPE_ATV);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.v("current channel", String.valueOf(TvManager
								.getChannelManager().getCurrChannelNumber()));
						Log.v("input source main:", TvManager
								.getCurrentMainInputSource().toString());
						Log.v("input source:", TvManager
								.getCurrentInputSource().toString());
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent localIntent = new Intent(
							"android.intent.action.MAIN");
					localIntent.addCategory("android.intent.category.LAUNCHER");
					localIntent.setComponent(localComponentName);
					localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(localIntent);
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			holder.progBar.setVisibility(View.VISIBLE);
		} else {
			holder.progBar.setVisibility(View.GONE);
		}

		if (this.updateNowPlaying) {
			holder.progBar.setProgress(this.progress);
		}

		ImageLoader.getInstance().displayImage(
				this.shows.get(position).getShowThumb(), holder.image);
		holder.showTitle.setText(this.shows.get(position).getShowTitle());
		holder.showDuration.setText(this.shows.get(position).getShowTime());

		return convertView;
	}
}
