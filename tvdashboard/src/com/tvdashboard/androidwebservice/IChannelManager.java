package com.tvdashboard.androidwebservice;

import java.util.ArrayList;

import com.tvdashboard.channelsetup.Channel;

import android.content.Context;

public interface IChannelManager {
	
	public ArrayList<Channel> getAllChannels();
	public ArrayList<Channel> getAllChannelsByRegion(String region);
}
