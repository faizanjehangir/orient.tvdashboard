package com.tvdashboard.helper;

import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.model.Video;
import com.tvdashboard.model.VideoCategory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	//changing file
	// Logcat tag
	//add comment
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Table Names
    private static final String TABLE_VIDEO_CATEGORY = "video_category";
    private static final String TABLE_VIDEO = "video";
 
    // Common column names
    private static final String KEY_ID = "id";
 
    // CATEGORY Table - column names
    private static final String KEY_CATEGORY_NAME = "category_name";
 
    // VIDEO Table - column names
    private static final String KEY_VIDEO_NAME = "video_name";
    private static final String KEY_VIDEO_PATH = "path";
    private static final String KEY_VIDEO_CAT_ID = "cat_id";
 
    // Table Create Statements
    // video category table create statement
    private static final String CREATE_TABLE_VIDEO_CAT = "CREATE TABLE "
            + TABLE_VIDEO_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY_NAME
            + " TEXT)";
 
    // Video table create statement
    private static final String CREATE_TABLE_VIDEO = "CREATE TABLE "
            + TABLE_VIDEO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_VIDEO_CAT_ID + " INTEGER," + KEY_VIDEO_NAME + " TEXT)";
	
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// creating required tables
        db.execSQL(CREATE_TABLE_VIDEO_CAT);
        db.execSQL(CREATE_TABLE_VIDEO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
 
        // create new tables
        onCreate(db);
	}
	
	public long createVideoCategory(VideoCategory vc){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_CATEGORY_NAME, vc.getName());
	 
	    // insert row
	    long category_id = db.insert(TABLE_VIDEO_CATEGORY, null, values);
	 
	    return category_id;
	}
	
	/*
	 * getting all video categories
	 * */
	public List<VideoCategory> getAllVideoCategories() {
	    List<VideoCategory> lstVc = new ArrayList<VideoCategory>();
	    String selectQuery = "SELECT  * FROM " + TABLE_VIDEO_CATEGORY;
	 
	    Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	        	VideoCategory vc = new VideoCategory();
	        	vc.setId(c.getInt((c.getColumnIndex(KEY_ID))));
	            vc.setName((c.getString(c.getColumnIndex(KEY_CATEGORY_NAME))));
	 
	            // adding to category list
	            lstVc.add(vc);
	        } while (c.moveToNext());
	    }
	 
	    return lstVc;
	}
	
	/*
	 * create videos
	 * */
	public boolean addVideos(List<Video> video, int category_id){
		SQLiteDatabase db = this.getWritableDatabase();
		long id = 0;
		for(int i = 0; i <video.size(); ++i){
			ContentValues values = new ContentValues();
		    values.put(KEY_VIDEO_NAME, video.get(i).getName());
		    values.put(KEY_VIDEO_PATH, video.get(i).getPath());
		    values.put(KEY_VIDEO_CAT_ID, category_id);
		    // insert row
		    id = db.insert(TABLE_VIDEO, null, values);
		}
	    
	    if (id < 0)
	    	return false;
	    return true;
	}
	
	/*
	 * getting all videos
	 * */
	public List<Video> getAllVideos() {
	    List<Video> lstVideo = new ArrayList<Video>();
	    String selectQuery = "SELECT  * FROM " + TABLE_VIDEO;
	 
	    Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	        	Video video = new Video();
	        	video.setId(c.getInt((c.getColumnIndex(KEY_ID))));
	        	video.setName((c.getString(c.getColumnIndex(KEY_VIDEO_NAME))));
	        	video.setPath((c.getString(c.getColumnIndex(KEY_VIDEO_PATH))));
	            // adding to video list
	        	lstVideo.add(video);
	        } while (c.moveToNext());
	    }
	 
	    return lstVideo;
	}
	
	/*
	 * getting all videos by categories
	 * */
	public List<Video> getAllVideosByCategory(int category) {
	    List<Video> lstVideo = new ArrayList<Video>();

	    String selectQuery = "SELECT  * FROM " + TABLE_VIDEO + 
	    		" WHERE " + TABLE_VIDEO + "." + KEY_VIDEO_CAT_ID + 
	    		" = " + category;
	 
	    Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	        	Video video = new Video();
	        	video.setId(c.getInt((c.getColumnIndex(KEY_ID))));
	        	video.setName((c.getString(c.getColumnIndex(KEY_VIDEO_NAME))));
	        	video.setPath((c.getString(c.getColumnIndex(KEY_VIDEO_PATH))));
	            // adding to video list
	        	lstVideo.add(video);
	        } while (c.moveToNext());
	    }
	 
	    return lstVideo;
	}
	
	// closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    
    

}
