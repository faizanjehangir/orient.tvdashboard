package com.tvdashboard.androidwebservice;

import java.util.List;

public class ChannelSchedule {
	
	private String date;
	private String channelName;
	private List<Show> listOfShows;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public List<Show> getListOfShows() {
		return listOfShows;
	}
	public void setListOfShows(List<Show> listOfShows) {
		this.listOfShows = listOfShows;
	}
}
