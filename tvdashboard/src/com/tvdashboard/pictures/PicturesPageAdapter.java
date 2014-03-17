package com.tvdashboard.pictures;
import java.util.List;

import com.viewpagerindicator.IconPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class PicturesPageAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private List<Fragment> fragments;

    public PicturesPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

	@Override
	public int getIconResId(int index) {

		return 0;
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /*Log.d(TAG, "destroy!");*/
    }
}