package com.tvdashboard.androidwebservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		xmlManager = new XMLChannelManager(context, Preferences.TVGuideFiles.CHANNEL_TVGUIDE_LIST);
		xmlManager.fetchXML();
		while(xmlManager.parsingComplete);
		return xmlManager.getAllChannels();
	}

	@Override
	public ArrayList<Channel> getAllChannelsByRegion(String region) {
		// TODO Auto-generated method stub
		xmlManager = new XMLChannelManager(context, Preferences.TVGuideFiles.CHANNEL_TVGUIDE_LIST);
		xmlManager.fetchXML();
		while(xmlManager.parsingComplete);
		return xmlManager.getChannelListByRegion(region);
	}
	
	@Override
	public ArrayList<String> getAllChannelNumbers(){
		xmlManager = new XMLChannelManager(context, Preferences.TVGuideFiles.CHANNEL_TVSET_NUMBERS);
		xmlManager.fetchXML();
		while(xmlManager.parsingComplete);
		return xmlManager.getAllChannelNumbers();
	}
	
	@Override
	public HashMap<String, ArrayList<String>> getAllChannelNamesByRegion(){
		xmlManager = new XMLChannelManager(context, Preferences.TVGuideFiles.CHANNEL_TVGUIDE_NAMES);
		xmlManager.fetchXML();
		while(xmlManager.parsingComplete);
		return xmlManager.getAllChannelNamesByRegions();
	}
	
	@Override
	public ArrayList<String> getAllRegions(){
		HashMap<String, ArrayList<String>> chRegions = getAllChannelNamesByRegion();
		//get all keys
		ArrayList<String> lstKeys = new ArrayList<String>();
		lstKeys.add("All");
		for (Map.Entry entry : chRegions.entrySet()) {
		    lstKeys.add((String)entry.getKey());
		}
		return lstKeys;
	}
	
	@Override
	public void setChannelNumber(String name, String number){
		Channel ch = new Channel();
		ch.setChannelName(name);
		ch.setChannelNum(number);
		xmlManager = new XMLChannelManager(context, Preferences.TVGuideFiles.CHANNEL_TVGUIDE_NAMES);
		xmlManager.channelXMLNumberStore(ch);
	}
}
