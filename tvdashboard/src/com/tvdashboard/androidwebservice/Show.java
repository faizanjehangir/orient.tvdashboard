package com.tvdashboard.androidwebservice;

import java.util.Map;

public class Show {
	
	private String showTitle;
	private String showTime;
	private String showThumb;
	Map<String, String> showDetails;
	
	public String getShowThumb() {
		return showThumb;
	}
	public void setShowThumb(String showThumb) {
		this.showThumb = showThumb;
	}
	public String getShowTitle() {
		return showTitle;
	}
	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public Map<String, String> getShowDetails() {
		return showDetails;
	}
	public void setShowDetails(Map<String, String> showDetails) {
		this.showDetails = showDetails;
	}
	
}
