package com.tvdashboard.music;

import com.tvdashboard.database.R;
import com.tvdashboard.database.R.id;
import com.tvdashboard.database.R.layout;
import com.tvdashboard.database.R.menu;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.music.manager.MusicTracks;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.ListView;

public class MusicArtistDetails extends Activity {

	ListView list;
    CustomAdapterMusicArtistDetails adapter;
    MusicArtistDetails musicArtistDetail = null;
    DatabaseHelper db;
    Context context;
    MusicTracks allTracks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_artist_details);		
		
		Resources res =getResources();		
		context = this.getApplicationContext();
		db = new DatabaseHelper(context);
		String idArtist = getIntent().getExtras().getString("idArtist");
		allTracks = db.getTracksByIdArtist(idArtist);
        list= ( ListView )findViewById( R.id.list );
        musicArtistDetail = this;
        adapter = new CustomAdapterMusicArtistDetails(allTracks,musicArtistDetail,res);
        list.setAdapter( adapter );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_artist_details, menu);
		return true;
	}
	
	public void onItemClick(int mPosition)
    {
    }

}
