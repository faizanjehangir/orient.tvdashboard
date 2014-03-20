package com.tvdashboard.channelsetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tvdashboard.database.R;

public class FragmentComingUpMain extends Fragment{
	
	public static FragmentComingUpMain newInstance(String num) {
		FragmentComingUpMain fragment = new FragmentComingUpMain();

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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_comingup_layout, container, false);
		return view;
	}	
	

}
