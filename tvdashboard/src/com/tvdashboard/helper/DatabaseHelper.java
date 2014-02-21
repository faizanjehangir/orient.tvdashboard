package com.tvdashboard.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.tvdashboard.model.Album;
import com.tvdashboard.model.Music;
import com.tvdashboard.model.Picture_BLL;
import com.tvdashboard.model.Video;
import com.tvdashboard.model.VideoCategory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	//changing master
	// Logcat tag
	//add comment
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 14;

	// Database Name

	private static final String DATABASE_NAME = "contactsManager";

	// Table Names

	private static final String TABLE_VIDEO = "table_video";
	private static final String TABLE_PICTURE = "table_picture";
	private static final String TABLE_MUSIC = "table_music";

	// Common column names

	private static final String KEY_ID = "id";
	private static final String KEY_FAV = "fav";
	private static final String KEY_ACTIVE = "active";

	// MUSIC Table - column names

	private static final String KEY_MUSIC_PATH = "path";
	private static final String KEY_MUSIC_ALBUM = "is_album";
	private static final String KEY_MUSIC_NAME = "music_name";



	// PICTURE Table - column names

	private static final String KEY_PICTURE_PATH = "path";
	private static final String KEY_PICTURE_ALBUM = "is_album";
	private static final String KEY_PICTURE_NAME = "picture_name";

	// VIDEO Table - column names

	private static final String KEY_VIDEO_PATH = "path";
	private static final String KEY_VIDEO_SUBCAT_ID = "subcat_id";
	private static final String KEY_VIDEO_NAME = "video_name";


	// Table Create Statements


	// Video table create statement
	private static final String CREATE_TABLE_VIDEO = "create table table_video( key_id integer primary key autoincrement, key_fav bit,key_subcat text, key_path text, key_source_name text, key_isactive bit, key_timestamp text)";

	// PICTURE table create statement
	private static final String CREATE_TABLE_PICTURE = "create table table_picture( KEY_ID integer primary key autoincrement, key_fav bit, key_path text, key_source_name text, key_isalbum bit, key_isactive bit, key_timestamp text)";

	// MUSIC table create statement
	private static final String CREATE_TABLE_MUSIC = "create table table_music( key_id integer primary key autoincrement, key_fav bit, key_path text, key_source_name text, key_isalbum bit, key_isactive bit, key_timestamp text)";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// creating required tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
		db.execSQL(CREATE_TABLE_PICTURE);
		db.execSQL(CREATE_TABLE_MUSIC);
		db.execSQL(CREATE_TABLE_VIDEO);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// on upgrade drop older tables

		/*db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);*/

		// create new tables
		onCreate(db);
	}




	// add videos
	public void addvideos(Video item, int active, String subcat) {
		SQLiteDatabase db = this.getWritableDatabase();


		String query ="insert into table_video (key_fav, key_path, key_source_name, key_subcat ,  key_isactive, key_timestamp) VALUES ('" + item.isFav() + "','" + item.getPath() + "','" + item.getSourcename() +"',"  + subcat + "," + active + ",'testtime')"; 
		db.execSQL(query);
	}

	// add pictures
	public void addpicturelist(List<Picture_BLL> pics){
		String query = "";
		SQLiteDatabase db = this.getWritableDatabase();
		for(int i=0;i<pics.size();i++)
		{
			query="insert into table_picture (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + pics.get(i).isFav() + "','" + pics.get(i).getPath() + "','" + pics.get(i).getSourcename() +"','"  + Boolean.toString( pics.get(i).isIsalbum()) + "','" + Boolean.toString( pics.get(i).isIsactive()) + "','testtime');";
			db.execSQL(query);
		}
		
		
		
	}
	
	// add music
	public void addmusiclist(List<Music> music){
		String query = "";
		SQLiteDatabase db = this.getWritableDatabase();
		for(int i=0;i<music.size();i++)
		{
			query="insert into table_music (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + music.get(i).isFav() + "','" + music.get(i).getPath() + "','" + music.get(i).getSourcename() +"','"  + Boolean.toString( music.get(i).isIsalbum()) + "','" + Boolean.toString( music.get(i).isIsactive()) + "','testtime');";
			db.execSQL(query);
		}
		
		
		
	}
	
	public void addVideolist(List<Video> video){
		String query = "";
		SQLiteDatabase db = this.getWritableDatabase();
		for(int i=0;i<video.size();i++)
		{
			query="insert into table_video (key_fav, key_path, key_source_name, key_subcat ,  key_isactive, key_timestamp) VALUES ('" + video.get(i).isFav() + "','" + video.get(i).getPath() + "','" + video.get(i).getSourcename() +"','"  + video.get(i).getSub_cat() + "','" + Boolean.toString( video.get(i).isIsactive()) + "','testtime');";
			db.execSQL(query);
		}
		
		
		
	}

	public void addpictures(Picture_BLL item, int is_album, int active) {
		SQLiteDatabase db = this.getWritableDatabase();
		/*Time now = new Time();
		now.setToNow();*/
		String query ="insert into table_picture (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + item.isFav() + "','" + item.getPath() + "','" + item.getSourcename() +"',"  + is_album + "," + active + ",'testtime')"; 

		db.execSQL(query);

	}

	// add music
	public void addmusic(Music item, int is_album, int active) {
		SQLiteDatabase db = this.getWritableDatabase();


		String query ="insert into table_music (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + item.isFav() + "','" + item.getPath() + "','" + item.getSourcename() +"',"  + is_album + "," + active + ",'testtime')"; 
		db.execSQL(query);
	}

	// add music
	public void addVideo(Video item, String subcat, int active) {
		SQLiteDatabase db = this.getWritableDatabase();


		String query ="insert into table_video (key_fav, key_path, key_source_name, key_subcat ,  key_isactive, key_timestamp) VALUES ('" + item.isFav() + "','" + item.getPath() + "','" + item.getSourcename() +"',"  + subcat + "," + active + ",'testtime')"; 
		db.execSQL(query);
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
				video.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				video.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				video.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				video.setSub_cat(c.getString(c.getColumnIndex(KEY_VIDEO_SUBCAT_ID)));
				video.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstVideo.add(video);
			} while (c.moveToNext());
		}

		return lstVideo;
	}



	/*
	 * getting all videos by categories
	 * */
	public List<Video> getAllVideosByCategory(String category) {
		List<Video> lstVideo = new ArrayList<Video>();

		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO + 
				" WHERE " + TABLE_VIDEO + "." + KEY_VIDEO_SUBCAT_ID + 
				" = " + category;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Video video = new Video();
				video.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				video.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				video.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				video.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				video.setSub_cat(c.getString(c.getColumnIndex(KEY_VIDEO_SUBCAT_ID)));
				video.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstVideo.add(video);
			} while (c.moveToNext());
		}

		return lstVideo;
	}

	/*
	 * getting all music by Favourite or not
	 * */
	public List<Music> getAllFavMusic(int fav) {
		List<Music> lstMusic = new ArrayList<Music>();

		String selectQuery = "SELECT  * FROM " + TABLE_MUSIC + 
				" WHERE " + TABLE_MUSIC + "." + KEY_FAV + 
				" = " + fav;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Music music = new Music();
				music.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				music.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				music.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				music.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				music.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstMusic.add(music);
			} while (c.moveToNext());
		}

		return lstMusic;
	}

	/*
	 * getting all pictures by Favourite or not
	 * */
	public List<Picture_BLL> getAllFavPictures(int fav) {
		List<Picture_BLL> lstPic = new ArrayList<Picture_BLL>();

		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE + 
				" WHERE " + TABLE_PICTURE + "." + KEY_FAV + 
				" = " + fav;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Picture_BLL pic = new Picture_BLL();
				pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				pic.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				pic.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				pic.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				pic.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstPic.add(pic);
			} while (c.moveToNext());
		}

		return lstPic;
	}

	/*
	 * getting all videos by Favourite or not
	 * */
	public List<Video> getAllFavVideos(int fav) {
		List<Video> lstVideo = new ArrayList<Video>();

		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO + 
				" WHERE " + TABLE_VIDEO + "." + KEY_FAV + 
				" = " + fav;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Video video = new Video();
				video.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				video.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				video.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				video.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				video.setSub_cat(c.getString(c.getColumnIndex(KEY_VIDEO_SUBCAT_ID)));
				video.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstVideo.add(video);
			} while (c.moveToNext());
		}

		return lstVideo;
	}

	// getting all pictures
	public List<Picture_BLL> getAllPictures() {
		List<Picture_BLL> lstPic = new ArrayList<Picture_BLL>();
		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Picture_BLL pic = new Picture_BLL();
				pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				pic.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				pic.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				pic.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				pic.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstPic.add(pic);
			} while (c.moveToNext());
		}

		return lstPic;
	}


	/*
	 * getting Pictures by imageID
	 * */
	public List<Picture_BLL> getPictureById(int id) {
		List<Picture_BLL> lstPic = new ArrayList<Picture_BLL>();

		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE + 
				" WHERE " + TABLE_PICTURE + "." + KEY_ID + 
				" = " + id;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Picture_BLL pic = new Picture_BLL();
				pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				pic.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				pic.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				pic.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				pic.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstPic.add(pic);
			} while (c.moveToNext());
		}

		return lstPic;
	}

	/*
	 * getting Pictures by image name
	 * */
	public List<Picture_BLL> getPictureByName(String name) {
		List<Picture_BLL> lstPic = new ArrayList<Picture_BLL>();

		if (name.length() != 0) {
			name = "%" + name + "%";
		}

		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE + 
				" WHERE " + TABLE_PICTURE + "." + KEY_PICTURE_PATH + 
				" Like " + name;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Picture_BLL pic = new Picture_BLL();
				pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				pic.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				pic.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				pic.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				pic.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstPic.add(pic);
			} while (c.moveToNext());
		}

		return lstPic;
	}

	/*
	 * getting Pictures by Album Name
	 * */
	public List<Picture_BLL> getPictureByAlbumName(String AlbumName) {
		List<Picture_BLL> lstPic = new ArrayList<Picture_BLL>();

		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE + 
				" WHERE " + TABLE_PICTURE + "." + KEY_PICTURE_NAME + 
				" = " + AlbumName;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Picture_BLL pic = new Picture_BLL();
				pic.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				pic.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				pic.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				pic.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				pic.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstPic.add(pic);
			} while (c.moveToNext());
		}

		return lstPic;
	}

	/*
	 * getting Pictures by Album Name
	 * */
	public List<Album> getAllAlbums() {

		List<Album> lstAlbum = new ArrayList<Album>();

		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE + 
				" WHERE " + TABLE_PICTURE + "." + KEY_PICTURE_ALBUM + 
				" = " + 1;


		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Album album = new Album();
				album.setName((c.getString(c.getColumnIndex(KEY_VIDEO_NAME))));
				album.setPath((c.getString(c.getColumnIndex(KEY_VIDEO_PATH))));
				album.setAllPictures(getPictureByAlbumName(album.getName()));

				// adding to video list
				lstAlbum.add(album);
			} while (c.moveToNext());
		}


		return lstAlbum;
	}
	// getting all music
	public List<Music> getAllMusic() {
		List<Music> lstMusic = new ArrayList<Music>();
		String selectQuery = "SELECT  * FROM " + TABLE_MUSIC;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Music music = new Music();
				music.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				music.setFav(Boolean.parseBoolean( c.getString((c.getColumnIndex(KEY_FAV)))));
				music.setSourcename((c.getString(c.getColumnIndex(KEY_PICTURE_NAME))));
				music.setPath((c.getString(c.getColumnIndex(KEY_PICTURE_PATH))));
				music.setIsactive(Boolean.parseBoolean(c.getString((c.getColumnIndex(KEY_ACTIVE)))));
				// adding to video list
				lstMusic.add(music);
			} while (c.moveToNext());
		}

		return lstMusic;
	}







	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}



}
