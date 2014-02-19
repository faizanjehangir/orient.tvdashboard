package com.tvdashboard.viewpageradapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tvdashboard.main.FavoritesFragment;
import com.tvdashboard.main.RecentMusicFragment;
import com.tvdashboard.main.RecentPicturesFragment;
import com.tvdashboard.main.RecentVideosFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class DashboardViewpagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	
    public DashboardViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	if (position == 0)
    	{
    		return FavoritesFragment.newInstance();
    	}
    	else if (position == 1)
    	{
    		return RecentVideosFragment.newInstance();
    	}
    	else if (position == 2)
    	{
    		return RecentMusicFragment.newInstance();
    	}
    	else if (position == 3)
    	{
    		return RecentPicturesFragment.newInstance();
    	}
    	
    	return null;
    }

    @Override
    public int getCount() {
        return 4;
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