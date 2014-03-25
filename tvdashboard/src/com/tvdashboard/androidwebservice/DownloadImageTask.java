package com.tvdashboard.androidwebservice;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tvdashboard.main.FragmentTvGuide;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	private ImageLoader imageLoader;
	private Show imageForShow;
	private Context context;
	private boolean isNowPlaying;
	private ChannelSchedule schedule;

	public DownloadImageTask(Context context, ImageLoader imageLoader,
			Show show, ChannelSchedule schedule, boolean isNowPlaying) {
		this.imageLoader = imageLoader;
		this.imageForShow = show;
		ScheduleManager.imageDownloaderTasks++;
		this.context = context;
		this.isNowPlaying = isNowPlaying;
		this.schedule = schedule;
	}

	protected void onPreExecute() {
		if (ScheduleManager.prgDiag == null) {
			ScheduleManager.prgDiag = new ProgressDialog(context);
			ScheduleManager.prgDiag.setTitle("Processing...");
			ScheduleManager.prgDiag.setMessage("Please wait.");
			ScheduleManager.prgDiag.setCancelable(false);
			ScheduleManager.prgDiag.setIndeterminate(true);
			ScheduleManager.prgDiag.show();
		}

		// mDialog = ProgressDialog.show(ChartActivity.this,"Please wait...",
		// "Retrieving data ...", true);
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			mIcon11 = imageLoader.loadImageSync(urldisplay);
		} catch (Exception e) {
			Log.e("Error", "image download error");
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		// add to show
		ScheduleManager.getBmpImages().put(this.imageForShow, result);
		
		if (this.isNowPlaying){
			ScheduleManager.AddShowToCurrentMedia(this.imageForShow, this.schedule, isNowPlaying);
		}
		else{
			ScheduleManager.AddShowToCurrentMedia(this.imageForShow, this.schedule, false);
		}

		ScheduleManager.imageDownloaderTasks--;
		if (ScheduleManager.imageDownloaderTasks == 0) {
			ScheduleManager.prgDiag.dismiss();
			//display the data in the tiles
			ScheduleManager.RenderSchedulesOnDash();
		}
		// close
		// mDialog.dismiss();
	}
}
