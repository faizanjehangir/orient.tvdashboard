package com.tvdashboard.androidwebservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tvdashboard.channelsetup.Channel;

import android.content.Context;

public interface IChannelManager {
	public ArrayList<Channel> getAllChannels();
	public ArrayList<Channel> getAllChannelsByRegion(String region);
	public ArrayList<String> getAllChannelNumbers();
	public HashMap<String, ArrayList<String>> getAllChannelNamesByRegion();
	public ArrayList<String> getAllRegions();
	public void setChannelNumber(String name, String number);
}
