package com.tvdashboard.main;

import com.tvdashboard.database.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecentPicturesFragment extends Fragment{
	
	public static RecentPicturesFragment newInstance() {
		RecentPicturesFragment fragment = new RecentPicturesFragment();

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
		View view = inflater.inflate(R.layout.recent_pictures_fragment_layout, container, false);
		return view;
	}	
	

}