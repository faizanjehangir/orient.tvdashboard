package com.tvdashboard.videos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tvdashboard.database.R;

public class FragmentTVShowsMain extends Fragment{
	
	public static LinearLayout layoutContent01;
	public static LinearLayout layoutOptions;
	
	public static FragmentTVShowsMain newInstance(String num) {
		FragmentTVShowsMain fragment = new FragmentTVShowsMain();
		
		Bundle b = new Bundle();
        b.putString("fragment#", num);
        fragment.setArguments(b);
        
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");
		if (num.equals("0"))
		{
			layoutContent01.setVisibility(View.GONE);
		}
		else
		{
			layoutOptions.setVisibility(View.GONE);
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_tvshows_layout, container, false);		
		layoutContent01 = (LinearLayout)view.findViewById(R.id.LayoutContent01);
		layoutOptions = (LinearLayout)view.findViewById(R.id.VideosOptions);
		return view;
	}	

}
