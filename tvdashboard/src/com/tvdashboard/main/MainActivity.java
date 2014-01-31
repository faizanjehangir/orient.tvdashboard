package com.tvdashboard.main;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.digitalaria.gama.wheel.Wheel;
import com.digitalaria.gama.wheel.WheelAdapter;
import com.digitalaria.gama.wheel.WheelAdapter.OnItemClickListener;
import com.orient.menu.animations.CollapseAnimationLTR;
import com.orient.menu.animations.ExpandAnimationLTR;
import com.orient.menu.animations.SampleList;
import com.tvdashboard.database.R;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout;

public class MainActivity extends SherlockActivity {

	private RelativeLayout MenuListLeft;
	private ImageButton openButtonLeft;
	private int screenWidth;
	private boolean isExpandedLeft;
	LinearLayout dashboard_content;
	Context context;
	public static String dir="";
	private Wheel wheel;
    private Resources res;
    private int[] icons = {
    		R.drawable.apps, R.drawable.videos, R.drawable.music,
    		R.drawable.pictures, R.drawable.browser, R.drawable.settings };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SampleList.THEME);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    	getSupportActionBar().setDisplayShowTitleEnabled(false);
    	getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		setContentView(R.layout.main);
		
		dashboard_content = (LinearLayout)findViewById(R.id.ContentFragmentLayout);
		wheel = (Wheel) findViewById(R.id.wheel);
        context = this.getApplicationContext();
        MenuListLeft = (RelativeLayout) findViewById(R.id.PieControlLayout);
        openButtonLeft = (ImageButton) findViewById(R.id.openLeft);
		
        
        FragmentTransaction t = getFragmentManager().beginTransaction();
		FavoritesFragment favoritesFragment = new FavoritesFragment();
		t.add(dashboard_content.getId(), favoritesFragment, "FavoritesFragment");
 
		RecentVideosFragment recentVideosFragment = new RecentVideosFragment();
		t.add(dashboard_content.getId(), recentVideosFragment, "RecentVideosFragment");
		t.commit();
		
		DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;        
        
		wheel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelAdapter<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), "Item # " + String.valueOf(position) , Toast.LENGTH_SHORT).show();
				
			}
		});        
        
        openButtonLeft.requestFocus();
        openButtonLeft.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) 
        	{
        		if (isExpandedLeft) 
        		{
        			isExpandedLeft = false;
        			MenuListLeft.startAnimation(new CollapseAnimationLTR(MenuListLeft, 0,(int)(screenWidth*1), 5));
        		}
        		else {
            		isExpandedLeft = true;
            		init();
            		MenuListLeft.startAnimation(new ExpandAnimationLTR(MenuListLeft, 0,(int)(screenWidth*1), 5));	
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
		wheel.setWheelDiameter(400);
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
	

}
