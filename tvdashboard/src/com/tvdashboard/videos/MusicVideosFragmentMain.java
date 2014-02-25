package com.tvdashboard.videos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvdashboard.database.R;

public class MusicVideosFragmentMain extends Fragment{
	
	public static MusicVideosFragmentMain newInstance() {
		MusicVideosFragmentMain fragment = new MusicVideosFragmentMain();

        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.music_videos_main_fragment_layout, container, false);
		return view;
	}	
	

}
