package com.tvdashboard.model;

public class Music {
	
	private int id;
	private String sourcename;
	private String path;
	private boolean fav;
	private boolean isactive;
	private boolean isalbum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isFav() {
		return fav;
	}
	public void setFav(boolean fav) {
		this.fav = fav;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	public boolean isIsalbum() {
		return isalbum;
	}
	public void setIsalbum(boolean isalbum) {
		this.isalbum = isalbum;
	}
	
	

	
}
