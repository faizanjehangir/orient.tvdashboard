package com.tvdashboard.main;

import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tvdashboard.androidwebservice.ChannelSchedule;
import com.tvdashboard.androidwebservice.ScheduleManager;
import com.tvdashboard.androidwebservice.Show;
import com.tvdashboard.database.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTvGuide extends Fragment{
	
	public static ImageButton[] btnImagesNowPlaying;
	public static ImageButton[] btnImagesUpComing;
	public static ImageView[] imgIconsNowPlaying;
	public static ImageView[] imgIconsUpComing;
	public static ProgressBar[] progBar;
	public static TextView[] showTitleNowPlaying;
	public static TextView[] showTitleUpComing;
	public static TextView[] showDuration;
	public static int nowPlayingTileSize = 6;
	public static int comingUpTileSize = 3;
	public static int imgIconsSize = 9;
	public static int[] mProgressStatus;
	public static Context context;
	public static int[] nowPlayingIndices = {0, 1, 2, 3, 4, 5};
	public static int[] upComingShowIndices = {6, 7, 8};
 	
	public static FragmentTvGuide newInstance() {
        FragmentTvGuide fragment = new FragmentTvGuide();

        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		context = getActivity();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
	
		ScheduleManager sm = new ScheduleManager(getActivity(), ImageLoader.getInstance());
		sm.getAllChannelSchedule();
		
		super.onActivityCreated(savedInstanceState);
	}
	
	public static void ResetRunningSchedules(){
		ScheduleManager.isScheduleRunning = false;
		ScheduleManager.imageDownloaderTasks = 0;
		ScheduleManager.showDuration = new HashMap<Show, Long>();
		ScheduleManager.schedules = null;
		ScheduleManager.prgDiag = null;
		ScheduleManager.setBmpImages(null);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context.getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
	
		ScheduleManager sm = new ScheduleManager(context, ImageLoader.getInstance());
		ScheduleManager.isScheduleRunning = false;
		sm.getAllChannelSchedule();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tvguide_layout, container, false);
		btnImagesNowPlaying = new ImageButton[nowPlayingTileSize];
		btnImagesUpComing = new ImageButton[comingUpTileSize];
		btnImagesNowPlaying[0] = (ImageButton) view.findViewById(R.id.image1);
		btnImagesNowPlaying[1] = (ImageButton) view.findViewById(R.id.image2);
		btnImagesNowPlaying[2] = (ImageButton) view.findViewById(R.id.image3);
		btnImagesNowPlaying[3] = (ImageButton) view.findViewById(R.id.image4);
		btnImagesNowPlaying[4] = (ImageButton) view.findViewById(R.id.image5);
		btnImagesNowPlaying[5] = (ImageButton) view.findViewById(R.id.image6);
		btnImagesUpComing[0] = (ImageButton) view.findViewById(R.id.image7);
		btnImagesUpComing[1] = (ImageButton) view.findViewById(R.id.image8);
		btnImagesUpComing[2] = (ImageButton) view.findViewById(R.id.image9);
		
		progBar = new ProgressBar[nowPlayingTileSize];
		progBar[0] = (ProgressBar) view.findViewById(R.id.progressBar01);
		progBar[1] = (ProgressBar) view.findViewById(R.id.progressBar02);
		progBar[2] = (ProgressBar) view.findViewById(R.id.progressBar03);
		progBar[3] = (ProgressBar) view.findViewById(R.id.progressBar04);
		progBar[4] = (ProgressBar) view.findViewById(R.id.progressBar05);
		progBar[5] = (ProgressBar) view.findViewById(R.id.progressBar06);
		
		mProgressStatus = new int[nowPlayingTileSize];
		
		showTitleNowPlaying = new TextView[nowPlayingTileSize];
		showTitleNowPlaying[0] = (TextView) view.findViewById(R.id.ShowTitle01);
		showTitleNowPlaying[1] = (TextView) view.findViewById(R.id.ShowTitle02);
		showTitleNowPlaying[2] = (TextView) view.findViewById(R.id.ShowTitle03);
		showTitleNowPlaying[3] = (TextView) view.findViewById(R.id.ShowTitle04);
		showTitleNowPlaying[4] = (TextView) view.findViewById(R.id.ShowTitle05);
		showTitleNowPlaying[5] = (TextView) view.findViewById(R.id.ShowTitle06);
		
		showTitleUpComing = new TextView[comingUpTileSize];
		showTitleUpComing[0] = (TextView) view.findViewById(R.id.ShowTitle07);
		showTitleUpComing[1] = (TextView) view.findViewById(R.id.ShowTitle08);
		showTitleUpComing[2] = (TextView) view.findViewById(R.id.ShowTitle09);
		
		showDuration = new TextView[nowPlayingTileSize];
		showDuration[0] = (TextView) view.findViewById(R.id.ShowDuration01);
		showDuration[1] = (TextView) view.findViewById(R.id.ShowDuration02);
		showDuration[2] = (TextView) view.findViewById(R.id.ShowDuration03);
		showDuration[3] = (TextView) view.findViewById(R.id.ShowDuration04);
		showDuration[4] = (TextView) view.findViewById(R.id.ShowDuration05);
		showDuration[5] = (TextView) view.findViewById(R.id.ShowDuration06);
		
		imgIconsNowPlaying = new ImageView[nowPlayingTileSize];		
		imgIconsNowPlaying[0] = (ImageView) view.findViewById(R.id.ChannelLogo01);
		imgIconsNowPlaying[1] = (ImageView) view.findViewById(R.id.ChannelLogo02);
		imgIconsNowPlaying[2] = (ImageView) view.findViewById(R.id.ChannelLogo03);
		imgIconsNowPlaying[3] = (ImageView) view.findViewById(R.id.ChannelLogo04);
		imgIconsNowPlaying[4] = (ImageView) view.findViewById(R.id.ChannelLogo05);
		imgIconsNowPlaying[5] = (ImageView) view.findViewById(R.id.ChannelLogo06);
		
		imgIconsUpComing = new ImageView[comingUpTileSize];
		imgIconsUpComing[0] = (ImageView) view.findViewById(R.id.ChannelLogo07);
		imgIconsUpComing[1] = (ImageView) view.findViewById(R.id.ChannelLogo08);
		imgIconsUpComing[2] = (ImageView) view.findViewById(R.id.ChannelLogo09);

		return view;
	}	
	
	public static Activity GetParentActivity(){
		return (Activity) context;
	}
	

}
