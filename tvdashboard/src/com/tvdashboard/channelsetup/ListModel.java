package com.tvdashboard.channelsetup;

import android.widget.ImageView;

public class ListModel {

	private String channelNum;
	private String channelName;
	private ImageView channelIcon;	
	
	public ListModel() {
	}
	
	public ListModel(String channelNum, String channelName,
			ImageView channelIcon) {
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
	public ImageView getChannelIcon() {
		return channelIcon;
	}
	public void setChannelIcon(ImageView channelIcon) {
		this.channelIcon = channelIcon;
	}
	
	
	
}
