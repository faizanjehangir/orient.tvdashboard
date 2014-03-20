package com.tvdashboard.channelsetup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.main.FixedSpeedScroller;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

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

public class TabComingUp extends FragmentActivity implements OnPageChangeListener {

	ChannelSetupPageAdapter pageAdapter;
    private ViewPager mViewPager;
    PageIndicator mIndicator;
    int fragmentCounter=0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_channel_setup);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageMargin(-150);
        List<Fragment> fragments = getFragments();
        pageAdapter = new ChannelSetupPageAdapter(getSupportFragmentManager(), fragments);        
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(TabComingUp.this);
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

        FragmentComingUpMain f1 = FragmentComingUpMain.newInstance(String.valueOf(fragmentCounter));
        fragmentCounter++;
        fList.add(f1);

        return fList;
    }

}