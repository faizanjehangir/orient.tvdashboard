package com.tvdashboard.model;

import java.util.List;

public class Album {

	public String name;
	public int size;
	public String path;
	public List<Picture_BLL> allPictures;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Picture_BLL> getAllPictures() {
		return allPictures;
	}
	public void setAllPictures(List<Picture_BLL> allPictures) {
		this.allPictures = allPictures;
	}
	
}
