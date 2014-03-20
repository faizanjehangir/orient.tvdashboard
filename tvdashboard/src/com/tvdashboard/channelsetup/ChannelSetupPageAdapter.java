package com.tvdashboard.channelsetup;
import java.util.List;

import com.viewpagerindicator.IconPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChannelSetupPageAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private List<Fragment> fragments;

    public ChannelSetupPageAdapter(FragmentManager fm, List<Fragment> fragments) {
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
}