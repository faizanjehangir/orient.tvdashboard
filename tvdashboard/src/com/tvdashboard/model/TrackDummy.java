package com.tvdashboard.model;

public class TrackDummy {
	
	private String path;
	private String trackName;
	private String artist;	
	private String realPath;
	
	public TrackDummy()
	{}
	
	public TrackDummy(String path, String trackName, String artist) {
		super();
		this.path = path;
		this.trackName = trackName;
		this.artist = artist;
	}

	public TrackDummy(String path, String trackName, String artist,	String realPath) {
		super();
		this.path = path;
		this.trackName = trackName;
		this.artist = artist;
		this.realPath = realPath;
	}


	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getRealPath() {
		return realPath;
	}


	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}	
	
	
}
