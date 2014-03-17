package com.tvdashboard.videos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.main.FixedSpeedScroller;
import com.tvdashboard.model.Video;
import com.tvdashboard.videos.TabMovies.getAllMovies;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabMusicVideos extends FragmentActivity implements OnPageChangeListener {

    VideosPageAdapter pageAdapter;
    private ViewPager mViewPager;
    List<Fragment> fragments;
    PageIndicator mIndicator;
    int fragmentCounter=0;
    private static int numOfPages; 
    
    private DatabaseHelper dbHelper;
	public static List<Video> allMusicVideos;
	
	Context context;
	    
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_videos_layout);

        context = this.getApplicationContext();        
        new getAllMusicVideos().execute();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DOWNLOAD_PROGRESS:
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Initializing ...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            return mProgressDialog;
        default:
        return null;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
    
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.mViewPager.getCurrentItem();
    }

    @Override
        public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments(int numOfPages){
        List<Fragment> fList = new ArrayList<Fragment>();
        FragmentMusicVideosMain [] f = new FragmentMusicVideosMain[numOfPages];

        for (int i=0; i<numOfPages; i++)
        {
        	f[i] = new FragmentMusicVideosMain().newInstance(String.valueOf(fragmentCounter));
        	fragmentCounter++;
        	fList.add(f[i]);
        }
        
        return fList;
    }
    
    public class getAllMusicVideos extends AsyncTask<Void, Void, Boolean>
	{
    	
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try 
			{
				allMusicVideos = dbHelper.getAllVideosByCategory("Music Videos");
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}

		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}
		
		@Override
		protected void onPostExecute(Boolean result) {			
			super.onPostExecute(result);
			
			if (result)
			{
				numOfPages = (int)Math.ceil(allMusicVideos.size()/12.0);
				
				
				mViewPager = (ViewPager) findViewById(R.id.viewpager);
		        mViewPager.setPageMargin(-150);
		        fragments = getFragments(numOfPages);       
		        pageAdapter = new VideosPageAdapter(getSupportFragmentManager(), fragments);
		        mViewPager.setAdapter(pageAdapter);
		        mViewPager.setOnPageChangeListener(TabMusicVideos.this);
		        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		        mIndicator.setViewPager(mViewPager);
		        
		        try {
					Field mScroller;
					mScroller = ViewPager.class.getDeclaredField("mScroller");
					mScroller.setAccessible(true);
					Interpolator sInterpolator = null;
					FixedSpeedScroller scroller = new FixedSpeedScroller(
							mViewPager.getContext(), sInterpolator);
					// scroller.setFixedDuration(5000);
					mScroller.set(mViewPager, scroller);
				} catch (NoSuchFieldException e) {
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
		        
		        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer(){
					@Override
					public void transformPage(View page, float position) {				
						
						final float normalizedposition = Math.abs(Math.abs(position) - 1);
//					    page.setScaleX(normalizedposition / 2 + 0.5f);
//					    page.setScaleY(normalizedposition / 2 + 0.5f);
					    page.setAlpha(normalizedposition);				
//						page.setRotationY(position * -30);				
					}        	
		        });
			}
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		}		
	}

}