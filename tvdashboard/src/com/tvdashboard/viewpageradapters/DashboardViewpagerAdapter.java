package com.tvdashboard.viewpageradapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tvdashboard.main.FragmentFavorites;
import com.tvdashboard.main.FragmentRecentMusic;
import com.tvdashboard.main.FragmentRecentPictures;
import com.tvdashboard.main.FragmentRecentVideos;
import com.viewpagerindicator.IconPagerAdapter;

public class DashboardViewpagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	
    public DashboardViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	if (position == 1)
    	{
    		return FragmentFavorites.newInstance();
    	}
    	else if (position == 2)
    	{
    		return FragmentRecentVideos.newInstance();
    	}
    	else if (position == 3)
    	{
    		return FragmentRecentMusic.newInstance();
    	}
    	else if (position == 4)
    	{
    		return FragmentRecentPictures.newInstance();
    	}
    	
    	return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//      return TestFragmentAdapter.CONTENT[position % CONTENT.length];
//    }

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}