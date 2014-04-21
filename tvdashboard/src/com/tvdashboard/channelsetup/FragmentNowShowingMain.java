package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;

public class FragmentNowShowingMain extends Fragment {

	NowShowingAdapter imageAdapter;
	GridView gvNowShowing;
	public ArrayList<Show> nowShowingList = new ArrayList<Show>();
	public ArrayList<Channel> channelList = new ArrayList<Channel>();
	private HashMap<Show, ChannelSchedule> hmSchedule = new HashMap<Show, ChannelSchedule>();

	public static FragmentNowShowingMain newInstance(String num) {
		FragmentNowShowingMain fragment = new FragmentNowShowingMain();

		Bundle b = new Bundle();
		b.putString("fragment#", num);
		fragment.setArguments(b);

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");
		super.onActivityCreated(savedInstanceState);
		setListData();
		gvNowShowing = (GridView) getActivity().findViewById(R.id.gridView);
		imageAdapter = new NowShowingAdapter(getActivity(),
				this.nowShowingList, this.channelList);
		gvNowShowing.setAdapter(imageAdapter);
		AsyncUpdateNowshowing task = new AsyncUpdateNowshowing(imageAdapter,
				this.nowShowingList, this.hmSchedule);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
			task.execute();
	}

	/****** Function to set data in ArrayList *************/
	public void setListData() {
		Iterator itSchedule = ScheduleManager.schedules.entrySet().iterator();
		while (itSchedule.hasNext()) {
			Map.Entry mEntry = (Map.Entry) itSchedule.next();
			ChannelSchedule mChannelSchedule = (ChannelSchedule) mEntry
					.getKey();
			Show npShow = ScheduleManager
					.getNowPlayingShow(ScheduleManager.schedules
							.get(mChannelSchedule));
			nowShowingList.add(npShow);
			channelList.add(ScheduleManager.channelData.get(mChannelSchedule));
			this.hmSchedule.put(npShow, mChannelSchedule);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_nowshowing_layout,
				container, false);
		return view;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v("view", "restored");
		super.onViewStateRestored(savedInstanceState);
	}

}
