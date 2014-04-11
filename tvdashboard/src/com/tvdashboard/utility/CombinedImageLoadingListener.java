package com.tvdashboard.utility;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class CombinedImageLoadingListener extends SimpleImageLoadingListener {

    private int imageCount;
    private ProgressDialog progressDialog;

    public CombinedImageLoadingListener(int imageCount, ProgressDialog progressDialog) {
        this.imageCount = imageCount;
        this.progressDialog = progressDialog;
    }

    @Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		// TODO Auto-generated method stub
    	imageCount--;
        if (imageCount == 0) {
        	progressDialog.dismiss();
        }
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// TODO Auto-generated method stub
		imageCount--;
        if (imageCount == 0) {
        	progressDialog.dismiss();
        }
	}
}