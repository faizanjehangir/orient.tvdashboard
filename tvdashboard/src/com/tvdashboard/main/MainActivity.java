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

public class MainActivity extends Activity implements OnClickListener {

	private MediaHandler mHandler;
	protected SurfacePanel dashBoardSurfaceView = null;
    protected SurfaceThread dashBoardThread;
    protected View loFav = null;
    protected View loRecent = null;
    
	private List<VideoCategory> lstVc;
//	Button multimedia;
	Button btnStart, btnStop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dashBoardSurfaceView = (SurfacePanel)findViewById(R.id.dashboardSurface);
		dashBoardThread = dashBoardSurfaceView.getThread();

        btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        btnStop = (Button)findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
        
        this.loFav = (View) findViewById(R.id.llFavorites);
        this.loRecent = (View) findViewById(R.id.llRecent);
        
//        this.loFav.setfo
        
//		DashboardUtility.getInstance().setActivity(this);
		//InitDashboard();
//		setContentView(R.layout.activity_main);
//		mHandler = new MediaHandler();
//		lstVc = mHandler.InitVideoCategories(getApplicationContext());
//		multimedia = (Button)findViewById(R.id.btnMultimedia);
//		parentLayout = (View)findViewById(R.id.llParentLayout);
//		InitDashboardMultimedia();

	}
	
	protected void InitDashboard(){
		this.dashBoardSurfaceView = new SurfacePanel(this);
	    this.dashBoardSurfaceView.setVisibility(0);
	    this.dashBoardSurfaceView.setZOrderOnTop(true);
	    this.dashBoardSurfaceView.getHolder().setFormat(-2);
	    this.dashBoardSurfaceView.requestFocus();
	    
	    //add a layout for the dialler
	    
//	    if (this.mActiveOKitUI == null) {
//	      this.mActiveOKitUI = MstarOKitUI.getInstance();
//	    }
//	    this.mActiveOKitUI.init(this.glView.getGameEngine());
//	    this.gameEngine = this.glView.gameEngine;
	}
	
	public void onClick(View v)
    {

        switch (v.getId()) {
	        case R.id.btnStart:
	            if(dashBoardThread.mState == dashBoardThread.STATE_PLAY){
	            	dashBoardThread.setDashboardState(SurfaceThread.STATE_RUNNING);
	            	Log.v("btnevent", "start");
	            }
	
	            break;
	
	        case R.id.btnStop:
	            if(dashBoardThread.mState == dashBoardThread.STATE_RUNNING){
	            	dashBoardThread.setDashboardState(SurfaceThread.STATE_PLAY);
	            	Log.v("btnevent", "stop");
	            }
	            break;
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
//	public void InitDashboardMultimedia(){
//		
//		multimedia.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				switch (parentLayout.getVisibility()){
//					case View.INVISIBLE:
//						parentLayout.setVisibility(View.VISIBLE);
//						break;
//					case View.VISIBLE:
//						parentLayout.setVisibility(View.INVISIBLE);
//						break;
//					default:
//						break;
//					
//				}			
//			}
//		});
//	}

}
