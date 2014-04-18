package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.utility.CombinedImageLoadingListener;
import com.tvdashboard.androidwebservice.UpdateMediaTask;
import com.tvdashboard.database.R;
import com.tvdashboard.main.FragmentTvGuide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
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

	public static int selPosition;

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener {
		int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

//			FragmentTvGuideMain.rlNowPlaying.setVisibility(View.GONE);
			AsyncUpdateTVGuide.isTVGuideRunning = false;
			ScheduleManager.isScheduleRunning = false;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Toast.makeText(activity, String.valueOf(mPosition),
					Toast.LENGTH_SHORT).show();

			selPosition = mPosition;

			Channel ch = data.get(mPosition);
			// if same channel is selected again
			if (FragmentTvGuideMain.selectedChannel != null) {
				if (FragmentTvGuideMain.selectedChannel.equals(ch
						.getChannelName()) && !FragmentTvGuideMain.dateChanged)
					return;
			}

			// get now playing media
			ChannelSchedule chSchedule = null;
			// get channel schedule
			Iterator itChannels = null;

			if (FragmentTvGuideMain.isTodayDate)
				itChannels = ScheduleManager.channelData.entrySet().iterator();
			else {
				ScheduleManager.getScheduleByDate(
						FragmentTvGuideMain.selectedDate, ch);
				return;
			}
			while (itChannels.hasNext()) {
				Map.Entry mEntry = (Map.Entry) itChannels.next();
				Channel mChannel = (Channel) mEntry.getValue();
				// check for the channel name selected
				if (mChannel.getChannelName().equals(ch.getChannelName())) {
					// get schedule of the selected channel
					chSchedule = (ChannelSchedule) mEntry.getKey();
					FragmentTvGuideMain.selectedChannel = ch.getChannelName();
					break;
				}
			}

			// get list of shows from the schedule if not null
			if (chSchedule != null) {

				// start a progress bar
				ScheduleManager.prgDiag = new ProgressDialog(
						FragmentTvGuideMain.context);
				ScheduleManager.prgDiag.setTitle("Processing...");
				ScheduleManager.prgDiag.setMessage("Please wait.");
				ScheduleManager.prgDiag.setCancelable(false);
				ScheduleManager.prgDiag.setIndeterminate(true);

				List<Show> shows = ScheduleManager.schedules.get(chSchedule);
				// get now playing and all upcoming shows
				Show nowPlaying = ScheduleManager.getNowPlayingShow(shows);
				List<Show> upComingShows = ScheduleManager
						.GetAllUpComingShows(shows);

				ArrayList<Show> completeShows = new ArrayList<Show>();
				completeShows.add(nowPlaying);
				for (int j = 0; j < upComingShows.size(); ++j){
					completeShows.add(upComingShows.get(j));
				}

				GridView gv = (GridView) FragmentTvGuideMain
						.GetParentActivity().findViewById(R.id.gridView);
				UpdateTVGuideAdapter adapter = new UpdateTVGuideAdapter(
						FragmentTvGuideMain.GetParentActivity(), completeShows,
						ch);
				gv.setAdapter(adapter);
				AsyncUpdateTVGuide task = new AsyncUpdateTVGuide(adapter,
						nowPlaying, chSchedule);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else
					task.execute();
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