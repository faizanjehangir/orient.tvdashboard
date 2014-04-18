package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;

public class FragmentComingUpMain extends Fragment {

	UpcomingAdapter imageAdapter;
	GridView gvUpcoming;
	public ArrayList<Show> upComingShows = new ArrayList<Show>();
	public ArrayList<Channel> channelList = new ArrayList<Channel>();

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
		// setlistdata
		setListData();
		gvUpcoming = (GridView) getActivity().findViewById(R.id.gridView);
		imageAdapter = new UpcomingAdapter(getActivity(), this.upComingShows, this.channelList, false);
		gvUpcoming.setAdapter(imageAdapter);
	}

	/****** Function to set data in ArrayList *************/
	public void setListData() {
		Iterator itSchedule = ScheduleManager.schedules.entrySet().iterator();
		while (itSchedule.hasNext()) {
			Map.Entry mEntry = (Map.Entry) itSchedule.next();
			ChannelSchedule mChannelSchedule = (ChannelSchedule) mEntry
					.getKey();
			upComingShows.add(ScheduleManager
					.getUpComingShow(ScheduleManager.schedules
							.get(mChannelSchedule)));
			channelList.add(ScheduleManager.channelData.get(mChannelSchedule));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_comingup_layout,
				container, false);
		return view;
	}

}
