package com.tvdashboard.androidwebservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.main.FragmentTvGuide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateMediaTask extends AsyncTask<String, Integer, Integer> {

	ImageButton[] btnImgNowPlaying;
	ImageButton[] btnImgUpComing;
	ProgressBar[] progBar;
	TextView[] showTitleNowPlaying;
	TextView[] showTitleUpComing;
	TextView[] showDuration;
	HashMap<Show, ChannelSchedule> nowPlayingMediaSet;
	HashMap<Show, ChannelSchedule> upComingMediaSet;
	Integer[] mProgressStatus;
	static volatile int currentProgressBarIndex;
	boolean newShowAdded = false;
	ChannelSchedule newSchedule;
	Show newShow;
	HashMap<Show, ChannelSchedule> newScheduleAdded;
	Activity currActivity;

	public UpdateMediaTask(Activity currActivity, ImageButton[] nowPlayingViews,
			ImageButton[] btnUpComing,
			HashMap<Show, ChannelSchedule> nowPlayingMediaSet,
			HashMap<Show, ChannelSchedule> upComingMediaSet,
			TextView[] showDuration, TextView[] showTitleNowPlaying,
			TextView[] showTitleUpComing, ProgressBar[] progBarArray) {
		this.btnImgNowPlaying = nowPlayingViews;
		this.btnImgUpComing = btnUpComing;
		this.nowPlayingMediaSet = nowPlayingMediaSet;
		this.upComingMediaSet = upComingMediaSet;
		this.mProgressStatus = new Integer[nowPlayingMediaSet.size()];
		this.progBar = progBarArray;
		this.showTitleNowPlaying = showTitleNowPlaying;
		this.showTitleUpComing = showTitleUpComing;
		this.showDuration = showDuration;
		this.newScheduleAdded = new HashMap<Show, ChannelSchedule>();
		this.currActivity = currActivity;
	}

	@Override
	protected Integer doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		// display the images in now playing
		while (ScheduleManager.isScheduleRunning) {
			UpdateNowPlayingMedia();
			UpdateUpComingMedia();
			// sleep 1 second to show the progress
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void UpdateUpComingMedia() {
		Iterator iterator = upComingMediaSet.entrySet().iterator();
		// set images on now playing
		for (int i = 0; i < btnImgUpComing.length && iterator.hasNext(); ++i) {
			Map.Entry mEntry = (Map.Entry) iterator.next();
			Show mShowUpComing = (Show) mEntry.getKey();
			Show mCurrentUpcoming = ScheduleManager
					.getNowPlayingShow(upComingMediaSet.get(mShowUpComing)
							.getListOfShows());

			// check if both are equal for a schedule
			if (mShowUpComing.getShowTitle().equals(
					mCurrentUpcoming.getShowTitle())
					&& mShowUpComing.getShowTime().equals(
							mCurrentUpcoming.getShowTime())) {
				this.newSchedule = (ChannelSchedule) mEntry.getValue();
				// get now playing show and replace it
				this.newShow = ScheduleManager.getUpComingShow(newSchedule
						.getListOfShows());
				iterator.remove();
				// add to temp hashmap
				this.newScheduleAdded.put(this.newShow, this.newSchedule);

				final int index = i;
				this.currActivity.runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ImageLoader iLoader = ImageLoader.getInstance();
								try {
									iLoader.displayImage(
											newShow.getShowThumb(),
											btnImgUpComing[index]);
									showTitleUpComing[index].setText(newShow
											.getShowTitle());
								} catch (Exception e) {
									Log.e("Error", "image download error");
									Log.e("Error", e.getMessage());
									e.printStackTrace();
								}
							}
						});
				this.newShowAdded = true;
			}
		}
		if (newShowAdded) {
			Iterator iter = this.newScheduleAdded.keySet().iterator();
			while (iter.hasNext()) {
				Show key = (Show) iter.next();
				ChannelSchedule val = (ChannelSchedule) this.newScheduleAdded
						.get(key);
				this.upComingMediaSet.put(key, val);
			}
			// reset temp new schedules
			this.newScheduleAdded = new HashMap<Show, ChannelSchedule>();
			newShowAdded = false;
		}
	}

	private void UpdateNowPlayingMedia() {
		Iterator iterator = nowPlayingMediaSet.entrySet().iterator();
		// set images on now playing
		for (int i = 0; i < btnImgNowPlaying.length && iterator.hasNext(); ++i) {
			Map.Entry mEntry = (Map.Entry) iterator.next();
			Show mShowNowPlaying = (Show) mEntry.getKey();
			// get show status
			mProgressStatus[i] = ScheduleManager
					.getCurrentPlayingShowStatus(mShowNowPlaying);

			if (mProgressStatus[i] > 100) {
				this.newSchedule = (ChannelSchedule) mEntry.getValue();
				// get now playing show and replace it
				this.newShow = ScheduleManager.getNowPlayingShow(newSchedule
						.getListOfShows());
				iterator.remove();

				// add to temp hashmap
				this.newScheduleAdded.put(this.newShow, this.newSchedule);

				final int index = i;
				this.currActivity.runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ImageLoader iLoader = ImageLoader.getInstance();
								try {
									iLoader.displayImage(
											newShow.getShowThumb(),
											btnImgNowPlaying[index]);
									showTitleNowPlaying[index].setText(newShow
											.getShowTitle());
								} catch (Exception e) {
									Log.e("Error", "image download error");
									Log.e("Error", e.getMessage());
									e.printStackTrace();
								}
							}
						});
				mProgressStatus[i] = 0;
				this.newShowAdded = true;
			}
			// Update the progress bar

		}
		if (newShowAdded) {
			Iterator iter = this.newScheduleAdded.keySet().iterator();
			while (iter.hasNext()) {
				Show key = (Show) iter.next();
				ChannelSchedule val = (ChannelSchedule) this.newScheduleAdded
						.get(key);
				this.nowPlayingMediaSet.put(key, val);
			}
			// reset temp new schedules
			this.newScheduleAdded = new HashMap<Show, ChannelSchedule>();
			newShowAdded = false;
		}
		publishProgress(mProgressStatus);
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub

		super.onPostExecute(result);
		
		Log.v("task ended", "ended");
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		// TODO Auto-generated method stub
		for (int i = 0; i < progress.length; ++i)
			this.progBar[i].setProgress(progress[i]);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		super.onPreExecute();
	}

}
