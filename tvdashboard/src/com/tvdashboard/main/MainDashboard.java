package com.tvdashboard.main;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.digitalaria.gama.wheel.Wheel;
import com.digitalaria.gama.wheel.WheelAdapter;
import com.digitalaria.gama.wheel.WheelAdapter.OnItemClickListener;
import com.orient.menu.animations.CollapseAnimationLTR;
import com.orient.menu.animations.ExpandAnimationLTR;
import com.orient.menu.animations.SampleList;
import com.tvdashboard.database.R;
import com.tvos.common.TvManager;
import com.tvos.common.TvPlayer;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.*;
import com.tvos.common.vo.TvOsType.EnumInputSource;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout;

public class MainDashboard extends SherlockActivity {

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
    
    int panel_height;
	int panel_width;
    Handler handlerTV = new Handler();
	SurfaceHolder.Callback callback;
	SurfaceView surfaceView = null;
	WindowManager.LayoutParams surfaceParams;
	private WindowManager wm;
	private boolean createSurface = false;
	Button btnHDMIOne;
	Button btnHDMITwo;
	Button btnTV;
	public boolean powerFirstOn = true;
	FrameLayout surfaceLayout;
	TvPlayer player;
	
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
		res = getApplicationContext().getResources();
        context = this.getApplicationContext();
        MenuListLeft = (RelativeLayout) findViewById(R.id.PieControlLayout);
        openButtonLeft = (ImageButton) findViewById(R.id.openLeft);
        
        try {
        	
			this.panel_height = TvManager.getPictureManager().getPanelWidthHeight().height;
			this.panel_width = TvManager.getPictureManager().getPanelWidthHeight().width;
		} catch (TvCommonException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	         
        btnHDMIOne = (Button) findViewById(R.id.btnHDMIOne);
        btnHDMITwo = (Button) findViewById(R.id.btnHDMITwo);
        btnTV = (Button) findViewById(R.id.btnTV);
        
        surfaceLayout = (FrameLayout) findViewById(R.id.surframelayout);
        
        surfaceLayout.setOnClickListener(new OnClickListener() {
			
        	@Override
			public void onClick(View arg0) {
        		Log.v("onclick called", "framelayout");
				// TODO Auto-generated method stub
				if (surfaceView != null){
					Log.v("onclick called", "surfacenotnull");
					setFullscale();
				}
			}
		});
        
        init();
        
        FragmentTransaction t = getFragmentManager().beginTransaction();
		FavoritesFragment favoritesFragment = new FavoritesFragment();
		t.add(dashboard_content.getId(), favoritesFragment, "FavoritesFragment");
 
		RecentVideosFragment recentVideosFragment = new RecentVideosFragment();
		t.add(dashboard_content.getId(), recentVideosFragment, "RecentVideosFragment");
		
		RecentMusicFragment recentmusicFragment = new RecentMusicFragment();
		t.add(dashboard_content.getId(), recentmusicFragment, "RecentMusicFragment");
		
		RecentPicturesFragment recentpicturesFragment = new RecentPicturesFragment();
		t.add(dashboard_content.getId(), recentpicturesFragment, "RecentPicturesFragment");
		
		t.commit();
		
		DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        
        isExpandedLeft = true;
        
		wheel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelAdapter<?> parent, View view,
					int position, long id) {
				switch (position)
				{
				case 1:
					Intent intent = new Intent(context, VideoSection.class);
					startActivity(intent);
				}
//				Toast.makeText(getApplicationContext(), "Item # " + String.valueOf(position) , Toast.LENGTH_SHORT).show();				
			}
		});


        openButtonLeft.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) 
        	{
        		init();
        		if (isExpandedLeft) 
        		{
        			isExpandedLeft = false;
        			MenuListLeft.startAnimation(new CollapseAnimationLTR(MenuListLeft, 0,(int)(screenWidth*1), 2));
        		}
        		else {
            		isExpandedLeft = true;            		
            		MenuListLeft.startAnimation(new ExpandAnimationLTR(MenuListLeft, 0,(int)(screenWidth*1), 2));
        		}
        	}
        });
        
		btnTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Log.v("tvsource", TvManager.getCurrentInputSource()
							.toString());
					TvManager.setInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
					
					if (surfaceView != null) {
						surfaceView.getHolder().removeCallback(callback);
						surfaceView = null;
					}
					handlerTV.postDelayed(handlerRuntv, 300L);
					

				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
		btnHDMIOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Log.v("tvsource", TvManager.getCurrentInputSource()
							.toString());
					TvManager
							.setInputSource(EnumInputSource.E_INPUT_SOURCE_HDMI);
					if (surfaceView != null) {
						surfaceView.getHolder().removeCallback(callback);
						surfaceView = null;
					}
					handlerTV.postDelayed(handlerRuntv, 300L);

				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btnHDMITwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					Log.v("tvsource", TvManager.getCurrentInputSource()
							.toString());
					TvManager
							.setInputSource(EnumInputSource.E_INPUT_SOURCE_HDMI2);
					if (surfaceView != null) {
						surfaceView.getHolder().removeCallback(callback);
						surfaceView = null;
					}
					handlerTV.postDelayed(handlerRuntv, 300L);

				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (this.surfaceView != null)
	    {
	      this.surfaceView.getHolder().removeCallback(this.callback);
	      this.surfaceView = null;
	    }
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
        
        menu.add("14�")
    	.setIcon(R.drawable.weather1)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        menu.add("00:00")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);        
        
        return true;
	}    
	
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
//		Toast.makeText(context, menu.getItem(3).getTitle(), Toast.LENGTH_SHORT).show();
		return super.onPrepareOptionsMenu(menu);
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
	
	public class InitializeDialer extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params) {			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			init();
			super.onPostExecute(result);
		}		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (this.powerFirstOn){
			this.powerFirstOn = false;
			ComponentName localComponentName = new ComponentName("mstar.tvsetting.ui", "mstar.tvsetting.ui.RootActivity");
	        Intent localIntent = new Intent("android.intent.action.MAIN");
	        localIntent.addCategory("android.intent.category.LAUNCHER");
	        localIntent.setComponent(localComponentName);
	        localIntent.setFlags(270532608);
	        MainDashboard.this.startActivity(localIntent);
		}
		else{
			this.handlerTV.postDelayed(this.handlerRuntv, 300L);
			
		}
	}
	
	Runnable handlerRuntv = new Runnable() {
		public void run() {
			try {
				MainDashboard.this.surfaceView = new SurfaceView(
						MainDashboard.this.getApplicationContext());
				MainDashboard.this.openSurfaceView();
				MainDashboard.this.setPipscale();
				
				try{
					if (TvManager.getCurrentInputSource().equals(EnumInputSource.E_INPUT_SOURCE_ATV))
						player.setDisplay(MainDashboard.this.surfaceView.getHolder());
				}
				catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MainDashboard.this.handlerTV
						.removeCallbacks(MainDashboard.this.handlerRuntv);
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}
	};
	
	private void setPipscale() {
		try {
			VideoWindowType localVideoWindowType = new VideoWindowType();
			if ((this.panel_width == 1366) && (this.panel_height == 768)) {
				localVideoWindowType.x = this.surfaceParams.x;
				localVideoWindowType.y = this.surfaceParams.y;
				localVideoWindowType.width = 461;
				localVideoWindowType.height = 248;
			}
			TvManager.getPictureManager().selectWindow(
					TvOsType.EnumScalerWindow.E_MAIN_WINDOW);
			TvManager.getPictureManager().setDisplayWindow(
					localVideoWindowType);
			TvManager.getPictureManager().scaleWindow();
		} catch (TvCommonException localTvCommonException) {
			localTvCommonException.printStackTrace();
		}
	}
	
	private void setFullscale() {
		try {
			this.surfaceParams = new WindowManager.LayoutParams();
			
			this.surfaceParams.width = this.panel_width;
			this.surfaceParams.height = this.panel_height;
			this.surfaceParams.type = 2;
			this.surfaceParams.flags = 24;
			this.surfaceParams.gravity = 51;
			this.wm.removeView(this.surfaceView);
			
			VideoWindowType localVideoWindowType = new VideoWindowType();
			localVideoWindowType.height = 65535;
			localVideoWindowType.width = 65535;
			localVideoWindowType.x = 65535;
			localVideoWindowType.y = 65535;
			TvManager.getPictureManager().selectWindow(
					TvOsType.EnumScalerWindow.E_MAIN_WINDOW);
			TvManager.getPictureManager()
					.setDisplayWindow(localVideoWindowType);
			TvManager.getPictureManager().scaleWindow();
			this.wm.addView(this.surfaceView, this.surfaceParams);
		} catch (TvCommonException localTvCommonException) {
			localTvCommonException.printStackTrace();
		}
	}
	
	private void openSurfaceView() {
		this.surfaceView.getHolder().setType(3);
		this.surfaceParams = new WindowManager.LayoutParams();
		
		this.surfaceParams.x = (int) findViewById(R.id.surframelayout).getX();
		this.surfaceParams.y = (int) findViewById(R.id.surframelayout).getY();
		Log.v("surface coordinates",this.surfaceParams.x + " " + this.surfaceParams.y);
		this.surfaceParams.width = findViewById(R.id.surframelayout).getWidth();
		this.surfaceParams.height = findViewById(R.id.surframelayout).getHeight();
		this.surfaceParams.type = 2;
		this.surfaceParams.flags = 24;
		this.surfaceParams.gravity = 51;
		this.callback = new SurfaceHolder.Callback() {
			public void surfaceChanged(
					SurfaceHolder paramAnonymousSurfaceHolder,
					int paramAnonymousInt1, int paramAnonymousInt2,
					int paramAnonymousInt3) {
				Log.v("LauncherActivity", "===surfaceChanged===");
				MainDashboard.this.createSurface = true;
			}

			public void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder) {
				try {
					Log.v("LauncherActivity", "===surfaceCreated===");
					TvManager.getPlayerManager().setDisplay(
							paramAnonymousSurfaceHolder);
					return;
				} catch (TvCommonException localTvCommonException) {
					localTvCommonException.printStackTrace();
				}
			}

			public void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder) {
			}
		};
		this.surfaceView.getHolder().addCallback(this.callback);
		this.wm = ((WindowManager) getSystemService("window"));
		this.wm.addView(this.surfaceView, this.surfaceParams);
	}

}
