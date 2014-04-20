package com.tvdashboard.music;

import java.io.File;

import android.graphics.Bitmap;

public class FileManagerItem {
	
	private Bitmap fileIcon;
	private File file;
	public Bitmap getFileIcon() {
		return fileIcon;
	}
	public void setFileIcon(Bitmap fileIcon) {
		this.fileIcon = fileIcon;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}
