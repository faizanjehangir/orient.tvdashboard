package com.tvdashboard.channelsetup;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tvdashboard.database.R;

public class FragmentTvGuideMain extends Fragment{
	
	ListView list;
	CustomListAdapter adapter;
//    public  FragmentTvGuideMain CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
		
	public static FragmentTvGuideMain newInstance(String num) {
		FragmentTvGuideMain fragment = new FragmentTvGuideMain();

		Bundle b = new Bundle();
        b.putString("fragment#", num);
        fragment.setArguments(b);
		
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");
		
		setListData();         
        Resources res =getResources();
        list= ( ListView )getActivity().findViewById( R.id.list );
        adapter=new CustomListAdapter( getActivity(), CustomListViewValuesArr,res );
        list.setAdapter( adapter );
        
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {      
        
		super.onCreate(savedInstanceState);		
	}
	
	/****** Function to set data in ArrayList *************/
    public void setListData()
    {         
		for (int i = 0; i < 11; i++) {

			final ListModel sched = new ListModel();
			sched.setChannelNum(String.valueOf(i));
			sched.setChannelName("PTV");
			CustomListViewValuesArr.add(sched);
		}         
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_tvguide_layout, container, false);
		return view;
	}	
	

}
