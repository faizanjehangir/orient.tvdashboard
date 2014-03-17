package com.tvdashboard.videos;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tvdashboard.database.R;
import com.tvdashboard.helper.DatabaseHelper;
import com.tvdashboard.model.Video;

public class FragmentMusicVideosMain extends Fragment{
	
	public static LinearLayout layoutContent01;
	public static LinearLayout layoutOptions;
	private DatabaseHelper dbHelper;
	public static List<Video> allVideos;
	
	public static FragmentMusicVideosMain newInstance(String num) {
		FragmentMusicVideosMain fragment = new FragmentMusicVideosMain();

		Bundle b = new Bundle();
        b.putString("fragment#", num);
        fragment.setArguments(b);
		
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		String num = getArguments().getString("fragment#");
		if (num.equals("0"))
		{
			layoutContent01.setVisibility(View.GONE);
		}
		else
		{
			layoutOptions.setVisibility(View.GONE);
		}
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
		View view = inflater.inflate(R.layout.fragment_tab_musicvideos_layout, container, false);
		layoutContent01 = (LinearLayout)view.findViewById(R.id.LayoutContent01);
		layoutOptions = (LinearLayout)view.findViewById(R.id.VideosOptions);
		return view;
	}
	
	public class getAllMusicVideos extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try 
			{
				allVideos = dbHelper.getAllVideosByCategory("Music Videos");
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {			
			super.onPostExecute(result);
			
			if (result)
			{
				
			}
			
		}		
	}
	

}
