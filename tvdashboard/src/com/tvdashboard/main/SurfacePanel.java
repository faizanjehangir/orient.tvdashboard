package com.tvdashboard.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfacePanel extends SurfaceView implements SurfaceHolder.Callback {
	Context context;
	SurfaceThread sThread;
	/** parameterized constructor for surface panel class**/
	
	public SurfacePanel(Context ctx){
		super(ctx);
		context = ctx;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);	
	}
	
	public SurfacePanel(Context ctx, AttributeSet attrSet)
	{
		super(ctx, attrSet);
        getHolder().addCallback(this);
        sThread = new SurfaceThread(getHolder(), ctx);
        setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Fetches the animation thread
     *
     * @return the animation thread
     */
    public SurfaceThread getThread() {
        return sThread;
    }
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		sThread.setRunning(true);
		sThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		 // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        sThread.setRunning(false);
        while (retry) {
            try {
            	sThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	 
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	 
	    	sThread.handleActionDown((int)event.getX(), (int)event.getY());
	    }
	    if (event.getAction() == MotionEvent.ACTION_MOVE) {
	           // the gestures
           if (sThread.isTouched()) {
            // the droid was picked up and is being dragged
        	   sThread.droidx = (int) event.getX();
        	   sThread.droidy = (int) event.getY();
 
           }	 
	    }
	    if (event.getAction() == MotionEvent.ACTION_UP) {
	           // touch was released
           if (sThread.isTouched()) {
        	   sThread.setTouched(false);
            }
        }
        return true;
	}
}
