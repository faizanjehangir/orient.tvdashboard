package com.tvdashboard.main;

import com.tvdashboard.database.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

public class SurfaceThread extends Thread {
	
	private SurfaceHolder _surfaceHolder;
    private boolean _run = false;
    private boolean touched = false;
    Bitmap bitmapDroid;
 
    /**
     * State-tracking constants.
     */
    public static final int STATE_PLAY = 0;
    public static final int STATE_RUNNING = 1;
    public int mState=STATE_PLAY;
    
    Resources mRes;
 
    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;
 
    float droidx=200,droidy=200;
    
	public SurfaceThread (SurfaceHolder surfaceHolder, Context context)
	{
		_surfaceHolder = surfaceHolder;
        mContext = context;
        mRes = context.getResources();
       // create droid and load bitmap
        bitmapDroid=BitmapFactory.decodeResource(mRes, R.drawable.ic_launcher);
	}

	void setRunning(boolean bRun)
	{
		_run = bRun;
	}
	
	@Override
	public void run()
	{
		Canvas c;
        while (_run) {
            c = null;
            try {
                c = _surfaceHolder.lockCanvas(null);
                synchronized (_surfaceHolder) {
                    onDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
	}
	
	public void onDraw(Canvas canvas) {	 
        canvas.drawColor(Color.rgb(187, 255, 255));
        canvas.drawBitmap(bitmapDroid, droidx - (bitmapDroid.getWidth() / 2), droidy - (bitmapDroid.getHeight() / 2), null);
    }
	
	public void setDashboardState(int mode) {
        synchronized (_surfaceHolder) {
            if (mode == STATE_RUNNING) {
                mState=STATE_RUNNING;
              } 
            else if(mode == STATE_PLAY)
            {
                 mState=STATE_PLAY;
            }
        }
    }
	
	public void handleActionDown(int eventX, int eventY)
    {
		if(mState==STATE_RUNNING){
			Log.i("openGl","StateRunning");
            if (eventX >= (droidx - bitmapDroid.getWidth() / 2) && (eventX <= (droidx + bitmapDroid.getWidth()/2)))
            {
                if (eventY >= (droidy - bitmapDroid.getHeight() / 2) && (eventY <= (droidy + bitmapDroid.getHeight() / 2)))
                {
            		setTouched(true);
                }
			    else
			    {
			        setTouched(false);
			    }
            }
            else
            {
                setTouched(false);
            }
        }
        else{
            setTouched(false);
        }
     }
 
     public void setTouched(boolean touched) {
          this.touched = touched;
         }
 
     public boolean isTouched() {
          return touched;
         }

}
