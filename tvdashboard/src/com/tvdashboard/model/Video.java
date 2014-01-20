package com.tvdashboard.model;

public class Video {
	
	int id;
	String name;
	String path;
	
	public Video(){
	}
	
	public Video(int id, String name, String path){
		this.id = id;
		this.name = name;
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
