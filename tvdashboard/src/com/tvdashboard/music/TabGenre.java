package com.tvdashboard.music;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.main.FixedSpeedScroller;
import com.tvdashboard.music.manager.Genre;
import com.tvdashboard.music.manager.MusicAlbums;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Interpolator;

public class TabGenre extends FragmentActivity implements OnPageChangeListener {

    MusicPageAdapter pageAdapter;
    private ViewPager mViewPager;
    PageIndicator mIndicator;
    private DatabaseHelper db;
    private Context context;
    public static int numOfPages;
    int fragmentCounter=0;
    public static List<String> listGenre;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_music_layout);
        
        context = this.getApplicationContext();
        listGenre = new ArrayList<String>();
        db = new DatabaseHelper(context);
        
        listGenre = db.getUniqueGenre();        
        HashSet<String> listToSet = new HashSet<String>(listGenre);
        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
        numOfPages = (int)Math.ceil(listWithoutDuplicates.size()/(float)MusicSection.totalItems);     
        
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageMargin(-150);
        List<Fragment> fragments = getFragments();
        pageAdapter = new MusicPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(TabGenre.this);
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
        FragmentGenreMain [] f = new FragmentGenreMain[numOfPages];
        for (int i=0; i<numOfPages; i++)
        {
        	f[i] = new FragmentGenreMain().newInstance(String.valueOf(fragmentCounter));
        	fragmentCounter++;
        	fList.add(f[i]);
        }
        return fList;
    }

}