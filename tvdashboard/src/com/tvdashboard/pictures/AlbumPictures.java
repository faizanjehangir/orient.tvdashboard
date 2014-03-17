package com.tvdashboard.pictures;


import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
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
import com.tvdashboard.bll.Album;
import com.tvdashboard.bll.Picture;
import com.tvdashboard.database.R;
import com.tvdashboard.database.R.drawable;
import com.tvdashboard.database.R.id;
import com.tvdashboard.database.R.layout;
import com.tvdashboard.database.R.style;
import com.tvdashboard.helper.Media_source;
import com.tvdashboard.helper.Source;
import com.tvdashboard.main.FixedSpeedScroller;
import com.tvdashboard.main.SelectedDirectoryListFragment;
import com.tvdashboard.model.Picture_BLL;
import com.tvdashboard.music.MusicSection;
import com.tvdashboard.videos.TabMovies;
import com.tvdashboard.videos.TabMusicVideos;
import com.tvdashboard.videos.TabTVShows;
import com.tvdashboard.videos.VideoSection;
import com.tvdashboard.weather.GPSTracker;
import com.tvdashboard.weather.JSONWeatherParser;
import com.tvdashboard.weather.Weather;
import com.tvdashboard.weather.WeatherHttpClient;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class AlbumPictures extends SherlockFragmentActivity {

	public static TabHost tabHost;
	public static int tabCounter=0;

	public static Context context;
	private RelativeLayout layoutDialer;
	private ImageButton btnOpenleftmenu;
	private int screenWidth, screenHeight;
	private boolean isExpandedLeft;
	private Wheel wheel;
	private Resources res;
	private int[] icons = {
			R.drawable.apps, R.drawable.videos, R.drawable.music,
			R.drawable.pictures, R.drawable.browser, R.drawable.settings };

	public static Menu menu;
	public static String currTime;
	public static Weather weather;
	public static  String weatherParam="";

	int fragmentCounter=0;
	private ViewPager mViewPager;
	PicturesPageAdapter pageAdapter;
	PageIndicator mIndicator;
	
	private int thumbWidth = 200;
	private int thumbHeight = 170;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SampleList.THEME);

		Bundle b = this.getIntent().getExtras();
		int albumIndex = b.getInt("albumIndex");
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setIcon(R.drawable.text_picturestitle);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		setContentView(R.layout.activity_album_pictures);

		final Media_source m = Media_source.Picture;

		this.context = this.getApplicationContext();
		Source mSource = new Source(Media_source.Picture, context);		

		context = this.getApplicationContext();
		wheel = (Wheel) findViewById(R.id.wheel);
		res = getApplicationContext().getResources();
		btnOpenleftmenu = (ImageButton) findViewById(R.id.openLeft);
		layoutDialer = (RelativeLayout)findViewById(R.id.PieControlLayout);

		isExpandedLeft = true;
		layoutDialer.setEnabled(false);
		init();

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;

		//		GPSTracker gpsTracker = new GPSTracker(this);
		//        if (gpsTracker.canGetLocation())
		//		{
		//        	String country = gpsTracker.getCountryName(this);
		//        	String city = gpsTracker.getLocality(this);
		//        	weatherParam = city+","+country;
		//		}
		//        else
		//		{
		//			gpsTracker.showSettingsAlert();
		//		}

		// *********************** Timer Thread ***************************** //        

		Thread myThread = null;
		Runnable myRunnableThread = new CountDownRunner();
		myThread= new Thread(myRunnableThread);
		myThread.start();

		// *********************** Weather Api ****************************** //

		//        if (weatherParam != "")
		//        {
		//        	/*new JSONWeatherTask().execute(weatherParam);*/
		//        }

		// ****************************************************************** //		

		 
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setPageMargin(-150);
		List<Fragment> fragments = getFragments(albumIndex);
		pageAdapter = new PicturesPageAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pageAdapter);
		/*mViewPager.setOnPageChangeListener((OnPageChangeListener) this);*/
		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mViewPager);

		try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Interpolator sInterpolator = null;
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mViewPager.getContext(), sInterpolator);
			mScroller.set(mViewPager, scroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

		mViewPager.setPageTransformer(false, new ViewPager.PageTransformer(){
			@Override
			public void transformPage(View page, float position) {				

				final float normalizedposition = Math.abs(Math.abs(position) - 1);
				page.setScaleX(normalizedposition / 2 + 0.5f);
				page.setScaleY(normalizedposition / 2 + 0.5f);
				page.setAlpha(normalizedposition);				
				//				page.setRotationY(position * -30);				
			}        	
		});

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

	private List<Album> makealbums(){
		List<Album> albums = new ArrayList<Album>();

		String result="";
		String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA};
		Cursor cur = this.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, "");
		String prevAlbum = "";
		Album album = new Album();
		Picture pic = new Picture();

		if (cur.moveToFirst()) {

			String bucket;

			int bucketColumn = cur.getColumnIndex(
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			int bucketData = cur.getColumnIndex(
					MediaStore.Images.Media.DATA);
			do {
				// Get the field values
				pic = new Picture();
				bucket = cur.getString(bucketColumn);
				if(!bucket.equals(prevAlbum)){
					prevAlbum = bucket;
					albums.add(album);
					album = new Album();
					album.albumName = bucket;
					album.pics = new ArrayList<Picture>();
				}
				pic.imgName = cur.getString(bucketData);
				album.pics.add(pic);
				// Do something with the values.
				result += bucket+",";
			} while (cur.moveToNext());

		}

		return albums;
	}

	public void SetGridLayoutDim(GridLayout gridLayout, int width, int height) {
		int colCount = screenWidth / width; // subtraction from colspan
													// from right/left
		gridLayout.setColumnCount(colCount);
	}
	
	private List<Fragment> getFragments(int albumindex){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		int totalThumbs = 0;
		int cols = screenWidth/ Integer.parseInt(this.getString(R.string.thumbWidth));
		int rows = screenHeight/Integer.parseInt(this.getString(R.string.thumbHeight));;
		cols--;
		rows--;
		totalThumbs = cols * rows;
		
		List<Album> albums = makealbums();
		List<Picture> pics = albums.get(albumindex).pics;
		List<Fragment> fList = new ArrayList<Fragment>();
		fragmentCounter = pics.size()%totalThumbs==0?pics.size()/totalThumbs:(pics.size()/totalThumbs)+1;
		for(int i=0;i<fragmentCounter;i++){

			AlbumPicturesFragment fragAlbum = AlbumPicturesFragment.newInstance(i, albums.get(albumindex), screenWidth, screenHeight);
			fList.add(fragAlbum);
		}
		/*

        FragmentAlbumsMain f1 = FragmentAlbumsMain.newInstance(String.valueOf(fragmentCounter));

        fragmentCounter++;
        FragmentAlbumsMain f2 = FragmentAlbumsMain.newInstance(String.valueOf(fragmentCounter));
        fragmentCounter++;
        fList.add(f1);
        fList.add(f2);*/

		return fList;
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

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
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

	private void initializeDirectory()
	{
		SelectedDirectoryListFragment fragment = (SelectedDirectoryListFragment) getFragmentManager()
				.findFragmentById(R.id.directoryFragment);
		File file = new File (Environment.getExternalStorageDirectory().toString());
		SelectedDirectoryListFragment.file = new File(Environment.getExternalStorageDirectory().toString());
		fragment.refresh();
		SelectedDirectoryListFragment.view.setVisibility(View.VISIBLE);
	}

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
				menu.getItem(2).setIcon(new BitmapDrawable(img));
			}			
			menu.getItem(2).setTitle(Math.round((weather.temperature.getTemp() - 273.15)) + "°C		");

		}
	}


}
