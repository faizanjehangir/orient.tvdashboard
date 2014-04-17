package com.tvdashboard.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jcifs.dcerpc.msrpc.netdfs;

import com.tvdashboard.model.Picture_BLL;
import com.tvdashboard.model.TrackDummy;
import com.tvdashboard.model.Video;
import com.tvdashboard.model.VideoCategory;
import com.tvdashboard.music.manager.Album;
import com.tvdashboard.music.manager.Artist;
import com.tvdashboard.music.manager.Genre;
import com.tvdashboard.music.manager.MusicAlbums;
import com.tvdashboard.music.manager.MusicArtist;
import com.tvdashboard.music.manager.MusicTracks;
import com.tvdashboard.music.manager.Track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private Context context;

	//changing master
	// Logcat tag
	//add comment
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name

	private static final String DATABASE_NAME = "contactsManager";

	// Table Names

	private static final String TABLE_VIDEO = "table_video";
	private static final String TABLE_PICTURE = "table_picture";
	
//	private static final String TABLE_MUSIC = "table_music";
	private static final String TABLE_MUSIC_TRACK = "table_music_track";
	private static final String TABLE_MUSIC_ARTIST = "table_music_artist";
	private static final String TABLE_MUSIC_ALBUM = "table_music_album";
	
	// Table MUSIC_TRACK schema
	
	private static final String MUSIC_TRACK_ID = "id";
	private static final String MUSIC_TRACK_IDTRACK = "idTrack";
	private static final String MUSIC_TRACK_IDALBUM = "idAlbum";
	private static final String MUSIC_TRACK_IDARTIST = "idArtist";
	private static final String MUSIC_TRACK_STRTRACK = "strTrack";
	private static final String MUSIC_TRACK_STRALBUM = "strAlbum";
	private static final String MUSIC_TRACK_STRARTIST = "strArtist";
	private static final String MUSIC_TRACK_PATH = "path";
	private static final String MUSIC_TRACK_INTDURATION = "intDuration";
	private static final String MUSIC_TRACK_STRMUSICVID = "strMusicVid";
	private static final String MUSIC_TRACK_ISFAV = "isFav";
	private static final String MUSIC_TRACK_ISACTIVE = "isActive";
	private static final String MUSIC_TRACK_INSERTTIMESTAMP = "insert_timestamp";
	private static final String MUSIC_TRACK_RECENTTIMESTAMP = "recent_timestamp";

	
	//Table MUSIC_ALBUM schema
	
	private static final String MUSIC_ALBUM_ID = "id";
	private static final String MUSIC_ALBUM_IDALBUM = "idAlbum";
	private static final String MUSIC_ALBUM_IDARTIST = "idArtist";
	private static final String MUSIC_ALBUM_STRALBUM = "strAlbum";
	private static final String MUSIC_ALBUM_STRARTIST = "strArtist";
	private static final String MUSIC_ALBUM_INTYEARRELEASED = "intYearReleased";
	private static final String MUSIC_ALBUM_STRGENRE = "strGenre";
	private static final String MUSIC_ALBUM_STRSUBGENRE= "strSubGenre";
	private static final String MUSIC_ALBUM_STRRELEASEFORMAT = "strReleaseFormat";
	private static final String MUSIC_ALBUM_INTSALES = "intSales";
	private static final String MUSIC_ALBUM_STRALBUMTHUMB = "strAlbumThumb";
	private static final String MUSIC_ALBUM_STRDESCRIPTIONEN = "strDescriptionEN";
	private static final String MUSIC_ALBUM_STRREVIEW = "strReview";
	private static final String MUSIC_ALBUM_ISFAV = "isFav";
	private static final String MUSIC_ALBUM_ISACTIVE = "isActive";
	private static final String MUSIC_ALBUM_TIMESTAMP = "timestamp";
	
	//Table MUSIC_ARTIST schema
	
	private static final String MUSIC_ARTIST_ID = "id";
	private static final String MUSIC_ARTIST_IDARTIST = "idArtist";
	private static final String MUSIC_ARTIST_STRARTIST = "strArtist";
	private static final String MUSIC_ARTIST_INTFORMEDYEAR = "intFormedYear";
	private static final String MUSIC_ARTIST_INTBORNYEAR = "intBornYear";
	private static final String MUSIC_ARTIST_STRGENRE = "strGenre";
	private static final String MUSIC_ARTIST_STRSUBGENRE = "strSubGenre";
	private static final String MUSIC_ARTIST_STRMOOD = "strMood";
	private static final String MUSIC_ARTIST_STRWEBSITE = "strWebsite";
	private static final String MUSIC_ARTIST_STRFACEBOOK = "strFacebook";
	private static final String MUSIC_ARTIST_STRTWITTER = "strTwitter";
	private static final String MUSIC_ARTIST_STRBIOGRAPHYEN = "strBiographyEN";
	private static final String MUSIC_ARTIST_STRCOUNTRY = "strCountry";
	private static final String MUSIC_ARTIST_STRGENDER = "strGender";
	private static final String MUSIC_ARTIST_INTMEMBERS = "intMembers";
	private static final String MUSIC_ARTIST_STRARTISTTHUMB = "strArtistThumb";
	private static final String MUSIC_ARTIST_STRARTISTLOGO = "strArtistLogo";
	private static final String MUSIC_ARTIST_STRARTISTFANART = "strArtistFanArt";
	private static final String MUSIC_ARTIST_STRARTISTFANART2 = "strArtistFanArt2";
	private static final String MUSIC_ARTIST_STRARTISTFANART3 = "strArtistFanArt3";
	private static final String MUSIC_ARTIST_STRARTISTBANNER = "strArtistBanner";
	private static final String MUSIC_ARTIST_ISFAV = "isFav";
	private static final String MUSIC_ARTIST_ISACTIVE = "isActive";
	private static final String MUSIC_ARTIST_TIMESTAMP = "timestamp";
	
	
	// Common column names

	private static final String KEY_ID = "key_id";
	private static final String KEY_FAV = "key_fav";
	private static final String KEY_ACTIVE = "key_isactive";

	// PICTURE Table - column names

	private static final String KEY_PICTURE_PATH = "key_path";
	private static final String KEY_PICTURE_ALBUM = "key_isalbum";
	private static final String KEY_PICTURE_NAME = "key_source_name";

	// VIDEO Table - column names

	private static final String KEY_VIDEO_PATH = "key_path";
	private static final String KEY_VIDEO_SUBCAT_ID = "key_subcat";
	private static final String KEY_VIDEO_NAME = "key_source_name";
	private static final String KEY_TIMESTAMP = "key_timestamp";


	// Table Create Statements


	// Video table create statement
	private static final String CREATE_TABLE_VIDEO = "create table table_video( key_id integer primary key autoincrement, key_fav bit,key_subcat text, key_path text, key_source_name text, key_isactive bit, key_timestamp text)";

	// PICTURE table create statement
	private static final String CREATE_TABLE_PICTURE = "create table table_picture( KEY_ID integer primary key autoincrement, key_fav bit, key_path text, key_source_name text, key_isalbum bit, key_isactive bit, key_timestamp text)";
	
	// MUSIC Tables ///////////////////////////////////////////////////////////////////////////////
	
	private static final String CREATE_TABLE_MUSIC_TRACK = "CREATE TABLE "+ TABLE_MUSIC_TRACK + "("
			+ MUSIC_TRACK_ID + " integer primary key autoincrement, "
			+ MUSIC_TRACK_IDTRACK + " text, "
			+ MUSIC_TRACK_IDALBUM + " text, "
			+ MUSIC_TRACK_IDARTIST + " text, "
			+ MUSIC_TRACK_STRTRACK + " text, "
			+ MUSIC_TRACK_STRALBUM + " text, "
			+ MUSIC_TRACK_STRARTIST + " text, "
			+ MUSIC_TRACK_PATH + " text, "
			+ MUSIC_TRACK_INTDURATION + " text, "
			+ MUSIC_TRACK_STRMUSICVID + " text, "
			+ MUSIC_TRACK_ISFAV  + " bit, "
			+ MUSIC_TRACK_ISACTIVE + " bit, "
			+ MUSIC_TRACK_RECENTTIMESTAMP + " text, "
			+ MUSIC_TRACK_INSERTTIMESTAMP + " text)";
	
	private static final String CREATE_TABLE_MUSIC_ALBUM = "CREATE TABLE " + TABLE_MUSIC_ALBUM + "("
			+ MUSIC_ALBUM_ID + " integer primary key autoincrement,"
			+ MUSIC_ALBUM_IDALBUM + " text, "
			+ MUSIC_ALBUM_IDARTIST + " text, "
			+ MUSIC_ALBUM_STRALBUM + " text, "
			+ MUSIC_ALBUM_STRARTIST + " text, "
			+ MUSIC_ALBUM_INTYEARRELEASED + " text, "
			+ MUSIC_ALBUM_STRGENRE + " text, "
			+ MUSIC_ALBUM_STRSUBGENRE + " text, "
			+ MUSIC_ALBUM_STRRELEASEFORMAT + " text, "
			+ MUSIC_ALBUM_INTSALES + " text, "
			+ MUSIC_ALBUM_STRALBUMTHUMB + " text, "
			+ MUSIC_ALBUM_STRDESCRIPTIONEN + " text, "
			+ MUSIC_ALBUM_STRREVIEW + " text,"
			+ MUSIC_ALBUM_ISFAV  + " bit, "
			+ MUSIC_ALBUM_ISACTIVE + " bit, "
			+ MUSIC_ALBUM_TIMESTAMP + " text)";
	
	private static final String CREATE_TABLE_MUSIC_ARTIST = "CREATE TABLE "+ TABLE_MUSIC_ARTIST + "("
			+ MUSIC_ARTIST_ID + " integer primary key autoincrement, "
			+ MUSIC_ARTIST_IDARTIST + " text, "
			+ MUSIC_ARTIST_STRARTIST + " text, "
			+ MUSIC_ARTIST_INTFORMEDYEAR + " text, "
			+ MUSIC_ARTIST_INTBORNYEAR + " text, "
			+ MUSIC_ARTIST_STRGENRE + " text, "
			+ MUSIC_ARTIST_STRSUBGENRE + " text, "
			+ MUSIC_ARTIST_STRMOOD + " text, "
			+ MUSIC_ARTIST_STRWEBSITE + " text, "
			+ MUSIC_ARTIST_STRFACEBOOK + " text, "
			+ MUSIC_ARTIST_STRTWITTER + " text, "
			+ MUSIC_ARTIST_STRBIOGRAPHYEN + " text, "
			+ MUSIC_ARTIST_STRCOUNTRY + " text, "
			+ MUSIC_ARTIST_STRGENDER + " text,"
			+ MUSIC_ARTIST_INTMEMBERS + " text, "
			+ MUSIC_ARTIST_STRARTISTTHUMB + " text, "
			+ MUSIC_ARTIST_STRARTISTLOGO + " text, "
			+ MUSIC_ARTIST_STRARTISTFANART + " text, "
			+ MUSIC_ARTIST_STRARTISTFANART2 + " text, "
			+ MUSIC_ARTIST_STRARTISTFANART3 + " text, "
			+ MUSIC_ARTIST_STRARTISTBANNER + " text,"
			+ MUSIC_ARTIST_ISFAV  + " bit, "
			+ MUSIC_ARTIST_ISACTIVE + " bit, "
			+ MUSIC_ARTIST_TIMESTAMP + " text)";
			
	/////////////////////////////////////////////////////////////////////////////////////////////////
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
//		db.execSQL(CREATE_TABLE_PICTURE);
//		db.execSQL(CREATE_TABLE_VIDEO);
		
		db.execSQL(CREATE_TABLE_MUSIC_TRACK);
		db.execSQL(CREATE_TABLE_MUSIC_ALBUM);
		db.execSQL(CREATE_TABLE_MUSIC_ARTIST);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC_TRACK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC_ALBUM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC_ARTIST);	
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
		Time now = new Time();
		now.setToNow();
		for(int i=0;i<pics.size();i++)
		{
			query="insert into table_picture (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + pics.get(i).isFav() + "','" + pics.get(i).getPath() + "','" + pics.get(i).getSourcename() +"','"  + Boolean.toString( pics.get(i).isIsalbum()) + "','" + Boolean.toString( pics.get(i).isIsactive()) + "','"+now.toString()+"');";
			db.execSQL(query);
		}	
	}
	
	// Add Music Track
	public void addMusicTrack(MusicTracks allTracks,String realPath, int isFav, int isActive){
		String query = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Time now = new Time();
		now.setToNow();
		ContentValues cv = new ContentValues();
		cv.put(MUSIC_TRACK_IDTRACK, allTracks.getTrack().get(0).getIdTrack());
		cv.put(MUSIC_TRACK_IDALBUM, allTracks.getTrack().get(0).getIdAlbum());
		cv.put(MUSIC_TRACK_IDARTIST, allTracks.getTrack().get(0).getIdArtist());
		cv.put(MUSIC_TRACK_STRTRACK, allTracks.getTrack().get(0).getStrTrack());
		cv.put(MUSIC_TRACK_STRALBUM, allTracks.getTrack().get(0).getStrAlbum());
		cv.put(MUSIC_TRACK_STRARTIST, allTracks.getTrack().get(0).getStrArtist());
		cv.put(MUSIC_TRACK_PATH, realPath);
		cv.put(MUSIC_TRACK_INTDURATION, allTracks.getTrack().get(0).getIntDuration());
		cv.put(MUSIC_TRACK_STRMUSICVID, allTracks.getTrack().get(0).getStrMusicVid());
		cv.put(MUSIC_TRACK_ISFAV, isFav);
		cv.put(MUSIC_TRACK_ISACTIVE, isActive);
		cv.put(MUSIC_TRACK_INSERTTIMESTAMP, now.toString());
			
		int rows = db.update(TABLE_MUSIC_TRACK, cv, MUSIC_TRACK_IDTRACK + " = '" + allTracks.getTrack().get(0).getIdTrack() + "'", null);			
		if (rows == 0)
		{
			int rowId = (int)db.insert(TABLE_MUSIC_TRACK, null, cv);
			if (rowId != -1)
			{
				Log.v("TRACK INSERTION", "Successfull");
			}			
		}
		closeDB();
	}
	
	// Add Music Track
		public void addMusicAlbum(MusicAlbums albums, int isFav, int isActive){
			String query = "";
			SQLiteDatabase db = this.getWritableDatabase();
			Time now = new Time();
			now.setToNow();	
			
			ContentValues cv = new ContentValues();
			cv.put(MUSIC_ALBUM_IDALBUM, albums.getAlbum().get(0).getIdAlbum());
			cv.put(MUSIC_ALBUM_IDARTIST, albums.getAlbum().get(0).getIdArtist());
			cv.put(MUSIC_ALBUM_STRALBUM, albums.getAlbum().get(0).getStrAlbum());
			cv.put(MUSIC_ALBUM_STRARTIST, albums.getAlbum().get(0).getStrArtist());
			cv.put(MUSIC_ALBUM_INTYEARRELEASED, albums.getAlbum().get(0).getIntYearReleased());
			cv.put(MUSIC_ALBUM_STRGENRE, albums.getAlbum().get(0).getStrGenre());
			cv.put(MUSIC_ALBUM_STRSUBGENRE, albums.getAlbum().get(0).getStrSubGenre());
			cv.put(MUSIC_ALBUM_STRRELEASEFORMAT, albums.getAlbum().get(0).getStrReleaseFormat());
			cv.put(MUSIC_ALBUM_INTSALES, albums.getAlbum().get(0).getIntSales());
			cv.put(MUSIC_ALBUM_STRALBUMTHUMB, albums.getAlbum().get(0).getStrAlbumThumb());
			cv.put(MUSIC_ALBUM_STRDESCRIPTIONEN, albums.getAlbum().get(0).getStrDescriptionEN());
			cv.put(MUSIC_ALBUM_STRREVIEW, albums.getAlbum().get(0).getStrReview());
			cv.put(MUSIC_ALBUM_ISFAV, isFav);
			cv.put(MUSIC_ALBUM_ISACTIVE, isActive);
			cv.put(MUSIC_ALBUM_TIMESTAMP, now.toString());
			
			int rows = db.update(TABLE_MUSIC_ALBUM, cv, MUSIC_ALBUM_IDALBUM + " = '" + albums.getAlbum().get(0).getIdAlbum() + "'", null);			
			if (rows == 0)
			{
				int rowId = (int)db.insert(TABLE_MUSIC_ALBUM, null, cv);
				if (rowId != -1)
				{
					Log.v("ALBUM INSERTION", "Successfull");
				}
			}
			closeDB();
		}

		
		public void addMusicArtist(MusicArtist artists, int isFav, int isActive){
			String query = "";
			SQLiteDatabase db = this.getWritableDatabase();
			Time now = new Time();
			now.setToNow();
			
			ContentValues cv = new ContentValues();
			cv.put(MUSIC_ARTIST_IDARTIST, artists.getArtists().get(0).getIdArtist());
			cv.put(MUSIC_ARTIST_STRARTIST, artists.getArtists().get(0).getStrArtist());
			cv.put(MUSIC_ARTIST_INTFORMEDYEAR, artists.getArtists().get(0).getIntFormedYear());
			cv.put(MUSIC_ARTIST_INTBORNYEAR, artists.getArtists().get(0).getIntBornYear());
			cv.put(MUSIC_ARTIST_STRGENRE, artists.getArtists().get(0).getStrGenre());
			cv.put(MUSIC_ARTIST_STRSUBGENRE, artists.getArtists().get(0).getStrSubGenre());
			cv.put(MUSIC_ARTIST_STRMOOD, artists.getArtists().get(0).getStrMood());
			cv.put(MUSIC_ARTIST_STRWEBSITE, artists.getArtists().get(0).getStrWebsite());
			cv.put(MUSIC_ARTIST_STRFACEBOOK, artists.getArtists().get(0).getStrFacebook());
			cv.put(MUSIC_ARTIST_STRTWITTER, artists.getArtists().get(0).getStrTwitter());
			cv.put(MUSIC_ARTIST_STRBIOGRAPHYEN, artists.getArtists().get(0).getStrBiographyEN());
			cv.put(MUSIC_ARTIST_STRCOUNTRY, artists.getArtists().get(0).getStrCountry());
			cv.put(MUSIC_ARTIST_STRGENDER, artists.getArtists().get(0).getStrGender());
			cv.put(MUSIC_ARTIST_INTMEMBERS, artists.getArtists().get(0).getIntMembers());
			cv.put(MUSIC_ARTIST_STRARTISTTHUMB, artists.getArtists().get(0).getStrArtistThumb());
			cv.put(MUSIC_ARTIST_STRARTISTLOGO, artists.getArtists().get(0).getStrArtistLogo());
			cv.put(MUSIC_ARTIST_STRARTISTFANART, artists.getArtists().get(0).getStrArtistFanart());
			cv.put(MUSIC_ARTIST_STRARTISTFANART2, artists.getArtists().get(0).getStrArtistFanart2());
			cv.put(MUSIC_ARTIST_STRARTISTFANART3, artists.getArtists().get(0).getStrArtistFanart3());
			cv.put(MUSIC_ARTIST_STRARTISTBANNER, artists.getArtists().get(0).getStrArtistBanner());
			cv.put(MUSIC_ARTIST_ISFAV, isFav);
			cv.put(MUSIC_ARTIST_ISACTIVE, isActive);
			cv.put(MUSIC_ARTIST_TIMESTAMP, now.toString());
			
			int rows = db.update(TABLE_MUSIC_ARTIST, cv, MUSIC_ARTIST_IDARTIST + " = '" + artists.getArtists().get(0).getIdArtist() + "'", null);			
			if (rows == 0)
			{				
				int rowId = (int)db.insert(TABLE_MUSIC_ARTIST, null, cv);
				if (rowId != -1)
				{
					Log.v("ARTIST INSERTION", "Successfull");
				}
			}			
			closeDB();
		}		
		
		public MusicTracks getAllMusicTracks()
		{
			MusicTracks allTracks = new MusicTracks();
			List<Track> list = new ArrayList<Track>();
			String selectQuery = "SELECT * FROM " + TABLE_MUSIC_TRACK;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					Track track = new Track();
					track.setIdTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_IDTRACK)));
					track.setIdAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_IDALBUM)));
					track.setIdArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_IDARTIST)));
					track.setStrTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_STRTRACK)));
					track.setStrAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_STRALBUM)));
					track.setStrArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_STRARTIST)));
					track.setPath(c.getString(c.getColumnIndex(MUSIC_TRACK_PATH)));
					track.setIntDuration(c.getString(c.getColumnIndex(MUSIC_TRACK_INTDURATION)));					
					track.setStrMusicVid(c.getString(c.getColumnIndex(MUSIC_TRACK_STRMUSICVID)));
					track.setIsFav(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISFAV)));	
					track.setIsActive(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISACTIVE)));
					track.setInserttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_INSERTTIMESTAMP)));
					track.setRecenttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_RECENTTIMESTAMP)));
					
					list.add(track);					
					
				} while (c.moveToNext());
			}
			allTracks.setTrack(list);
			c.close();
			closeDB();
			return allTracks;
		}
		
		
		public MusicAlbums getAllMusicAlbums()
		{
			MusicAlbums allAlbums = new MusicAlbums();
			List<Album> list = new ArrayList<Album>();
			String selectQuery = "SELECT * FROM " + TABLE_MUSIC_ALBUM;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					Album album = new Album();
					album.setIdAlbum(c.getString(c.getColumnIndex(MUSIC_ALBUM_IDALBUM)));
					album.setIdArtist(c.getString(c.getColumnIndex(MUSIC_ALBUM_IDARTIST)));
					album.setStrAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_STRALBUM)));
					album.setStrArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_STRARTIST)));
					album.setIntYearReleased(c.getString(c.getColumnIndex(MUSIC_ALBUM_INTYEARRELEASED)));
					album.setStrGenre(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRGENRE)));
					album.setStrSubGenre(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRSUBGENRE)));
					album.setStrReleaseFormat(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRRELEASEFORMAT)));
					album.setIntSales(c.getString(c.getColumnIndex(MUSIC_ALBUM_INTSALES)));
					album.setStrAlbumThumb(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRALBUMTHUMB)));
					album.setStrDescriptionEN(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRDESCRIPTIONEN)));
					album.setStrReview(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRREVIEW)));
					album.setIsFav(c.getInt(c.getColumnIndex(MUSIC_ALBUM_ISFAV)));
					album.setIsActive(c.getInt(c.getColumnIndex(MUSIC_ALBUM_ISACTIVE)));
					album.setTimestamp(c.getString(c.getColumnIndex(MUSIC_ALBUM_TIMESTAMP)));					
					
					list.add(album);					
					
				} while (c.moveToNext());
			}
			allAlbums.setAlbum(list);
			c.close();
			closeDB();
			return allAlbums;
		}
		
		public MusicArtist getAllMusicArtists()
		{
			MusicArtist allArtists = new MusicArtist();
			List<Artist> list = new ArrayList<Artist>();
			String selectQuery = "SELECT * FROM " + TABLE_MUSIC_ARTIST;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					Artist artist = new Artist();
					artist.setIdArtist(c.getString(c.getColumnIndex(MUSIC_ARTIST_IDARTIST)));
					artist.setStrArtist(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTIST)));
					artist.setIntFormedYear(c.getString(c.getColumnIndex(MUSIC_ARTIST_INTFORMEDYEAR)));
					artist.setIntBornYear(c.getString(c.getColumnIndex(MUSIC_ARTIST_INTBORNYEAR)));
					artist.setStrGenre(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRGENRE)));
					artist.setStrSubGenre(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRSUBGENRE)));
					artist.setStrMood(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRMOOD)));
					artist.setStrWebsite(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRWEBSITE)));
					artist.setStrFacebook(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRFACEBOOK)));
					artist.setStrTwitter(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRTWITTER)));
					artist.setStrBiographyEN(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRBIOGRAPHYEN)));
					artist.setStrCountry(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRCOUNTRY)));
					artist.setStrGender(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRGENDER)));
					artist.setIntMembers(c.getString(c.getColumnIndex(MUSIC_ARTIST_INTMEMBERS)));
					artist.setStrArtistThumb(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTISTTHUMB)));
					artist.setStrArtistLogo(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTISTLOGO)));
					artist.setStrArtistFanart(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTISTFANART)));
					artist.setStrArtistFanart2(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTISTFANART2)));
					artist.setStrArtistFanart3(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTISTFANART3)));
					artist.setStrArtistBanner(c.getString(c.getColumnIndex(MUSIC_ARTIST_STRARTISTBANNER)));
					artist.setIsFav(c.getInt(c.getColumnIndex(MUSIC_ARTIST_ISFAV)));
					artist.setIsActive(c.getInt(c.getColumnIndex(MUSIC_ARTIST_ISACTIVE)));
					artist.setTimestamp(c.getString(c.getColumnIndex(MUSIC_ARTIST_TIMESTAMP)));
					
					list.add(artist);					
					
				} while (c.moveToNext());
			}
			allArtists.setArtists(list);
			c.close();
			closeDB();
			return allArtists;
		}
		
		public List<String> getUniqueGenre()
		{
			List<String> allGenres = new ArrayList<String>();
			String selectQuery = "SELECT DISTINCT "+ MUSIC_ALBUM_STRGENRE +" FROM " + TABLE_MUSIC_ALBUM;
			
			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			if (c.moveToFirst()) {
				do {
					String [] genres = (c.getString(c.getColumnIndex(MUSIC_ALBUM_STRGENRE))).split("/");
					for (int i=0; i<genres.length;i++)
					{
						allGenres.add(genres[i]);			
					}
				} while (c.moveToNext());
			}
			
			c.close();
			closeDB();
			return allGenres;
		}
		
		public List<Genre> getIdAlbumByGenre (String strGenre)
		{
			List<Genre> idAlbums = new ArrayList<Genre>();
			String selectQuery = "SELECT "+ MUSIC_ALBUM_STRGENRE +", "+ MUSIC_ALBUM_IDALBUM +", "+ MUSIC_ALBUM_IDARTIST +" FROM " + TABLE_MUSIC_ALBUM
					+ " WHERE "+ MUSIC_ALBUM_STRGENRE + " LIKE " + "'%" + strGenre + "%'";
			Log.e(LOG, selectQuery);
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
		
			if (c.moveToFirst()) {
				do {
						Genre genre = new Genre();
						genre.setIdAlbum(c.getString(c.getColumnIndex(MUSIC_ALBUM_IDALBUM)));
						genre.setIdArtist(c.getString(c.getColumnIndex(MUSIC_ALBUM_IDARTIST)));
						genre.setStrGenre(c.getString(c.getColumnIndex(MUSIC_ALBUM_STRGENRE)));
						idAlbums.add(genre);			
					
				} while (c.moveToNext());
			}
		
			c.close();
			closeDB();
		return idAlbums;			
		}
		
		public MusicTracks getTracksByIdArtist (String idArtist)
		{
			MusicTracks allTracks = new MusicTracks();
			List<Track> list = new ArrayList<Track>();
			String selectQuery = "SELECT * FROM " + TABLE_MUSIC_TRACK + " WHERE " + MUSIC_TRACK_IDARTIST + " = "+ idArtist;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					Track track = new Track();
					track.setIdTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_IDTRACK)));
					track.setIdAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_IDALBUM)));
					track.setIdArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_IDARTIST)));
					track.setStrTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_STRTRACK)));
					track.setStrAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_STRALBUM)));
					track.setStrArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_STRARTIST)));
					track.setPath(c.getString(c.getColumnIndex(MUSIC_TRACK_PATH)));
					track.setIntDuration(c.getString(c.getColumnIndex(MUSIC_TRACK_INTDURATION)));					
					track.setStrMusicVid(c.getString(c.getColumnIndex(MUSIC_TRACK_STRMUSICVID)));
					track.setIsFav(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISFAV)));	
					track.setIsActive(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISACTIVE)));	
					track.setInserttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_INSERTTIMESTAMP)));
					track.setRecenttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_RECENTTIMESTAMP)));
					
					list.add(track);					
					
				} while (c.moveToNext());
			}
			allTracks.setTrack(list);
			c.close();
			closeDB();
			return allTracks;
		}
		
		public MusicTracks getTracksByGenre (List<Genre> idAlbums)
		{
			
			MusicTracks allTracks = new MusicTracks();
			List<Track> list = new ArrayList<Track>();
			if (idAlbums.size() != 0)
			{
				String selectQuery = "SELECT * FROM " + TABLE_MUSIC_TRACK + " WHERE "+ MUSIC_TRACK_IDALBUM + " IN (";
			
				for (int i = 0; i < idAlbums.size(); i++)
				{
					selectQuery = selectQuery + idAlbums.get(i).getIdAlbum();
					if (i != idAlbums.size()-1)
						selectQuery = selectQuery + ", ";
				}
				selectQuery = selectQuery + ") ";

				Log.e(LOG, selectQuery);

				SQLiteDatabase db = this.getReadableDatabase();
				Cursor c = db.rawQuery(selectQuery, null);
			
				// looping through all rows and adding to list
				if (c.moveToFirst()) {
					do {
						Track track = new Track();
						track.setIdTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_IDTRACK)));
						track.setIdAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_IDALBUM)));
						track.setIdArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_IDARTIST)));
						track.setStrTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_STRTRACK)));
						track.setStrAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_STRALBUM)));
						track.setStrArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_STRARTIST)));
						track.setPath(c.getString(c.getColumnIndex(MUSIC_TRACK_PATH)));
						track.setIntDuration(c.getString(c.getColumnIndex(MUSIC_TRACK_INTDURATION)));					
						track.setStrMusicVid(c.getString(c.getColumnIndex(MUSIC_TRACK_STRMUSICVID)));
						track.setIsFav(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISFAV)));	
						track.setIsActive(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISACTIVE)));	
						track.setInserttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_INSERTTIMESTAMP)));
						track.setRecenttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_RECENTTIMESTAMP)));
					
						list.add(track);					
					
					} while (c.moveToNext());
				}
				allTracks.setTrack(list);			
				c.close();
			}
			closeDB();
			return allTracks;
		}
		
		public MusicTracks getTracksByIdAlbum (String idAlbum)
		{
			MusicTracks allTracks = new MusicTracks();
			List<Track> list = new ArrayList<Track>();
			String selectQuery = "SELECT * FROM " + TABLE_MUSIC_TRACK + " WHERE " + MUSIC_TRACK_IDALBUM + " = "+ idAlbum;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					Track track = new Track();
					track.setIdTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_IDTRACK)));
					track.setIdAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_IDALBUM)));
					track.setIdArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_IDARTIST)));
					track.setStrTrack(c.getString(c.getColumnIndex(MUSIC_TRACK_STRTRACK)));
					track.setStrAlbum(c.getString(c.getColumnIndex(MUSIC_TRACK_STRALBUM)));
					track.setStrArtist(c.getString(c.getColumnIndex(MUSIC_TRACK_STRARTIST)));
					track.setPath(c.getString(c.getColumnIndex(MUSIC_TRACK_PATH)));
					track.setIntDuration(c.getString(c.getColumnIndex(MUSIC_TRACK_INTDURATION)));					
					track.setStrMusicVid(c.getString(c.getColumnIndex(MUSIC_TRACK_STRMUSICVID)));
					track.setIsFav(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISFAV)));	
					track.setIsActive(c.getInt(c.getColumnIndex(MUSIC_TRACK_ISACTIVE)));
					track.setInserttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_INSERTTIMESTAMP)));
					track.setRecenttimestamp(c.getString(c.getColumnIndex(MUSIC_TRACK_RECENTTIMESTAMP)));
					
					list.add(track);					
				} while (c.moveToNext());
			}
			allTracks.setTrack(list);
			c.close();
			closeDB();
			return allTracks;
		}
		
		
		 
	
	public void addVideolist(List<Video> video){
		String query = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Time now = new Time();
		now.setToNow();
		for(int i=0;i<video.size();i++)
		{			
			ContentValues cv = new ContentValues();
			cv.put(KEY_FAV, video.get(i).isFav());
			cv.put(KEY_VIDEO_SUBCAT_ID, video.get(i).getSub_cat());
			cv.put(KEY_VIDEO_PATH, video.get(i).getPath());
			cv.put(KEY_VIDEO_NAME, video.get(i).getSourcename());
			cv.put(KEY_ACTIVE, video.get(i).isIsactive());
			cv.put(KEY_TIMESTAMP, now.toString());
			
			int rows = db.update(TABLE_VIDEO, cv, KEY_VIDEO_PATH + " = '" + video.get(i).getPath() + "'", null);			
			if (rows == 0)
			{
				query = "insert into "+ TABLE_VIDEO +" ("+ KEY_FAV +", "+ KEY_VIDEO_PATH +", "+ KEY_VIDEO_NAME +", "+ KEY_VIDEO_SUBCAT_ID +" , "+ KEY_ACTIVE +", key_timestamp) VALUES ('" + video.get(i).isFav() + "','" + video.get(i).getPath() + "','" + video.get(i).getSourcename() +"','"  + video.get(i).getSub_cat() + "','" + Boolean.toString( video.get(i).isIsactive()) + "','"+now.toString()+"')";
				db.execSQL(query);
			}
		}		
		
	}
	
//	public ContentValues initValues (List<String> param, List<String> values)
//	{
//		ContentValues cv = new ContentValues();
//		for (int i = 0; i < param.size(); i++)
//		{
//			cv.put(param.get(i), values.get(i));
//		}
//		return cv;
//	}

	public void addpictures(Picture_BLL item, int is_album, int active) {
		SQLiteDatabase db = this.getWritableDatabase();
		/*Time now = new Time();
		now.setToNow();*/
		String query ="insert into table_picture (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + item.isFav() + "','" + item.getPath() + "','" + item.getSourcename() +"',"  + is_album + "," + active + ",'testtime')"; 

		db.execSQL(query);

	}

	// add music
	/*public void addmusic(Music item, int is_album, int active) {
		SQLiteDatabase db = this.getWritableDatabase();


		String query ="insert into table_music (key_fav, key_path, key_source_name, key_isalbum ,  key_isactive, key_timestamp) VALUES ('" + item.isFav() + "','" + item.getPath() + "','" + item.getSourcename() +"',"  + is_album + "," + active + ",'testtime')"; 
		db.execSQL(query);
	}*/

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
		String selectQuery = "SELECT * FROM " + TABLE_VIDEO;

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

		c.close();
		closeDB();
		return lstVideo;
	}



	/*
	 * getting all videos by categories
	 * */
	public List<Video> getAllVideosByCategory(String category) {
		List<Video> lstVideo = new ArrayList<Video>();

		String selectQuery = "SELECT * FROM " + TABLE_VIDEO + 
				" WHERE " + TABLE_VIDEO + "." + KEY_VIDEO_SUBCAT_ID + 
				" = '" + category + "'";

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
			c.close();
			closeDB();
		}
		
		return lstVideo;
	}

	/*
	 * getting all music by Favourite or not
	 * */
	/*public List<Music> getAllFavMusic(int fav) {
		List<Music> lstMusic = new ArrayList<Music>();

		String selectQuery = "SELECT  * FROM " + "table_music" + 
				" WHERE " + "table_music" + "." + "key_fav" + 
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
	}*/

	/*
	 * getting all pictures by Favourite or not
	 * */
	public List<Picture_BLL> getAllFavPictures(int fav) {
		List<Picture_BLL> lstPic = new ArrayList<Picture_BLL>();

		String selectQuery = "SELECT  * FROM " + "table_picture" + 
				" WHERE " + "table_picture" + "." + KEY_FAV + 
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
//	public List<Album> getAllAlbums() {
//
//		List<Album> lstAlbum = new ArrayList<Album>();
//
//		String selectQuery = "SELECT  * FROM " + TABLE_PICTURE + 
//				" WHERE " + TABLE_PICTURE + "." + KEY_PICTURE_ALBUM + 
//				" = " + 1;
//
//
//		Log.e(LOG, selectQuery);
//
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor c = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (c.moveToFirst()) {
//			do {
//				Album album = new Album();
//				album.setName((c.getString(c.getColumnIndex(KEY_VIDEO_NAME))));
//				album.setPath((c.getString(c.getColumnIndex(KEY_VIDEO_PATH))));
//				album.setAllPictures(getPictureByAlbumName(album.getName()));
//
//				// adding to video list
//				lstAlbum.add(album);
//			} while (c.moveToNext());
//		}
//
//		return lstAlbum;
//	}
	// getting all music
	/*public List<Music> getAllMusic() {
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
	}*/


	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}



}
