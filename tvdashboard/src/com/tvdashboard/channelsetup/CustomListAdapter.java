package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.androidwebservice.UpdateMediaTask;
import com.tvdashboard.database.R;
import com.tvdashboard.main.FragmentTvGuide;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListAdapter extends BaseAdapter implements OnClickListener {

	/*********** Declare Used Variables *********/
	private Activity activity;
	private ArrayList<Channel> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	Channel tempValues = null;
	public TextView channelNum;
	public TextView channelTitle;
	public ImageView image;
	int i = 0;

	/************* CustomAdapter Constructor *****************/
	public CustomListAdapter(Activity a, ArrayList<Channel> d,
			Resources resLocal) {

		/********** Take passed values **********/
		activity = a;
		data = d;
		res = resLocal;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/******** What is the size of Passed Arraylist Size ************/
	@Override
	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			/******
			 * Inflate tabitem.xml file for each row ( Defined below )
			 *******/
			vi = inflater
					.inflate(R.layout.channelsetup_list_row, parent, false);
		}

		channelNum = (TextView) vi.findViewById(R.id.ChannelNum);
		channelTitle = (TextView) vi.findViewById(R.id.ChannelTitle);
		image = (ImageView) vi.findViewById(R.id.ChannelIcon);
		channelNum.setText(data.get(position).getChannelNum());
		channelTitle.setText(data.get(position).getChannelName());
		image.setImageBitmap(data.get(position).getChannelIcon());

		/******** Set Item Click Listner for LayoutInflater for each row *******/
		vi.setOnClickListener(new OnItemClickListener(position));

		return vi;
	}

	@Override
	public void onClick(View v) {
		Log.v("CustomAdapter", "=====Row button clicked=====");
	}

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			ScheduleManager.isScheduleRunning = false;

			Toast.makeText(activity, String.valueOf(mPosition),
					Toast.LENGTH_SHORT).show();
			// get now playing media
			Channel ch = data.get(mPosition);
			ChannelSchedule chSchedule = null;
			// get channel schedule
			Iterator itChannels = ScheduleManager.channelData.entrySet()
					.iterator();
			while (itChannels.hasNext()) {
				Map.Entry mEntry = (Map.Entry) itChannels.next();
				Channel mChannel = (Channel) mEntry.getValue();
				// check for the channel name selected
				if (mChannel.getChannelName().equals(ch.getChannelName())) {
					// get schedule of the selected channel
					chSchedule = (ChannelSchedule) mEntry.getKey();
					break;
				}
			}

			// for (int index = 0; index < FragmentTvGuideMain.upComingSize;
			// ++index)
			// FragmentTvGuideMain.rlImages[index].setVisibility(View.GONE);

			// get list of shows from the schedule if not null
			if (chSchedule != null) {
				List<Show> shows = ScheduleManager.schedules.get(chSchedule);
				// get now playing and all upcoming shows
				Show nowPlaying = ScheduleManager.getNowPlayingShow(shows);
				// ImageLoader.getInstance().displayImage(nowPlaying.getShowThumb(),
				// FragmentTvGuideMain.imgBtnNowPlaying[0]);
				// FragmentTvGuideMain.txtNowPlayingTitle[0].setText(nowPlaying.getShowTitle());
				// FragmentTvGuideMain.rlImages[0].setVisibility(View.VISIBLE);

				List<GVShow> lstGVShow = new ArrayList<GVShow>();
				GVShow gvShow = new GVShow();
				gvShow.setShowImage(nowPlaying.getShowThumb());
				gvShow.setShowName(nowPlaying.getShowTitle());
				lstGVShow.add(gvShow);

				List<Show> upComingShows = ScheduleManager
						.GetAllUpComingShows(shows);
				// display upcoming details:
				for (int j = 0; j < upComingShows.size(); ++j) {
					// ImageLoader.getInstance().displayImage(upComingShows.get(i).getShowThumb(),
					// FragmentTvGuideMain.imgBtnUpComing[i]);
					// FragmentTvGuideMain.txtUpComingTitle[i].setText(upComingShows.get(i).getShowTitle());
					// FragmentTvGuideMain.rlImages[j].setVisibility(View.VISIBLE);
					GVShow gvObj = new GVShow();
					gvObj.setShowImage(upComingShows.get(j).getShowThumb());
					gvObj.setShowName(upComingShows.get(j).getShowTitle());
					lstGVShow.add(gvObj);
				}
				FragmentTvGuideMain.gridView.setAdapter(new GridViewAdapter(
						FragmentTvGuideMain.context.getApplicationContext(),
						lstGVShow));

				int gvSize = upComingShows.size();
				TextView[] txtNowPlayingTitle = new TextView[1];
				ImageButton[] imgBtnNowPlaying = new ImageButton[1];
				TextView[] txtNowPlayingDuration = new TextView[1];
				ProgressBar[] prgBar = new ProgressBar[1];
				
//				imgBtnNowPlaying[0] = FragmentTvGuideMain.viewHolder.get(0).getImage();
//				txtNowPlayingTitle[0] = FragmentTvGuideMain.viewHolder.get(0).getShowTitle();
				
//				View rlNowPlaying = FragmentTvGuideMain.gridView
//						.getChildAt(0);
//				imgBtnNowPlaying[0] = (ImageButton) ((ViewGroup) rlNowPlaying).getChildAt(0);
				
				
//				txtNowPlayingTitle[0] = (TextView) rlNowPlaying.getChildAt(2);
//				txtNowPlayingDuration[0] = (TextView) rlNowPlaying
//						.getChildAt(3);
//				prgBar[0] = (ProgressBar) rlNowPlaying.getChildAt(4);
//
//				TextView[] txtUpComingTitle = new TextView[gvSize];
//				ImageButton[] imgBtnUpComing = new ImageButton[gvSize];
//
//				for (int i = 1; i < gvSize; ++i) {
//					RelativeLayout rl = (RelativeLayout) FragmentTvGuideMain.gridView
//							.getChildAt(i);
//					imgBtnUpComing[i - 1] = (ImageButton) rl.getChildAt(0);
//					txtUpComingTitle[i - 1] = (TextView) rl.getChildAt(2);
//				}
//
//				// txtNowPlayingTitle[0] = (TextView)
//				// FragmentTvGuideMain.gridView.getChildAt(index)
//
//				HashMap<Show, ChannelSchedule> nowPlayingSet = new HashMap<Show, ChannelSchedule>();
//				HashMap<Show, ChannelSchedule> upComingSet = new HashMap<Show, ChannelSchedule>();
//				nowPlayingSet.put(nowPlaying, chSchedule);
//				upComingSet.put(upComingShows.get(0), chSchedule);
//
//				// start the scheduler
//				ScheduleManager.isScheduleRunning = true;
//
//				// execute nowplaying async task
//				new UpdateMediaTask(FragmentTvGuideMain.GetParentActivity(),
//						imgBtnNowPlaying, imgBtnUpComing, nowPlayingSet,
//						upComingSet, txtNowPlayingDuration, txtNowPlayingTitle,
//						txtUpComingTitle, prgBar).execute();
			}
		}
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