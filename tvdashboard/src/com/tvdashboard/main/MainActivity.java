package com.tvdashboard.main;

import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.mediacenter.MediaHandler;
import com.tvdashboard.model.VideoCategory;
import com.tvdashboard.utility.DashboardUtility;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity{

	private MediaHandler mHandler;
	protected SurfacePanel dashBoardSurfaceView = null;
    protected SurfaceThread dashBoardThread;
    protected View loFav = null;
    protected View loRecent = null;
    ImageButton Tv;
	Button btnStart, btnStop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		Tv = (ImageButton)findViewById(R.id.webView1);
		Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
