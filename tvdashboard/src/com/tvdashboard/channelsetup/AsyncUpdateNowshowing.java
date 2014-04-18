package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.HashMap;

import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;

import android.os.AsyncTask;

public class AsyncUpdateNowshowing extends AsyncTask<Void, String, Void> {

	private NowShowingAdapter imgTaskAdapter;
	private ArrayList<Show> nowPlayingShows;
	public static boolean isNowShowingRunning = false;
	HashMap<Show, ChannelSchedule> showSchedule;
	private Show newShow = null;
	private boolean showComplete = false;
	private int showPos = 0;

	public AsyncUpdateNowshowing(NowShowingAdapter adapter,
			ArrayList<Show> nowPlayingShows,
			HashMap<Show, ChannelSchedule> showSchedule) {
		imgTaskAdapter = adapter;
		this.nowPlayingShows = nowPlayingShows;
		isNowShowingRunning = true;
		this.showSchedule = showSchedule;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		while (isNowShowingRunning) {
			String[] showArr = new String[this.nowPlayingShows.size()];
			for (int i = 0; i < this.nowPlayingShows.size(); ++i) {
				// get current show duration
				int prog = ScheduleManager
						.getCurrentPlayingShowStatus(this.nowPlayingShows
								.get(i));
				if (prog > 100) {
					this.showComplete = true;
					this.showPos = i;
					this.newShow = ScheduleManager
							.getNowPlayingShow(ScheduleManager.schedules
									.get(this.showSchedule
											.get(this.nowPlayingShows.get(i))));
					prog = 0;
				}
				showArr[i] = String.valueOf(prog);

			}
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
		imgTaskAdapter.resetProgress(0);
		if (this.showComplete){
			imgTaskAdapter.setNewShowAdded(true);
			imgTaskAdapter.setNowPlayingShow(this.showPos, this.newShow);
			this.showComplete = false;
		}
		else
			imgTaskAdapter.setNewShowAdded(false);
		for (int i = 0; i < values.length; ++i) {
			imgTaskAdapter.addProgress(Integer.valueOf(values[i]));
		}
		imgTaskAdapter.notifyDataSetChanged();
		super.onProgressUpdate(values);
	}

}
