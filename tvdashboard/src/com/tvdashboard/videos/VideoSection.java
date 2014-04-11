package com.tvdashboard.videos;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.SherlockActivity;
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
import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.helper.Media_source;
import com.tvdashboard.helper.Source;
import com.tvdashboard.main.CustomOnItemSelectedListener;
import com.tvdashboard.main.SelectedDirectoryListFragment;
import com.tvdashboard.main.XmlParser;
import com.tvdashboard.model.Video;
import com.tvdashboard.music.MusicSection;
import com.tvdashboard.pictures.PictureSection;
import com.tvdashboard.weather.GPSTracker;
import com.tvdashboard.weather.JSONWeatherParser;
import com.tvdashboard.weather.Weather;
import com.tvdashboard.weather.WeatherHttpClient;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class VideoSection extends SherlockFragmentActivity implements OnTabChangeListener {
	
	public static TabHost tabHost;
	public static int tabCounter=0;
	
	public static Context context;
	private LinearLayout layoutRightMenu,layoutDirectory;
	private RelativeLayout layoutDialer;
	private ImageButton btnOpenleftmenu,/*btnOpenRightMenu,*/btnSelect, btnReturn, btnAddSource, btnBrowse;
	public static EditText browseText,txtAlbumName;
	private int screenWidth, screenHeight;
	private boolean isExpandedLeft,isExpandedRight;
	private Wheel wheel;
	private Resources res;
	public static String dir="";
	SelectedDirectoryListFragment fragment;
    private int[] icons = {
    		R.drawable.apps, R.drawable.videos, R.drawable.music,
    		R.drawable.pictures, R.drawable.browser, R.drawable.settings };    
    private static Menu menu;
    private static String currTime;
	private static Weather weather;
	private static  String weatherParam="";
	private static Spinner videoSource;
	private static String source;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SampleList.THEME);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    	getSupportActionBar().setDisplayShowTitleEnabled(false);
    	getSupportActionBar().setIcon(R.drawable.text_videostitle);
    	getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
		setContentView(R.layout.section_videos);
		
		context = this.getApplicationContext();
		
		Media_source m = null;
        m = m.Videos;		
        txtAlbumName = (EditText)findViewById(R.id.text_source_name);
		Source mSource = new Source(Media_source.Videos, context);
		
		wheel = (Wheel) findViewById(R.id.wheel);
		res = getApplicationContext().getResources();
        layoutDirectory = (LinearLayout)findViewById(R.id.DirectoryLayout);
        layoutRightMenu = (LinearLayout) findViewById(R.id.AddSourceLayout);
//        btnOpenRightMenu = (ImageButton) findViewById(R.id.AddSource);
        btnReturn = (ImageButton) findViewById(R.id.returnBtn);
        btnBrowse = (ImageButton)findViewById(R.id.btn_browse);
        btnSelect = (ImageButton)findViewById(R.id.okBtn);
        btnAddSource = (ImageButton)findViewById(R.id.btn_add_source);
        browseText = (EditText)findViewById(R.id.text_browse);
        btnOpenleftmenu = (ImageButton) findViewById(R.id.openLeft);
        layoutDialer = (RelativeLayout)findViewById(R.id.PieControlLayout);
        videoSource = (Spinner)findViewById(R.id.spinner_source);
        
		fragment = new SelectedDirectoryListFragment();
        
		browseText.setText(dir);
		layoutDirectory.setVisibility(View.GONE);
		btnSelect.setVisibility(View.INVISIBLE);					
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        
//        layoutRightMenu.startAnimation(new ExpandAnimationRTL(layoutRightMenu, (int)(screenWidth),(int)(screenWidth*0.5), 3, screenWidth));
        
        layoutDialer.startAnimation(new CollapseAnimationLTR(layoutDialer, 0,(int)(screenWidth*1), 2));
        layoutDialer.setEnabled(false);
        init();
        
        initVideoCategoriesSpinner();
        addListenerOnSpinnerItemSelection();
		
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
//        
//// *********************** Timer Thread ***************************** //        
//        
//        Thread myThread = null;
//        Runnable myRunnableThread = new CountDownRunner();
//        myThread= new Thread(myRunnableThread);
//        myThread.start();
//        
//// *********************** Weather Api ****************************** //
//        
//        if (weatherParam != "")
//        {
//        	new JSONWeatherTask().execute(weatherParam);
//        }
        
// ****************************************************************** //

        
     		tabHost = (TabHost) findViewById(android.R.id.tabhost);
     		LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
     	    mLocalActivityManager.dispatchCreate(savedInstanceState);
     	    tabHost.setup(mLocalActivityManager);
     		TabSpec tab1 = tabHost.newTabSpec("TV Shows");
     		TabSpec tab2 = tabHost.newTabSpec("Movies");
     		TabSpec tab3 = tabHost.newTabSpec("Music Videos");
     		TabSpec tab4 = tabHost.newTabSpec("File Explorer");
     		tab1.setIndicator("TV Shows");
     		tab1.setContent(new Intent(this, TabTVShows.class));
     		tab2.setIndicator("Movies");
     		tab2.setContent(new Intent(this, TabMovies.class));
     		tab3.setIndicator("Music Videos");
     		tab3.setContent(new Intent(this, TabMusicVideos.class));
     		tab4.setIndicator("File Explorer");
     		tab4.setContent(new Intent(this, TabFileExplorer.class));
     		tabHost.addTab(tab1);
     		tabHost.addTab(tab2);
     		tabHost.addTab(tab3);
     		tabHost.addTab(tab4);
     		
     		TextView x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
     	    x.setTextSize(22);
     	    x = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
    	    x.setTextSize(22);
    	    x = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
     	    x.setTextSize(22);
     	    x = (TextView) tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
    	    x.setTextSize(22);

     		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
     			public TabHost tabHost1 = tabHost;

     			@Override
     			public void onTabChanged(String tabId) {
     				int pos = this.tabHost1.getCurrentTab();
     				this.tabHost1.setCurrentTab(pos);
     			}
     		});
        
		wheel.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Toast.makeText(context,
						"key pressed: " + String.valueOf(keyCode),
						Toast.LENGTH_SHORT).show();

				if (keyCode == 19) // UP key pressed
				{
					if (event.getAction() == KeyEvent.ACTION_UP)
						wheel.previousItem();
				} else if (keyCode == 20) // DOWN key pressed
				{
					if (event.getAction() == KeyEvent.ACTION_UP)
						wheel.nextItem();
				} else if (keyCode == 66) {
					if (event.getAction() == KeyEvent.ACTION_UP)
						switch (wheel.getSelectedItem()) {
						case 1:
							Intent intent = new Intent(context,
									VideoSection.class);
							startActivity(intent);

							break;

						case 2:
							Intent intent1 = new Intent(context,
									MusicSection.class);
							startActivity(intent1);

							break;

						case 3:
							Intent intent2 = new Intent(context,
									PictureSection.class);
							startActivity(intent2);

							break;

						case 4:
							Intent viewIntent = new Intent(
									"android.intent.action.VIEW", Uri
											.parse("http://www.novoda.com"));
							startActivity(viewIntent);

							break;

						case 5:
							startActivityForResult(new Intent(
									android.provider.Settings.ACTION_SETTINGS),
									0);

							break;

						case 0:
							Intent intent3 = new Intent(context,
									AppSection.class);
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
				switch (position) {
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
					Intent viewIntent = new Intent(
							"android.intent.action.VIEW", Uri
									.parse("http://www.novoda.com"));
					startActivity(viewIntent);

					break;

				case 5:
					startActivityForResult(new Intent(
							android.provider.Settings.ACTION_SETTINGS), 0);

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
		
//		btnOpenRightMenu.setOnClickListener(new OnClickListener() {
//        	public void onClick(View v) {
//        		if (isExpandedRight) {
//        			isExpandedRight = false;
//        			layoutRightMenu.startAnimation(new CollapseAnimationRTL(layoutRightMenu, (int)(screenWidth*0.5),(int)(screenWidth), 3, screenWidth));
//        		}else {
//            		isExpandedRight= true;
//            		layoutRightMenu.startAnimation(new ExpandAnimationRTL(layoutRightMenu, (int)(screenWidth),(int)(screenWidth*0.5), 3, screenWidth));
//        		}
//        		}
//        });        
        
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
        			layoutRightMenu.startAnimation(new CollapseAnimationRTL(layoutRightMenu, (int)(screenWidth*0.5),(int)(screenWidth), 3, screenWidth));
        		}        		
			}
		});
        
        btnBrowse.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				layoutDirectory.setVisibility(View.VISIBLE);
				btnSelect.setVisibility(View.VISIBLE);
				initializeDirectory();
				SelectedDirectoryListFragment.calledBy = "VideoSection";
			}
		});
        
        btnSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSelect.setVisibility(View.GONE);
				layoutDirectory.setVisibility(View.GONE);
			}
		});	  
        
		btnAddSource.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				/* Source.selectStuff(m); */
//				ArrayList<String> ext = XmlParser.parseXml(context, "Videos");
//				String path = browseText.getText().toString();
//				/* Log.d("Files", "Path: " + path); */
//				File f = new File(path);
//				File file[] = f.listFiles();
//				/* Log.d("Files", "Size: "+ file.length); */
//				String filenames = "";
//
//				List<Video> pics = new ArrayList<Video>();
//				for (int i = 0; i < file.length; i++) {
//					if (file[i].isDirectory()) {
//						/* fileList.add(listFile[i]); */
//						/* getpicfile(file[i]); */
//
//					} else {
//						
//						for (int k = 0; k < ext.size(); k++)
//						{
//							if (file[i].getName().endsWith("." + ext.get(k).toLowerCase()))
//							{
//								Video vid = new Video();
//								vid.setFav(false);
//								vid.setIsactive(true);
//								vid.setSub_cat(source);
//								vid.setPath(file[i].getPath());
//								vid.setSourcename(txtAlbumName.getText().toString());
//								pics.add(vid);
//								
//								break;
//							}
//							
//						}
//					}
//					/* Log.d("Files", "FileName:" + file[i].getName()); */
//				}
//				Source mSource = new Source(Media_source.Picture, context);
//				mSource.insertVideoList(pics);
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
        
        menu.add("�C")
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
                    currTime = hours + " : " + minutes;
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
			menu.getItem(2).setTitle(Math.round((weather.temperature.getTemp() - 273.15)) + "�C		");
		}
    }

	@Override
	public void onTabChanged(String tabId) {
		
	}
	
	public void addListenerOnSpinnerItemSelection() {
		videoSource = (Spinner) findViewById(R.id.spinner_source);
		videoSource.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }
	
	public void initVideoCategoriesSpinner()
	{
		List<String> videoCategories = new ArrayList<String>();
		videoCategories.add("TV Shows");
		videoCategories.add("Movies");
		videoCategories.add("Music Videos");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, videoCategories);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		videoSource.setAdapter(dataAdapter);
	}
	
	class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			source = parent.getItemAtPosition(pos).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}

	}

}
