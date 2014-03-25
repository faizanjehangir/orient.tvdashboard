package com.tvdashboard.androidwebservice;

import java.util.ArrayList;

public interface IScheduleManager {
	
	public Show getNowPlayingScheduleByChannel(ChannelSchedule channel);
	public Show getUpComingScheduleByChannel(ChannelSchedule channel);
	public void getAllChannelSchedule();

}
