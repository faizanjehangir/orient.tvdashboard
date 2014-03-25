package com.tvdashboard.androidwebservice;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

public class ScheduleListnerTask extends AsyncTask<String, Void, Bitmap> {

	ImageButton btnImage;
	public ScheduleListnerTask(ImageButton btnImage){
		this.btnImage = btnImage;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		String urldisplay = params[0];
		Bitmap mIcon11 = null;
		ImageLoader iLoader = ImageLoader.getInstance();
		try {
			mIcon11 = iLoader.loadImageSync(urldisplay);
		} catch (Exception e) {
			Log.e("Error", "image download error");
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		btnImage.setImageBitmap(result);
	}
}
