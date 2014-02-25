package com.tvdashboard.videos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvdashboard.database.R;

public class MoviesFragmentMain extends Fragment{
	
	public static MoviesFragmentMain newInstance() {
		MoviesFragmentMain fragment = new MoviesFragmentMain();

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
		View view = inflater.inflate(R.layout.movies_main_fragment_layout, container, false);
		return view;
	}	
	

}
