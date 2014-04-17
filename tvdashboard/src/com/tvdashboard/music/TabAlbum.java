package com.tvdashboard.music;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.main.FixedSpeedScroller;
import com.tvdashboard.music.manager.MusicAlbums;
import com.tvdashboard.videos.FragmentTVShowsMain;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabAlbum extends FragmentActivity implements OnPageChangeListener {

	MusicPageAdapter pageAdapter;
    private ViewPager mViewPager;
    PageIndicator mIndicator;
    public static MusicAlbums allAlbums;
    private DatabaseHelper db;
    private Context context;
    public static int numOfPages;
    int fragmentCounter=0;
    
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_music_layout);
        
        context = this.getApplicationContext();
        db = new DatabaseHelper(context);
        
        allAlbums = db.getAllMusicAlbums();
        numOfPages = (int)Math.ceil(allAlbums.getAlbum().size()/(float)MusicSection.totalItems);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageMargin(-150);
        List<Fragment> fragments = getFragments();
        pageAdapter = new MusicPageAdapter(getSupportFragmentManager(), fragments);        
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(TabAlbum.this);
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
			    page.setScaleX(normalizedposition / 2 + 0.5f);
			    page.setScaleY(normalizedposition / 2 + 0.5f);
			    page.setAlpha(normalizedposition);				
//				page.setRotationY(position * -30);				
			}        	
        });
        
        
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

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        FragmentAlbumMain [] f = new FragmentAlbumMain[numOfPages];
        for (int i=0; i<numOfPages; i++)
        {
        	f[i] = new FragmentAlbumMain().newInstance(String.valueOf(fragmentCounter));
        	fragmentCounter++;
        	fList.add(f[i]);
        }

        return fList;
    }

}