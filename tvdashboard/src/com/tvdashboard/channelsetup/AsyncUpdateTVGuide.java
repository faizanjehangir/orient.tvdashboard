package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;

import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;

public class AsyncUpdateTVGuide extends AsyncTask<Void, String, Void> {

	private UpdateTVGuideAdapter tvGuideTaskAdapter;
	private Show nowPlayingShow;
	public static boolean isTVGuideRunning = false;
	private ChannelSchedule showSchedule;
	private Show newShow = null;
	private boolean showComplete = false;
	private List<Show> upcomingShows;

	public AsyncUpdateTVGuide(UpdateTVGuideAdapter adapter,
			Show nowPlayingShow, ChannelSchedule showSchedule) {
		tvGuideTaskAdapter = adapter;
		this.nowPlayingShow = nowPlayingShow;
		isTVGuideRunning = true;
		this.showSchedule = showSchedule;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		this.tvGuideTaskAdapter.setUpdateNowPlayingBool(true);
		// TODO Auto-generated method stub
		while (isTVGuideRunning) {
			String[] showArr = new String[1];
			// get current show duration
			int prog = ScheduleManager
					.getCurrentPlayingShowStatus(this.nowPlayingShow);
			if (prog > 100) {
				this.showComplete = true;
				this.newShow = ScheduleManager
						.getNowPlayingShow(ScheduleManager.schedules
								.get(this.showSchedule));
				this.upcomingShows = ScheduleManager
						.GetAllUpComingShows(ScheduleManager.schedules
								.get(this.showSchedule));
				prog = 0;
			}
			showArr[0] = String.valueOf(prog);
			publishProgress(showArr);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		if (this.showComplete) {
			tvGuideTaskAdapter.setUpdateNowPlayingBool(false);
			tvGuideTaskAdapter.updateShows(this.nowPlayingShow, this.upcomingShows);
			this.showComplete = false;			
		}
		else
			tvGuideTaskAdapter.setUpdateNowPlayingBool(true);
		tvGuideTaskAdapter.updateNowPlayingProgress(Integer.valueOf(values[0]));
		tvGuideTaskAdapter.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}

}
