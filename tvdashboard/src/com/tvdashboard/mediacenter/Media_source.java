package com.tvdashboard.mediacenter;

public enum Media_source {
	
	Pitures("P"), Videos("V"), Music("M");
	 
	private String statusCode;
 
	private Media_source(String s) {
		statusCode = s;
	}
 
	public String get_media_source() {
		return statusCode;
	}

}