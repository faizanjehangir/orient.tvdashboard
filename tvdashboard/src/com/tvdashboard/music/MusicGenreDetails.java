package com.tvdashboard.music;

import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.database.R.id;
import com.tvdashboard.database.R.layout;
import com.tvdashboard.database.R.menu;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.music.manager.Genre;
import com.tvdashboard.music.manager.MusicTracks;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.ListView;

public class MusicGenreDetails extends Activity {

	ListView list;
    CustomAdapterMusicGenreDetails adapter;
    MusicGenreDetails musicGenreDetail = null;
    DatabaseHelper db;
    Context context;
    MusicTracks allTracks;
    private static String strGenre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_artist_details);		
		
		Resources res =getResources();		
		context = this.getApplicationContext();
		db = new DatabaseHelper(context);
		strGenre = getIntent().getExtras().getString("strGenre");
		List<Genre> idAlbums = db.getIdAlbumByGenre(strGenre);
		allTracks = db.getTracksByGenre(idAlbums);
        list= ( ListView )findViewById( R.id.list );
        musicGenreDetail = this;
        adapter = new CustomAdapterMusicGenreDetails(allTracks,musicGenreDetail,res);
        list.setAdapter( adapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_genre_details, menu);
		return true;
	}
	
	public void onItemClick(int mPosition)
    {
    }
	
	public List<String> getIdAlbum()
	{
		List<String> listIdAlbums = new ArrayList<String>();
		
		
		return listIdAlbums;
	}

}
