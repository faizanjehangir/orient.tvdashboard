package com.tvdashboard.music;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.orient.menu.animations.CollapseAnimationRTL;
import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.main.FixedSpeedScroller;
import com.tvdashboard.music.manager.MusicAlbums;
import com.tvdashboard.music.manager.MusicArtist;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Interpolator;

public class TabArtist extends FragmentActivity implements OnPageChangeListener {

    public static MusicPageAdapter pageAdapter;
    public static  ViewPager mViewPager;
    public static PageIndicator mIndicator;
    public static MusicArtist allArtists;
    public static  DatabaseHelper db;
    public static  Context context;
    public static FragmentManager fm;
    public static int fragmentCounter=0;
    public static int numOfPages;
    
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_music_layout);
        
        context = this.getApplicationContext();
        db = new DatabaseHelper(context);
        fm = getSupportFragmentManager();
        
//        allArtists = db.getAllMusicArtists();
//        numOfPages = (int)Math.ceil(allArtists.getArtists().size()/(float)MusicSection.totalItems);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageMargin(-150);
//        List<Fragment> fragments = getFragments();
//        pageAdapter = new MusicPageAdapter(getSupportFragmentManager(), fragments);
//        mViewPager.setAdapter(pageAdapter);
        refresh();
        mViewPager.setOnPageChangeListener(TabArtist.this);
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
    
    @Override
	public void onBackPressed() {
    	if (MusicSection.isExpandedRight) {
    		MusicSection.isExpandedRight = false;
    		MusicSection.layoutRightMenu.startAnimation(new CollapseAnimationRTL(MusicSection.layoutRightMenu, (int)(MusicSection.screenWidth*0.5),(int)(MusicSection.screenWidth), 3, MusicSection.screenWidth));
		}
    	else
    	{
    		super.onBackPressed();
		}
	}

    private static List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        FragmentArtistMain [] f = new FragmentArtistMain[numOfPages];
        for (int i=0; i<numOfPages; i++)
        {
        	f[i] = new FragmentArtistMain().newInstance(String.valueOf(fragmentCounter));
        	fragmentCounter++;
        	fList.add(f[i]);
        }

        return fList;
    }
    
    public static void refresh ()
    {
    	allArtists = db.getAllMusicArtists();
        numOfPages = (int)Math.ceil(allArtists.getArtists().size()/(float)MusicSection.totalItems);
        List<Fragment> fragments = getFragments();
        pageAdapter = new MusicPageAdapter(fm, fragments);
        mViewPager.setAdapter(pageAdapter);
        pageAdapter.notifyDataSetChanged();
    }

}