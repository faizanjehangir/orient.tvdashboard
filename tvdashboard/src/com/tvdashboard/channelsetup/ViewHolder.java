package com.tvdashboard.channelsetup;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewHolder {
	
	private TextView showTitle;
	private TextView showDuration;
	private ImageButton image;
	private ProgressBar pb;
	public ViewHolder(TextView showTitle, TextView showDuration,
			ImageButton image, ProgressBar pb) {
		super();
		this.showTitle = showTitle;
		this.showDuration = showDuration;
		this.image = image;
		this.pb = pb;
	}
	public TextView getShowTitle() {
		return showTitle;
	}
	public void setShowTitle(TextView showTitle) {
		this.showTitle = showTitle;
	}
	public TextView getShowDuration() {
		return showDuration;
	}
	public void setShowDuration(TextView showDuration) {
		this.showDuration = showDuration;
	}
	public ImageButton getImage() {
		return image;
	}
	public void setImage(ImageButton image) {
		this.image = image;
	}
	public ProgressBar getPb() {
		return pb;
	}
	public void setPb(ProgressBar pb) {
		this.pb = pb;
	}
	
	

}
