package com.tvdashboard.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.model.Music;
import com.tvdashboard.model.Picture_BLL;
import com.tvdashboard.model.Video;
import com.tvdashboard.music.MusicSection;
import com.tvdashboard.pictures.PictureSection;
import com.tvdashboard.videos.VideoSection;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


public class Source {
	private static File root;
    private static ArrayList<File> fileList = new ArrayList<File>();
    public static Media_source mSource;
    public static Picture_BLL pictures;
    public static Video videos;
    public static Music music;
    public static DatabaseHelper db;
    private static Context context;
    
    public Source(Media_source mSource, Context context2) {
		// TODO Auto-generated constructor stub
    	this.mSource = mSource;
		this.context=context2;
	}

	public static void main(String[] args){
       
    	
    	
    	db = new DatabaseHelper(context);
		root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
       
		pictures = new Picture_BLL();
		videos = new Video();
		music = new Music();
    	selectStuff(mSource);
    }

	public void insertPictureList(List<Picture_BLL> pics){
		context = PictureSection.context;
		db = new DatabaseHelper(context);
		db.addpicturelist(pics);
	}
	
	public void insertMusicList(List<Music> music){
		context = MusicSection.context;
		db = new DatabaseHelper(context);
		db.addmusiclist(music);
	}
	
	public void insertVideoList(List<Video> video){
		context = VideoSection.context;
		db = new DatabaseHelper(context);
		db.addVideolist(video);
	}
	
	public static void selectStuff(Media_source m) {

	    switch(m) {

	        case Picture:
	        	//Do image stuff
	        	
	        	context = PictureSection.context;
	        	db = new DatabaseHelper(context);
	        	root = new File(Environment.getExternalStorageDirectory()
	                    .getAbsolutePath());
	            
	    		pictures = new Picture_BLL();
	    		
	        	getpicfile(root);   	

	        	
	        	
	    		for (int i = 0; i < fileList.size(); i++) {
	                
	    			pictures.setSourcename(fileList.get(i).getName());
	                pictures.setPath(fileList.get(i).getAbsolutePath());
	                 
	                db.addpictures(pictures, 1,1);
	            }
	    		
	    		
	    		
	    		/*Log.d("Reading: ", "Reading all contacts..");
	            List<Picture_BLL> contacts = db.getAllPictures();*/
	            
	            /*for (Picture_BLL cn : contacts) {
	                String log = "Id: "+cn.getId()+" ,Name: " + cn.getSourcename() + " fav: " + cn.isFav()
	                		+ " isActive:" + cn.isIsactive() + "path:" + cn.getPath();
	                   // Writing Contacts to log
	            Log.d("Name: ", log);
	           
	            
	            //name_title=cn.getName().toString();
	            }  */
	            break;

	        case Videos:
	            //Do video stuff
	        	context = VideoSection.context;
	        	db = new DatabaseHelper(context);
	        	root = new File(Environment.getExternalStorageDirectory()
	                    .getAbsolutePath());
	           
	    		videos = new Video();
	        	
	        	getvidfile(root); 
	        	
	        	for (int i = 0; i < fileList.size(); i++) {
	                
	        		
	                videos.setPath(fileList.get(i).getAbsolutePath());
	                
	                db.addvideos(videos, 1, "videocat");
	            }
	            break;

	        case Music:
	            //Do audio stuff
	        	context = MusicSection.context;
	        	db = new DatabaseHelper(context);
	        	root = new File(Environment.getExternalStorageDirectory()
	                    .getAbsolutePath());
	           
	    		music = new Music();
	    		
	        	getmusicfile(root); 
	        	
	        	for (int i = 0; i < fileList.size(); i++) {
	                
	        		music.setSourcename(fileList.get(i).getName());
	                music.setPath(fileList.get(i).getAbsolutePath());
	                
	                db.addmusic(music, 1,1);
	            }
	            break;

	        default:
	            break;

	    }
}
	public static ArrayList<File> getpicfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
 
                if (listFile[i].isDirectory()) {
                    /*fileList.add(listFile[i]);*/
                    getpicfile(listFile[i]);
 
                } else {
                    if (listFile[i].getName().endsWith(".png")
                            || listFile[i].getName().endsWith(".jpg")
                            || listFile[i].getName().endsWith(".jpeg")
                            || listFile[i].getName().endsWith(".gif"))
                    {
                        fileList.add(listFile[i]);
                    }
                }
 
            }
        }
        
        return fileList;
    }
	
	
	public static ArrayList<File> getvidfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
 
                if (listFile[i].isDirectory()) {
                    fileList.add(listFile[i]);
                    getvidfile(listFile[i]);
 
                } else {
                    if (listFile[i].getName().endsWith(".mp4")
                            || listFile[i].getName().endsWith(".mpeg")
                            || listFile[i].getName().endsWith(".mpg")
                            || listFile[i].getName().endsWith(".wmv")
                            || listFile[i].getName().endsWith(".wmx"))
 
                    {
                        fileList.add(listFile[i]);
                    }
                }
 
            }
        }
        return fileList;
    }
	
	public static ArrayList<File> getmusicfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
 
                if (listFile[i].isDirectory()) {
                    fileList.add(listFile[i]);
                    getmusicfile(listFile[i]);
 
                } else {
                    if (listFile[i].getName().endsWith(".mp3")
                            || listFile[i].getName().endsWith(".wav")
                            || listFile[i].getName().endsWith(".gsm")
                            || listFile[i].getName().endsWith(".au"))
 
                    {
                        fileList.add(listFile[i]);
                    }
                }
 
            }
        }
        return fileList;
    }


}
