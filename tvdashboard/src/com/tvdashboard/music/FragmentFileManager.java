package com.tvdashboard.music;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tvdashboard.database.R;

public class FragmentFileManager extends Fragment{
	
	public static FragmentFileManager newInstance(String num) {
		FragmentFileManager fragment = new FragmentFileManager();

		Bundle b = new Bundle();
        b.putString("fragment#", num);
        fragment.setArguments(b);
		
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_music_filemanager_layout, container, false);
		return view;
	}	
	

}
