package com.tvdashboard.main;

import com.tvdashboard.database.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class FragmentFavorites extends Fragment{
	
	ImageButton image1;
	ImageButton overlayImage1;
	
	public static FragmentFavorites newInstance() {
        FragmentFavorites fragment = new FragmentFavorites();

        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
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
		View view = inflater.inflate(R.layout.fragment_favorites_layout, container, false);
		return view;
	}	
	

}
