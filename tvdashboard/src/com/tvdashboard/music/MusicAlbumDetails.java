package com.tvdashboard.music;

import com.tvdashboard.database.R;
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

public class MusicAlbumDetails extends Activity {
	
	ListView list;
    CustomAdapterMusicAlbumDetails adapter;
    MusicAlbumDetails musicAlbumDetail = null;
    DatabaseHelper db;
    Context context;
    MusicTracks allTracks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_album_details);
		
		Resources res =getResources();		
		context = this.getApplicationContext();
		db = new DatabaseHelper(context);
		String idAlbum = getIntent().getExtras().getString("idAlbum");
		allTracks = db.getTracksByIdAlbum(idAlbum);
        list= ( ListView )findViewById( R.id.list );
        musicAlbumDetail = this;
        adapter = new CustomAdapterMusicAlbumDetails(allTracks,musicAlbumDetail,res);
        list.setAdapter( adapter );
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_album_details, menu);
		return true;
	}
	
	public void onItemClick(int mPosition)
    {
    }

}
