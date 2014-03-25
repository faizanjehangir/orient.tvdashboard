package com.tvdashboard.channelsetup;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Channel {

	private String channelNum;
	private String channelName;
	private Bitmap channelIcon;	
	
	public Channel() {
	}
	
	public Channel(String channelNum, String channelName,
			Bitmap channelIcon) {
		super();
		this.channelNum = channelNum;
		this.channelName = channelName;
		this.channelIcon = channelIcon;
	}
	public String getChannelNum() {
		return channelNum;
	}
	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Bitmap getChannelIcon() {
		return channelIcon;
	}
	public void setChannelIcon(Bitmap channelIcon) {
		this.channelIcon = channelIcon;
	}
	
	
	
}
