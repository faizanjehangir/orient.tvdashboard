package com.tvdashboard.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class MusicFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
//    protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };

//    private int mCount = CONTENT.length;

    public MusicFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MusicFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 4;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//      return TestFragmentAdapter.CONTENT[position % CONTENT.length];
//    }

//    public void setCount(int count) {
//        if (count > 0 && count <= 10) {
//            mCount = count;
//            notifyDataSetChanged();
//        }
//    }

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}