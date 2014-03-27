package com.tvdashboard.channelsetup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;

public class FragmentTvGuideMain extends Fragment {

	ListView list;
	CustomListAdapter adapter;
	public static ImageButton[] imgBtnUpComing;
	public static int upComingSize = 20;
	public static ImageButton[] imgBtnNowPlaying;
	public static RelativeLayout[] rlImages;
	public static TextView[] txtShowDuration;
	public static TextView[] txtNowPlayingTitle;
	public static TextView[] txtUpComingTitle;
	public static ProgressBar[] progBarNowPlaying;
	public static Context context;
	public static GridView gridView;
	public static List<ViewHolder> viewHolder;
	// public FragmentTvGuideMain CustomListView = null;
	public ArrayList<Channel> CustomListViewValuesArr = new ArrayList<Channel>();

	public static FragmentTvGuideMain newInstance(String num) {
		FragmentTvGuideMain fragment = new FragmentTvGuideMain();

		Bundle b = new Bundle();
		b.putString("fragment#", num);
		fragment.setArguments(b);
		viewHolder = new ArrayList<ViewHolder>();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		context = getActivity();
//		gridView.setAdapter(new GridViewAdapter(this, **ArrayList HERE ***));
		gridView = (GridView) getActivity().findViewById(R.id.gridView);
	  gridView.setOnItemClickListener(new OnItemClickListener() {
	   public void onItemClick(AdapterView<?> parent, View v,
	     int position, long id) {
//	    Toast.makeText(
//	      getApplicationContext(),
//	      ((TextView) v.findViewById(R.id.grid_item_label))
//	        .getText(), Toast.LENGTH_SHORT).show();

	   }
	  });
		
//		imgBtnNowPlaying = new ImageButton[1];
//		imgBtnNowPlaying[0] = (ImageButton) getActivity().findViewById(R.id.image1);
//		
//		imgBtnUpComing = new ImageButton[upComingSize];
//		imgBtnUpComing[0] = (ImageButton) getActivity().findViewById(R.id.image2);
//		imgBtnUpComing[1] = (ImageButton) getActivity().findViewById(R.id.image3);
//		imgBtnUpComing[2] = (ImageButton) getActivity().findViewById(R.id.image4);
//		imgBtnUpComing[3] = (ImageButton) getActivity().findViewById(R.id.image5);
//		imgBtnUpComing[4] = (ImageButton) getActivity().findViewById(R.id.image6);
//		imgBtnUpComing[5] = (ImageButton) getActivity().findViewById(R.id.image7);
//		imgBtnUpComing[6] = (ImageButton) getActivity().findViewById(R.id.image8);
//		imgBtnUpComing[7] = (ImageButton) getActivity().findViewById(R.id.image9);
//		imgBtnUpComing[8] = (ImageButton) getActivity().findViewById(R.id.image10);
//		imgBtnUpComing[9] = (ImageButton) getActivity().findViewById(R.id.image11);
//		imgBtnUpComing[10] = (ImageButton) getActivity().findViewById(R.id.image12);
//		imgBtnUpComing[11] = (ImageButton) getActivity().findViewById(R.id.image13);
//		imgBtnUpComing[12] = (ImageButton) getActivity().findViewById(R.id.image14);
//		imgBtnUpComing[13] = (ImageButton) getActivity().findViewById(R.id.image15);
//		imgBtnUpComing[14] = (ImageButton) getActivity().findViewById(R.id.image16);
//		imgBtnUpComing[15] = (ImageButton) getActivity().findViewById(R.id.image17);
//		imgBtnUpComing[16] = (ImageButton) getActivity().findViewById(R.id.image18);
//		imgBtnUpComing[17] = (ImageButton) getActivity().findViewById(R.id.image19);
//		imgBtnUpComing[18] = (ImageButton) getActivity().findViewById(R.id.image20);
//		imgBtnUpComing[19] = (ImageButton) getActivity().findViewById(R.id.image21);
//		
//		
//		rlImages = new RelativeLayout[21];
//		rlImages[0] = (RelativeLayout) getActivity().findViewById(R.id.rlImage1);
//		rlImages[1] = (RelativeLayout) getActivity().findViewById(R.id.rlImage2);
//		rlImages[2] = (RelativeLayout) getActivity().findViewById(R.id.rlImage3);
//		rlImages[3] = (RelativeLayout) getActivity().findViewById(R.id.rlImage4);
//		rlImages[4] = (RelativeLayout) getActivity().findViewById(R.id.rlImage5);
//		rlImages[5] = (RelativeLayout) getActivity().findViewById(R.id.rlImage6);
//		rlImages[6] = (RelativeLayout) getActivity().findViewById(R.id.rlImage7);
//		rlImages[7] = (RelativeLayout) getActivity().findViewById(R.id.rlImage8);
//		rlImages[8] = (RelativeLayout) getActivity().findViewById(R.id.rlImage9);
//		rlImages[9] = (RelativeLayout) getActivity().findViewById(R.id.rlImage10);
//		rlImages[10] = (RelativeLayout) getActivity().findViewById(R.id.rlImage11);
//		rlImages[11] = (RelativeLayout) getActivity().findViewById(R.id.rlImage12);
//		rlImages[12] = (RelativeLayout) getActivity().findViewById(R.id.rlImage13);
//		rlImages[13] = (RelativeLayout) getActivity().findViewById(R.id.rlImage14);
//		rlImages[14] = (RelativeLayout) getActivity().findViewById(R.id.rlImage15);
//		rlImages[15] = (RelativeLayout) getActivity().findViewById(R.id.rlImage16);
//		rlImages[16] = (RelativeLayout) getActivity().findViewById(R.id.rlImage17);
//		rlImages[17] = (RelativeLayout) getActivity().findViewById(R.id.rlImage18);
//		rlImages[18] = (RelativeLayout) getActivity().findViewById(R.id.rlImage19);
//		rlImages[19] = (RelativeLayout) getActivity().findViewById(R.id.rlImage20);
//		rlImages[20] = (RelativeLayout) getActivity().findViewById(R.id.rlImage21);
//		
//		txtShowDuration = new TextView[1];
//		txtShowDuration[0] = (TextView) getActivity().findViewById(R.id.ShowTime1);
//		
//		txtNowPlayingTitle = new TextView[1];
//		txtNowPlayingTitle[0] = (TextView) getActivity().findViewById(R.id.ShowTitle1);
//		
//		txtUpComingTitle = new TextView[20];
//		txtUpComingTitle[0] = (TextView) getActivity().findViewById(R.id.ShowTitle2);
//		txtUpComingTitle[1] = (TextView) getActivity().findViewById(R.id.ShowTitle3);
//		txtUpComingTitle[2] = (TextView) getActivity().findViewById(R.id.ShowTitle4);
//		txtUpComingTitle[3] = (TextView) getActivity().findViewById(R.id.ShowTitle5);
//		txtUpComingTitle[4] = (TextView) getActivity().findViewById(R.id.ShowTitle6);
//		txtUpComingTitle[5] = (TextView) getActivity().findViewById(R.id.ShowTitle7);
//		txtUpComingTitle[6] = (TextView) getActivity().findViewById(R.id.ShowTitle8);
//		txtUpComingTitle[7] = (TextView) getActivity().findViewById(R.id.ShowTitle9);
//		txtUpComingTitle[8] = (TextView) getActivity().findViewById(R.id.ShowTitle10);
//		txtUpComingTitle[9] = (TextView) getActivity().findViewById(R.id.ShowTitle11);
//		txtUpComingTitle[10] = (TextView) getActivity().findViewById(R.id.ShowTitle12);
//		txtUpComingTitle[11] = (TextView) getActivity().findViewById(R.id.ShowTitle13);
//		txtUpComingTitle[12] = (TextView) getActivity().findViewById(R.id.ShowTitle14);
//		txtUpComingTitle[13] = (TextView) getActivity().findViewById(R.id.ShowTitle15);
//		txtUpComingTitle[14] = (TextView) getActivity().findViewById(R.id.ShowTitle16);
//		txtUpComingTitle[15] = (TextView) getActivity().findViewById(R.id.ShowTitle17);
//		txtUpComingTitle[16] = (TextView) getActivity().findViewById(R.id.ShowTitle18);
//		txtUpComingTitle[17] = (TextView) getActivity().findViewById(R.id.ShowTitle19);
//		txtUpComingTitle[18] = (TextView) getActivity().findViewById(R.id.ShowTitle20);
//		txtUpComingTitle[19] = (TextView) getActivity().findViewById(R.id.ShowTitle21);
//		
//		progBarNowPlaying = new ProgressBar[1];
//		progBarNowPlaying[0] = (ProgressBar) getActivity().findViewById(R.id.progressBar01);
		
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
	public void setListData() {
		Iterator itSchedule = ScheduleManager.schedules.entrySet().iterator();
		while (itSchedule.hasNext()) {
			Map.Entry mEntry = (Map.Entry) itSchedule.next();
			ChannelSchedule mChannelSchedule = (ChannelSchedule) mEntry
					.getKey();
			CustomListViewValuesArr.add(ScheduleManager.channelData
					.get(mChannelSchedule));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tab_tvguide_layout,
				container, false);
		
		return view;
	}

	public static Activity GetParentActivity() {
		return (Activity) context;
	}

}
