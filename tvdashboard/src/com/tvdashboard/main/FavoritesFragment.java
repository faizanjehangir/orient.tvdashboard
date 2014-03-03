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

public class FavoritesFragment extends Fragment{
	
	ImageButton image1;
	ImageButton overlayImage1;
	
	public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();

        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		image1 = (ImageButton)getActivity().findViewById(R.id.image1);
		overlayImage1 = (ImageButton)getActivity().findViewById(R.id.OverlayImage1);
		image1.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					overlayImage1.setImageResource(R.drawable.test_xhdpi);
				}else{
				overlayImage1.setImageResource(android.R.color.transparent);
				}				
			}
		});
		
		overlayImage1.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if (hasFocus)
				{
				
				}
			}
		});
		
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
