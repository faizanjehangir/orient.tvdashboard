package com.tvdashboard.music;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.digitalaria.gama.wheel.Wheel;
import com.digitalaria.gama.wheel.WheelAdapter;
import com.digitalaria.gama.wheel.WheelAdapter.OnItemClickListener;
import com.orient.menu.animations.CollapseAnimationLTR;
import com.orient.menu.animations.CollapseAnimationRTL;
import com.orient.menu.animations.ExpandAnimationLTR;
import com.orient.menu.animations.ExpandAnimationRTL;
import com.orient.menu.animations.SampleList;
import com.tvdashboard.apps.AppSection;
import com.tvdashboard.channelsetup.SectionChannelSetup;
import com.tvdashboard.database.R;
import com.tvdashboard.database.R.layout;
import com.tvdashboard.database.R.menu;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.main.MainDashboard;
import com.tvdashboard.music.manager.MusicTracks;
import com.tvdashboard.pictures.PictureSection;
import com.tvdashboard.videos.VideoSection;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MusicAlbumDetails extends SherlockFragmentActivity {
	
	ListView list;
    CustomAdapterMusicAlbumDetails adapter;
    MusicAlbumDetails musicAlbumDetail = null;
    DatabaseHelper db;
    Context context;
    MusicTracks allTracks;
    private Wheel wheel;
    private RelativeLayout layoutDialer;
    private boolean isExpandedLeft;;
    private Resources res;
    private int[] icons = {
			R.drawable.home_off, R.drawable.channelsetup_off, R.drawable.videos_off,R.drawable.music_off,R.drawable.photos_off,
			R.drawable.apps_off, R.drawable.internet_off, R.drawable.settings_off};
    private int screenWidth, screenHeight;
    private ImageButton btnOpenleftmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SampleList.THEME);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    	getSupportActionBar().setDisplayShowTitleEnabled(false);
    	getSupportActionBar().setIcon(R.drawable.text_musictitle);
    	getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		setContentView(R.layout.activity_music_album_details);
		
		Resources res =getResources();		
		context = this.getApplicationContext();
		db = new DatabaseHelper(context);
		wheel = (Wheel) findViewById(R.id.wheel);
		btnOpenleftmenu = (ImageButton) findViewById(R.id.openLeft);
		String idAlbum = getIntent().getExtras().getString("idAlbum");
		allTracks = db.getTracksByIdAlbum(idAlbum);
        list= ( ListView )findViewById( R.id.list );
        layoutDialer = (RelativeLayout)findViewById(R.id.PieControlLayout);
        musicAlbumDetail = this;
        adapter = new CustomAdapterMusicAlbumDetails(allTracks,musicAlbumDetail,res);
        list.setAdapter( adapter );
        
        layoutDialer.setEnabled(false);
        init();
        layoutDialer.startAnimation(new CollapseAnimationLTR(layoutDialer, 0,(int)(screenWidth*1), 2));
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        
        
        
        wheel.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Toast.makeText(context, "key pressed: " + String.valueOf(keyCode), Toast.LENGTH_SHORT).show();
				
				
				if (keyCode == 19) // UP key pressed
				{
					if (event.getAction() == KeyEvent.ACTION_UP)
					wheel.previousItem();
				}
				else if (keyCode == 20) // DOWN key pressed
				{
					if (event.getAction() == KeyEvent.ACTION_UP)
					wheel.nextItem();
				}
				else if (keyCode == 66)
				{
					Intent intent;
					if (event.getAction() == KeyEvent.ACTION_UP)
						switch (wheel.getSelectedItem())
						{
						case 0:
							intent = new Intent(context, MainDashboard.class);
							startActivity(intent);							
							break;
							
						case 1:
							intent = new Intent(context, SectionChannelSetup.class);
							startActivity(intent);							
							break;
							
						case 2:
							intent = new Intent(context, VideoSection.class);
							startActivity(intent);							
							break;
							
						case 3: 
							intent = new Intent(context, MusicSection.class);
							startActivity(intent);								
							break;
							
						case 4: 
							intent = new Intent(context, PictureSection.class);
							startActivity(intent);								
							break;
								
						case 5:
							intent = new Intent(context, AppSection.class);
							startActivity(intent);							
							break;
							
						case 6:
							intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.novoda.com"));
							startActivity(intent);														
							break;
							
						case 7:
							startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);													
							break;
						}
				}
				return false;
			}
		});
        
        wheel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelAdapter<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position)
				{
				case 0:
					intent = new Intent(context, MainDashboard.class);
					startActivity(intent);							
					break;
					
				case 1:
					intent = new Intent(context, SectionChannelSetup.class);
					startActivity(intent);							
					break;
					
				case 2:
					intent = new Intent(context, VideoSection.class);
					startActivity(intent);							
					break;
					
				case 3: 
					intent = new Intent(context, MusicSection.class);
					startActivity(intent);								
					break;
					
				case 4: 
					intent = new Intent(context, PictureSection.class);
					startActivity(intent);								
					break;
						
				case 5:
					intent = new Intent(context, AppSection.class);
					startActivity(intent);							
					break;
					
				case 6:
					intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.novoda.com"));
					startActivity(intent);														
					break;
					
				case 7:
					startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);													
					break;
				}				
			}
		});
        
        btnOpenleftmenu.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (layoutDialer.getWidth() > 0) {
						btnOpenleftmenu.setNextFocusRightId(R.id.wheel);
						btnOpenleftmenu.setNextFocusDownId(R.id.wheel);
						btnOpenleftmenu.setNextFocusUpId(R.id.wheel);
					} else {
						btnOpenleftmenu.setNextFocusRightId(R.id.pager);
						btnOpenleftmenu.setNextFocusDownId(R.id.pager);
						btnOpenleftmenu.setNextFocusUpId(R.id.pager);
					}
				}
			}
		});
		
		btnOpenleftmenu.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) 
        	{
        		init();
        		if (isExpandedLeft) 
        		{
        			isExpandedLeft = false;
        			layoutDialer.startAnimation(new CollapseAnimationLTR(layoutDialer, 0,(int)(screenWidth*1), 2));
        			btnOpenleftmenu.setNextFocusRightId(R.id.pager);
        			btnOpenleftmenu.setNextFocusDownId(R.id.pager);
        			btnOpenleftmenu.setNextFocusUpId(R.id.pager);
        		}
        		else {
            		isExpandedLeft = true;            		
            		layoutDialer.startAnimation(new ExpandAnimationLTR(layoutDialer, 0,(int)(screenWidth*1), 2));
            		btnOpenleftmenu.setNextFocusRightId(R.id.wheel);
            		btnOpenleftmenu.setNextFocusDownId(R.id.wheel);
            		btnOpenleftmenu.setNextFocusUpId(R.id.wheel);
        		}
        	}
        });       
		
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;
        
        menu.add("Search")
        .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
        .setActionView(R.layout.collapsible_edittext)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        menu.add("")
    	.setIcon(R.drawable.network)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        menu.add("°C")
    	.setIcon(R.drawable.weather1)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        menu.add("00:00")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);        
        
        return true;
	}    
	
	private void init() 
	{
		res = getApplicationContext().getResources();
		wheel = (Wheel) findViewById(R.id.wheel);
		wheel.setItems(getDrawableFromData(icons));
		wheel.setWheelDiameter(450);
	}
	
	private Drawable[] getDrawableFromData(int[] data) 
	{
		Drawable[] ret = new Drawable[data.length];
		for (int i = 0; i < data.length; i++) 
		{
			ret[i] = res.getDrawable(data[i]);
		}
		return ret;
	}
	
	public void onItemClick(int mPosition)
    {
    }

}
