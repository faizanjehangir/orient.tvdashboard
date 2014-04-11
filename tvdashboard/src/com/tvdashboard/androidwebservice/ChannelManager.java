package com.tvdashboard.androidwebservice;

import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.channelsetup.Channel;
import com.tvdashboard.utility.Preferences;

import android.content.Context;

public class ChannelManager implements IChannelManager {

	Context context;
	private XMLChannelManager xmlManager;
	//private static String channelListFileName = "tvguidechannellist.xml";
	
	public ChannelManager(Context context){
		this.context = context;
	}
	
	@Override
	public ArrayList<Channel> getAllChannels() {
		// TODO Auto-generated method stub
		xmlManager = new XMLChannelManager(context, Preferences.CHANNEL_LIST_FILENAME);
		xmlManager.fetchXML();
		while(xmlManager.parsingComplete);
		return xmlManager.getAllChannels();
	}

	@Override
	public ArrayList<Channel> getAllChannelsByRegion(String region) {
		// TODO Auto-generated method stub
		xmlManager = new XMLChannelManager(context, Preferences.CHANNEL_LIST_FILENAME);
		xmlManager.fetchXML();
		while(xmlManager.parsingComplete);
		return xmlManager.getChannelListByRegion(region);
	}
	
	public List<String> getAllRegions()
	 {
	  List<String> regions = new ArrayList<String>();
	  xmlManager = new XMLChannelManager(context, Preferences.CHANNEL_LIST_FILENAME);
	  xmlManager.fetchXML();
	  while(xmlManager.parsingComplete);
	  return xmlManager.getAllRegions();
	 }
}
