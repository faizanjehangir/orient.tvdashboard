package com.tvdashboard.music.manager;

public class Album {
	
	String [] theme;
	String description;
	String type;
	String [] style;
	int albumid;
	int playcount;
	String albumlabel;
	String [] mood;
	 
	public String[] getTheme() {
		return theme;
	}
	public void setTheme(String[] theme) {
		this.theme = theme;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getStyle() {
		return style;
	}
	public void setStyle(String[] style) {
		this.style = style;
	}
	public int getAlbumid() {
		return albumid;
	}
	public void setAlbumid(int albumid) {
		this.albumid = albumid;
	}
	public int getPlaycount() {
		return playcount;
	}
	public void setPlaycount(int playcount) {
		this.playcount = playcount;
	}
	public String getAlbumlabel() {
		return albumlabel;
	}
	public void setAlbumlabel(String albumlabel) {
		this.albumlabel = albumlabel;
	}
	public String[] getMood() {
		return mood;
	}
	public void setMood(String[] mood) {
		this.mood = mood;
	}
	 
}
