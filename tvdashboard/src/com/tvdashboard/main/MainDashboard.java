package com.tvdashboard.main;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.Date;

import org.json.JSONException;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.digitalaria.gama.wheel.Wheel;
import com.digitalaria.gama.wheel.WheelAdapter;
import com.digitalaria.gama.wheel.WheelAdapter.OnItemClickListener;
import com.mstar.tv.service.aidl.EN_MEMBER_SERVICE_TYPE;
import com.mstar.tv.service.interfaces.ITvServiceServer;
import com.mstar.tv.service.interfaces.ITvServiceServerCommon;
import com.orient.menu.animations.CollapseAnimationLTR;
import com.orient.menu.animations.ExpandAnimationLTR;
import com.orient.menu.animations.SampleList;
import com.tvdashboard.database.R;
import com.tvdashboard.weather.GPSTracker;
import com.tvdashboard.weather.JSONWeatherParser;
import com.tvdashboard.weather.Weather;
import com.tvdashboard.weather.WeatherHttpClient;
import com.tvos.common.TvManager;
import com.tvos.common.TvPlayer;
import com.tvos.common.exception.TvCommonException;
import com.tvos.common.vo.*;
import com.tvos.common.vo.TvOsType.EnumInputSource;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.tvdashboard.viewpageradapters.DashboardViewpagerAdapter;
import android.support.v4.view.ViewPager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.RelativeLayout;

public class MainDashboard extends SherlockFragmentActivity {

	private ImageButton openButtonLeft;
	private int screenWidth, screenHeigt;
	private boolean isExpandedLeft;
	RelativeLayout layoutDialer;
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
	boolean isFullScale = false;
	boolean isTVSource = false;
	public static  String weatherParam="";
	public static Menu menu=null;
	public static String currTime;
	public static Weather weather;
	
	DashboardViewpagerAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SampleList.THEME);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    	getSupportActionBar().setDisplayShowTitleEnabled(false);
    	getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		setContentView(R.layout.main);
		
		wheel = (Wheel) findViewById(R.id.wheel);
		res = getApplicationContext().getResources();
        context = this.getApplicationContext();
        openButtonLeft = (ImageButton) findViewById(R.id.openLeft);
        layoutDialer = (RelativeLayout)findViewById(R.id.PieControlLayout);
        
        wheel.requestFocus();
        isExpandedLeft = true;
        layoutDialer.setEnabled(false);
        init();
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeigt = metrics.heightPixels;
        
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation())
		{
        	String country = gpsTracker.getCountryName(this);
        	String city = gpsTracker.getLocality(this);
        	weatherParam = city+","+country;
		}
        else
		{
			gpsTracker.showSettingsAlert();
		}
        
// *********************** Timer Thread ***************************** //        
        
        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();
        
// *********************** Weather Api ****************************** //
        
        if (weatherParam != "")
        {
        	new JSONWeatherTask().execute(weatherParam);
        }
        
// ****************************************************************** //   
        
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
					if (event.getAction() == KeyEvent.ACTION_UP)
						switch (wheel.getSelectedItem())
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
				}
				return false;
			}
		});
        
        wheel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelAdapter<?> parent, View view,
					int position, long id) {
				
				Toast.makeText(context, "item clicked: " + position, Toast.LENGTH_SHORT).show();
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
			}
		});
        
        openButtonLeft.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (layoutDialer.getWidth() > 0) {
						openButtonLeft.setNextFocusRightId(R.id.wheel);
						openButtonLeft.setNextFocusDownId(R.id.wheel);
						openButtonLeft.setNextFocusUpId(R.id.wheel);
					} else {
						openButtonLeft.setNextFocusRightId(R.id.pager);
						openButtonLeft.setNextFocusDownId(R.id.pager);
						openButtonLeft.setNextFocusUpId(R.id.pager);
					}
				}
			}
		});
        
        openButtonLeft.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) 
        	{
        		init();
        		if (isExpandedLeft) 
        		{
        			isExpandedLeft = false;
        			layoutDialer.startAnimation(new CollapseAnimationLTR(layoutDialer, 0,(int)(screenWidth*1), 2));
        			openButtonLeft.setNextFocusRightId(R.id.pager);
        			openButtonLeft.setNextFocusDownId(R.id.pager);
        			openButtonLeft.setNextFocusUpId(R.id.pager);
        		}
        		else {        			
            		isExpandedLeft = true;            		
            		layoutDialer.startAnimation(new ExpandAnimationLTR(layoutDialer, 0,(int)(screenWidth*1), 2));
            		openButtonLeft.setNextFocusRightId(R.id.wheel);
        			openButtonLeft.setNextFocusDownId(R.id.wheel);
        			openButtonLeft.setNextFocusUpId(R.id.wheel);
        		}
        	}
        });
        
        
        
//		try {
//
//			this.panel_height = TvManager.getPictureManager()
//					.getPanelWidthHeight().height;
//			this.panel_width = TvManager.getPictureManager()
//					.getPanelWidthHeight().width;
//		} catch (TvCommonException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	         
//        btnHDMIOne = (Button) findViewById(R.id.btnHDMIOne);
//        btnHDMITwo = (Button) findViewById(R.id.btnHDMITwo);
//        btnTV = (Button) findViewById(R.id.btnTV);
//        
//        surfaceLayout = (FrameLayout) findViewById(R.id.surframelayout);
//        
//        surfaceLayout.setOnClickListener(new OnClickListener() {
//			
//        	@Override
//			public void onClick(View arg0) {
//        		Log.v("onclick called", "framelayout");
//				// TODO Auto-generated method stub
//				if (surfaceView != null){
//					Log.v("onclick called", "surfacenotnull");
//					setFullscale();
//				}
//			}
//		});        
        
        
//        FragmentTransaction t = getFragmentManager().beginTransaction();
//		FavoritesFragment favoritesFragment = new FavoritesFragment();
//		t.add(dashboard_content.getId(), favoritesFragment, "FavoritesFragment");
// 
//		RecentVideosFragment recentVideosFragment = new RecentVideosFragment();
//		t.add(dashboard_content.getId(), recentVideosFragment, "RecentVideosFragment");
//		
//		RecentMusicFragment recentmusicFragment = new RecentMusicFragment();
//		t.add(dashboard_content.getId(), recentmusicFragment, "RecentMusicFragment");
//		
//		RecentPicturesFragment recentpicturesFragment = new RecentPicturesFragment();
//		t.add(dashboard_content.getId(), recentpicturesFragment, "RecentPicturesFragment");
//		
//		t.commit();
        
        mAdapter = new DashboardViewpagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setPageMargin(-400);
        mPager.setHorizontalFadingEdgeEnabled(true);
//        mPager.setFadingEdgeLength(30);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
        try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Interpolator sInterpolator = null;
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mPager.getContext(), sInterpolator);
			// scroller.setFixedDuration(5000);
			mScroller.set(mPager, scroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
        
        mPager.setPageTransformer(false, new ViewPager.PageTransformer(){
			@Override
			public void transformPage(View page, float position) {				
				
				final float normalizedposition = Math.abs(Math.abs(position) - 1);
			    page.setScaleX(normalizedposition / 2 + 0.5f);
			    page.setScaleY(normalizedposition / 2 + 0.5f);
			    page.setAlpha(normalizedposition);				
//				page.setRotationY(position * -30);				
			}        	
        });	        
        
        
        
//        btnTV.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				isTVSource = true;
//				try {
//					Log.v("tvsource", TvManager.getCurrentInputSource()
//							.toString());
//					//TvManager.setInputSource(EnumInputSource.E_INPUT_SOURCE_ATV);
//					ITvServiceServer localITvServiceServer = ITvServiceServer.Stub.asInterface(ServiceManager.checkService("tv_services"));
//					try {
//						ITvServiceServerCommon localITvServiceServerCommon = localITvServiceServer.getCommonManager();
//						int k = localITvServiceServer.getChannelManager().getCurrentChannelNumber();
//						localITvServiceServer.getChannelManager().programSel(k, EN_MEMBER_SERVICE_TYPE.E_SERVICETYPE_ATV);
//					} catch (RemoteException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					handlerTV = new Handler();
////					ClearTVManager_RestorSTR();
//					if (surfaceView != null) {
//						surfaceView.getHolder().removeCallback(callback);
//						surfaceView = null;
//					}
////					handlerTV.removeCallbacks(handlerRuntv);
//					handlerTV.postDelayed(handlerRuntv, 300L);	
//
//				} catch (TvCommonException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//        
//		btnHDMIOne.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				isTVSource = false;
//				try {
//					Log.v("tvsource", TvManager.getCurrentInputSource()
//							.toString());
//					TvManager
//							.setInputSource(EnumInputSource.E_INPUT_SOURCE_HDMI);
////					if (surfaceView != null) {
////						surfaceView.getHolder().removeCallback(callback);
////						surfaceView = null;
////					}
////					handlerTV.postDelayed(handlerRuntv, 300L);
//					//setPipscale();
//					if (surfaceView != null) {
//						surfaceView.getHolder().removeCallback(callback);
//						surfaceView = null;
//					}
////					handlerTV.removeCallbacks(handlerRuntv);
//					handlerTV.postDelayed(handlerRuntv, 300L);	
//
//				} catch (TvCommonException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		btnHDMITwo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				isTVSource = false;
//				try {
//					Log.v("tvsource", TvManager.getCurrentInputSource()
//							.toString());
//					TvManager
//							.setInputSource(EnumInputSource.E_INPUT_SOURCE_HDMI2);
////					if (surfaceView != null) {
////						surfaceView.getHolder().removeCallback(callback);
////						surfaceView = null;
////					}
////					handlerTV.postDelayed(handlerRuntv, 300L);
//					//setPipscale();
//					if (surfaceView != null) {
//						surfaceView.getHolder().removeCallback(callback);
//						surfaceView = null;
//					}
////					handlerTV.removeCallbacks(handlerRuntv);
//					handlerTV.postDelayed(handlerRuntv, 300L);	
//
//				} catch (TvCommonException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});

	}
	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		if (this.surfaceView != null)
//	    {
//	      this.surfaceView.getHolder().removeCallback(this.callback);
//	      this.surfaceView = null;
//	    }
//	}
	
	


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
	
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
//		Toast.makeText(context, menu.getItem(2).getTitle(), Toast.LENGTH_SHORT).show();
		this.menu = menu;
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
	
//	public class InitializeDialer extends AsyncTask<Void, Void, Void>
//	{
//		@Override
//		protected Void doInBackground(Void... params) {			
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			init();
//			super.onPostExecute(result);
//		}		
//	}
	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		if (this.powerFirstOn){
//			this.powerFirstOn = false;
//			ComponentName localComponentName = new ComponentName("mstar.tvsetting.ui", "mstar.tvsetting.ui.RootActivity");
//	        Intent localIntent = new Intent("android.intent.action.MAIN");
//	        localIntent.addCategory("android.intent.category.LAUNCHER");
//	        localIntent.setComponent(localComponentName);
//	        localIntent.setFlags(270532608);
//	        MainDashboard.this.startActivity(localIntent);
//		}
//		else{
//			this.handlerTV.postDelayed(this.handlerRuntv, 300L);
//			
//		}
//	}
//	
//	Runnable handlerRuntv = new Runnable() {
//		public void run() {
//			try {
//				MainDashboard.this.surfaceView = new SurfaceView(
//						MainDashboard.this.getApplicationContext());
//				MainDashboard.this.openSurfaceView();
//				MainDashboard.this.setPipscale();
//				MainDashboard.this.handlerTV
//						.removeCallbacks(MainDashboard.this.handlerRuntv);
//			} catch (Exception localException) {
//				localException.printStackTrace();
//			}
//		}
//	};
//	
//	private void setPipscale() {
//		if (this.isFullScale){
//			this.wm.removeView(this.surfaceLayout);
//			this.isFullScale = false;
//		}
//		
//		try {
//			VideoWindowType localVideoWindowType = new VideoWindowType();
//			if ((this.panel_width == 1366) && (this.panel_height == 768)) {
//				localVideoWindowType.x = (int) this.surfaceLayout.getX() + 10;
//				localVideoWindowType.y = (int) this.surfaceLayout.getY() + 15;
//				localVideoWindowType.width = 461;
//				localVideoWindowType.height = 248;
//			}
//			TvManager.getPictureManager().selectWindow(
//					TvOsType.EnumScalerWindow.E_MAIN_WINDOW);
//			TvManager.getPictureManager().setDisplayWindow(
//					localVideoWindowType);
//			TvManager.getPictureManager().scaleWindow();
//		} catch (TvCommonException localTvCommonException) {
//			localTvCommonException.printStackTrace();
//		}
//	}
//	
//	private void setFullscale() {
//		try {
//			this.isFullScale = true;
//			this.surfaceParams = new WindowManager.LayoutParams();
//			
//			this.surfaceParams.width = this.panel_width;
//			this.surfaceParams.height = this.panel_height;
//			Log.v("surface coordinates",this.surfaceParams.x + " " + this.surfaceParams.y);
//			this.surfaceParams.type = 2;
//			this.surfaceParams.flags = 24;
//			this.surfaceParams.gravity = 51;
//			//this.wm = ((WindowManager) getSystemService("window"));
//			VideoWindowType localVideoWindowType = new VideoWindowType();
//			localVideoWindowType.height = 65535;
//			localVideoWindowType.width = 65535;
//			localVideoWindowType.x = 65535;
//			localVideoWindowType.y = 65535;
//			TvManager.getPictureManager().selectWindow(
//					TvOsType.EnumScalerWindow.E_MAIN_WINDOW);
//			TvManager.getPictureManager()
//					.setDisplayWindow(localVideoWindowType);
//			TvManager.getPictureManager().scaleWindow();
//			this.surfaceLayout.removeView(this.surfaceView);
//			this.wm = ((WindowManager)getSystemService("window"));	
//			this.wm.addView(this.surfaceView, this.surfaceParams);
//		} catch (TvCommonException localTvCommonException) {
//			localTvCommonException.printStackTrace();
//		}
//	}
//	
//	private void openSurfaceView() {
//		if (this.isTVSource){
//			this.surfaceLayout.removeView(this.surfaceView);
//		}
//		this.surfaceView.getHolder().setType(3);
//		this.surfaceLayout.addView(this.surfaceView);
//		this.surfaceLayout.setBackgroundResource(R.drawable.selector_tv);
//
//		this.callback = new SurfaceHolder.Callback() {
//			public void surfaceChanged(
//					SurfaceHolder paramAnonymousSurfaceHolder,
//					int paramAnonymousInt1, int paramAnonymousInt2,
//					int paramAnonymousInt3) {
//				Log.v("LauncherActivity", "===surfaceChanged===");
//				MainDashboard.this.createSurface = true;
//			}
//
//			public void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder) {
//				try {
//					Log.v("LauncherActivity", "===surfaceCreated===");
//					TvManager.getPlayerManager().setDisplay(
//							paramAnonymousSurfaceHolder);
//				} catch (TvCommonException localTvCommonException) {
//					localTvCommonException.printStackTrace();
//				}
//			}
//
//			public void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder) {
//			}
//		};
//		this.surfaceView.getHolder().addCallback(this.callback);
////		this.wm.addView(this.surfaceView, this.surfaceParams);
//	}
	
	public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    int day = dt.getDay();
                    int month = dt.getMonth();
                    int year = dt.getYear();
                    currTime = hours + " : " + minutes;// + ":" + seconds;
                    String curDate = day + " " + new DateFormatSymbols().getMonths()[month-1];
                    menu.getItem(3).setTitle(currTime);
                }catch (Exception e) {}
            }
        });
    }

    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
    
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

		@Override
		protected Weather doInBackground(String... params) {
			weather = new Weather();
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				// Let's retrieve the icon
				weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;

	}
	@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);

			if (weather.iconData != null && weather.iconData.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
//				imgView.setImageBitmap(img);
				menu.getItem(2).setIcon(new BitmapDrawable(img));
			}
			
			menu.getItem(2).setTitle(Math.round((weather.temperature.getTemp() - 273.15)) + "°C		");
//			cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
//			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
//			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
//			hum.setText("" + weather.currentCondition.getHumidity() + "%");
//			press.setText("" + weather.currentCondition.getPressure() + " hPa");
//			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
//			windDeg.setText("" + weather.wind.getDeg() + "�");

		}
    }
	
    

}
