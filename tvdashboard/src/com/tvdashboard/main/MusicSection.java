package com.tvdashboard.main;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.tvdashboard.database.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class MusicSection extends SherlockFragmentActivity {

	private Context context;
	private LinearLayout layoutRightMenu,layoutDirectory;
	private RelativeLayout layoutLeftMenu;
	private ImageButton btnOpenLeftMenu,btnOpenRightMenu,btnSelect, btnReturn, btnBrowse;
	public static EditText browseText;
	private int screenWidth;
	private boolean isExpandedLeft,isExpandedRight;
	private Wheel wheel;
	private Resources res;
	public static String dir="";
	SelectedDirectoryListFragment fragment;
    private int[] icons = {
    		R.drawable.apps, R.drawable.videos, R.drawable.music,
    		R.drawable.pictures, R.drawable.browser, R.drawable.settings };
    
    MusicFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SampleList.THEME);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    	getSupportActionBar().setDisplayShowTitleEnabled(false);
    	getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		setContentView(R.layout.music_section);
		
		context = this.getApplicationContext();
		wheel = (Wheel) findViewById(R.id.wheel);
		res = getApplicationContext().getResources();
        layoutLeftMenu = (RelativeLayout) findViewById(R.id.PieControlLayout);
        layoutDirectory = (LinearLayout)findViewById(R.id.DirectoryLayout);
        layoutRightMenu = (LinearLayout) findViewById(R.id.AddSourceLayout);
        btnOpenLeftMenu = (ImageButton) findViewById(R.id.openLeft);
        btnOpenRightMenu = (ImageButton) findViewById(R.id.AddSource);
        btnReturn = (ImageButton) findViewById(R.id.returnBtn);
        btnBrowse = (ImageButton)findViewById(R.id.btn_browse);
        btnSelect = (ImageButton)findViewById(R.id.okBtn);
        browseText = (EditText)findViewById(R.id.text_browse);
        
		fragment = new SelectedDirectoryListFragment();
        
		browseText.setText(dir);
		
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        
        init();
        layoutLeftMenu.startAnimation(new CollapseAnimationLTR(layoutLeftMenu, 0,(int)(screenWidth*1), 2));
        
        mAdapter = new MusicFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
        browseText.addTextChangedListener(new TextWatcher() 
        {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SelectedDirectoryListFragment fragment = (SelectedDirectoryListFragment) getFragmentManager()
                        .findFragmentById(R.id.directoryFragment);
				File file = new File (browseText.getText().toString());
				fragment.refresh();
				SelectedDirectoryListFragment.view.setVisibility(View.VISIBLE);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
		});
        
        browseText.setOnFocusChangeListener(new OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					initializeDirectory();
			}
			
			
		});
        
        btnReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isExpandedRight) {
        			isExpandedRight = false;
        			layoutRightMenu.startAnimation(new CollapseAnimationRTL(layoutRightMenu, (int)(screenWidth*0.4),(int)(screenWidth), 3, screenWidth));
        		}        		
			}
		});
        
        btnBrowse.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				initializeDirectory();
			}
		});
        
        btnSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSelect.setVisibility(View.GONE);
				layoutDirectory.setVisibility(View.GONE);
			}
		});
        
        wheel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelAdapter<?> parent, View view,
					int position, long id) {
				switch (position)
				{
				case 1:
					Intent intent = new Intent(context, VideoSection.class);
					startActivity(intent);
					
					break;
					
				case 2:
					Intent intent1 = new Intent(context, MusicSection.class);
					startActivity(intent1);
					
					break;
					
				case 3:
					Intent intent2 = new Intent(context, PictureSection.class);
					startActivity(intent2);
					
					break;
					
				case 4:
					Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.novoda.com"));
					startActivity(viewIntent);
					
					break;
					
				case 5: 
					startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
						
					break;
						
				case 0:
					Intent intent3 = new Intent(context, AppSection.class);
					startActivity(intent3);
					
					break;
				}
//				Toast.makeText(getApplicationContext(), "Item # " + String.valueOf(position) , Toast.LENGTH_SHORT).show();				
			}
		});


        btnOpenLeftMenu.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) 
        	{
        		init();
        		if (isExpandedLeft) 
        		{
        			isExpandedLeft = false;
        			layoutLeftMenu.startAnimation(new CollapseAnimationLTR(layoutLeftMenu, 0,(int)(screenWidth*1), 2));
        		}
        		else {
            		isExpandedLeft = true;            		
            		layoutLeftMenu.startAnimation(new ExpandAnimationLTR(layoutLeftMenu, 0,(int)(screenWidth*1), 2));
        		}
        	}
        });
        
        btnOpenRightMenu.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if (isExpandedRight) {
        			isExpandedRight = false;
        			layoutRightMenu.startAnimation(new CollapseAnimationRTL(layoutRightMenu, (int)(screenWidth*0.4),(int)(screenWidth), 3, screenWidth));
        		}else {
            		isExpandedRight= true;
            		layoutRightMenu.startAnimation(new ExpandAnimationRTL(layoutRightMenu, (int)(screenWidth),(int)(screenWidth*0.4), 3, screenWidth));
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
        
        menu.add("14°")
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
	
	private void initializeDirectory()
	{
		layoutDirectory.setVisibility(View.VISIBLE);
		btnSelect.setVisibility(View.VISIBLE);
		SelectedDirectoryListFragment fragment = (SelectedDirectoryListFragment) getFragmentManager()
                .findFragmentById(R.id.directoryFragment);
		File file = new File (Environment.getExternalStorageDirectory().toString());
		SelectedDirectoryListFragment.file = new File(Environment.getExternalStorageDirectory().toString());
		fragment.refresh();
		SelectedDirectoryListFragment.view.setVisibility(View.VISIBLE);
	}

}
