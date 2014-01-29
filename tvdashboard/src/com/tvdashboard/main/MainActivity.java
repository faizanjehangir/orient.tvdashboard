package com.tvdashboard.main;

import java.util.List;

import com.tvdashboard.database.R;
import com.tvdashboard.mediacenter.MediaHandler;
import com.tvdashboard.model.VideoCategory;
import com.tvdashboard.utility.DashboardUtility;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private MediaHandler mHandler;
	protected SurfacePanel dashBoardSurfaceView = null;
    protected SurfaceThread dashBoardThread;
    protected View loFav = null;
    protected View loRecent = null;
    ImageButton Tv;
    public boolean hdmi1on = true;
    public boolean hdmi2on = true;
    public boolean usbon = true;
    RelativeLayout TvLayout;
    RelativeLayout FavLayout;
    RelativeLayout VidLayout;
    RelativeLayout PicLayout;
    RelativeLayout MusicLayout;
    public boolean focusFav = true;
    public boolean focusTv = true;
    public boolean focusMusic = true;
    public boolean focusPicture = true;
    public boolean focusVideos = true;
    private Animation animFadeIn;
    private Animation animFadeOut;
    ImageView[] arrImgView;
    ImageView imgView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		arrImgView = new ImageView[20];
		
		arrImgView[0] = (ImageView) findViewById(R.id.favImage1);
		arrImgView[1] = (ImageView) findViewById(R.id.favImage2);
		arrImgView[2] = (ImageView) findViewById(R.id.favImage3);
		arrImgView[3] = (ImageView) findViewById(R.id.favImage4);
		arrImgView[4] = (ImageView) findViewById(R.id.favImage5);
		arrImgView[5] = (ImageView) findViewById(R.id.recentImage1);
		arrImgView[6] = (ImageView) findViewById(R.id.recentImage2);
		arrImgView[7] = (ImageView) findViewById(R.id.recentImage3);
		arrImgView[8] = (ImageView) findViewById(R.id.recentImage4);
		arrImgView[9] = (ImageView) findViewById(R.id.recentImage5);
		arrImgView[10] = (ImageView) findViewById(R.id.musicImage1);
		arrImgView[11] = (ImageView) findViewById(R.id.musicImage2);
		arrImgView[12] = (ImageView) findViewById(R.id.musicImage3);
		arrImgView[13] = (ImageView) findViewById(R.id.musicImage4);
		arrImgView[14] = (ImageView) findViewById(R.id.musicImage5);
		arrImgView[15] = (ImageView) findViewById(R.id.picImage1);
		arrImgView[16] = (ImageView) findViewById(R.id.picImage2);
		arrImgView[17] = (ImageView) findViewById(R.id.picImage3);
		arrImgView[18] = (ImageView) findViewById(R.id.picImage4);
		arrImgView[19] = (ImageView) findViewById(R.id.picImage5);
		
		arrImgView[0].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[0].setBackgroundResource(R.drawable.selectlarge);
				}else
				{
					arrImgView[0].setBackgroundResource(R.drawable.largetile);
				}
			}
		});
		arrImgView[1].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[1].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[1].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[2].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[2].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[2].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[3].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[3].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[3].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[4].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[4].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[4].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[5].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[5].setBackgroundResource(R.drawable.selectlarge);
				}else
				{
					arrImgView[5].setBackgroundResource(R.drawable.largetile);
				}
			}
		});
		arrImgView[6].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[6].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[6].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[7].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[7].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[7].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[8].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[8].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[8].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[9].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[9].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[9].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[10].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[10].setBackgroundResource(R.drawable.selectlarge);
				}else
				{
					arrImgView[10].setBackgroundResource(R.drawable.largetile);
				}
			}
		});
		arrImgView[11].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[11].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[11].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[12].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[12].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[12].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[13].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[13].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[13].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[14].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[14].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[14].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[15].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[15].setBackgroundResource(R.drawable.selectlarge);
				}else
				{
					arrImgView[15].setBackgroundResource(R.drawable.largetile);
				}
			}
		});
		arrImgView[16].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[16].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[16].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		arrImgView[17].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[17].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[17].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[18].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[18].setBackgroundResource(R.drawable.selectsmall);
				}else
				{
					arrImgView[18].setBackgroundResource(R.drawable.smalltile);
				}
			}
		});
		arrImgView[19].setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					arrImgView[19].setBackgroundResource(R.drawable.selectmedium);
				}else
				{
					arrImgView[19].setBackgroundResource(R.drawable.mediumtile);
				}
			}
		});
		
		
		RelativeLayout imageFav = (RelativeLayout)findViewById(R.id.ib1);
		RelativeLayout imageVid = (RelativeLayout)findViewById(R.id.ib2);
		RelativeLayout imageMusic = (RelativeLayout)findViewById(R.id.ib3);
		RelativeLayout imagePic = (RelativeLayout)findViewById(R.id.ib4);
		
		
		RelativeLayout Fav1 = (RelativeLayout)findViewById(R.id.button4);
		RelativeLayout Fav2 = (RelativeLayout)findViewById(R.id.button5);
		RelativeLayout Fav3 = (RelativeLayout)findViewById(R.id.button6);
		RelativeLayout Fav4 = (RelativeLayout)findViewById(R.id.button7);
		
		final Button hdmi1 = (Button)findViewById(R.id.button1);
		final Button hdmi2 = (Button)findViewById(R.id.button2);
		final Button usb = (Button)findViewById(R.id.button3);
		
		
		RelativeLayout Vid1 = (RelativeLayout)findViewById(R.id.button8);
		RelativeLayout Vid2 = (RelativeLayout)findViewById(R.id.button9);
		RelativeLayout Vid3 = (RelativeLayout)findViewById(R.id.button10);
		RelativeLayout Vid4 = (RelativeLayout)findViewById(R.id.button11);
		
		RelativeLayout music1 = (RelativeLayout)findViewById(R.id.button12);
		RelativeLayout music2 = (RelativeLayout)findViewById(R.id.button13);
		RelativeLayout music3 = (RelativeLayout)findViewById(R.id.button14);
		RelativeLayout music4 = (RelativeLayout)findViewById(R.id.button15);
		
		RelativeLayout pic1 = (RelativeLayout)findViewById(R.id.button16);
		RelativeLayout pic2 = (RelativeLayout)findViewById(R.id.button17);
		RelativeLayout pic3 = (RelativeLayout)findViewById(R.id.button18);
		RelativeLayout pic4 = (RelativeLayout)findViewById(R.id.button19);
		
		animFadeIn = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
		
			TvLayout = (RelativeLayout)findViewById(R.id.tvLayout); 
			FavLayout = (RelativeLayout)findViewById(R.id.favourite);
			VidLayout =  (RelativeLayout)findViewById(R.id.recent);
			PicLayout = (RelativeLayout)findViewById(R.id.pictures);
			MusicLayout = (RelativeLayout)findViewById(R.id.movies);
		
			hdmi1.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(hdmi1on==true)
					{
					hdmi1.setBackgroundResource(R.drawable.hdmi1on);
					hdmi1on=false;
					}else
					{
						hdmi1.setBackgroundResource(R.drawable.hdmi1off);
						hdmi1on=true;
					}
					
					
				}
			});
			
			hdmi2.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(hdmi2on==true)
					{
					hdmi2.setBackgroundResource(R.drawable.hdmi2on);

					hdmi2on=false;
					}else
					{
						hdmi2.setBackgroundResource(R.drawable.hdmi2off);
						hdmi2on=true;
				}}
			});
			
			usb.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(usbon==true)
					{
					usb.setBackgroundResource(R.drawable.usbon);
					usbon=false;
					}else
					{
						usb.setBackgroundResource(R.drawable.usboff);
						usbon=true;
					}
					
				}
			});
			
			imageFav.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusFav==true)
				{
				focusFav=false;
				}
				
			}
		});
			
			Fav1.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusFav==true)
				{
				
				focusFav=false;
				}
				
			}
		});
			Fav2.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusFav==true)
				{
				
				focusFav=false;
				}
				
			}
		});
			Fav3.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusFav==true)
				{
				
				focusFav=false;
				}
				
			}
		});
			Fav4.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusFav==true)
				{
				
				focusFav=false;
				}
				
			}
		});

			imageVid.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusVideos==true)
				{
				focusVideos=false;
				}
			}
		});
			
			
			Vid1.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusVideos==true)
				{
				focusVideos=false;
				}
			}
		});
			
			Vid2.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusVideos==true)
				{
				focusVideos=false;
				}
			}
		});
			
			Vid3.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusVideos==true)
				{
				focusVideos=false;
				}
			}
		});
			
			Vid4.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusVideos==true)
				{
				focusVideos=false;
				}
			}
		});
			

			imageMusic.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(focusMusic==true)
				{
				focusMusic=false;
				}
			}
		});
			
			music1.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(focusMusic==true)
				{
				focusMusic=false;
				}
			}
		});
			music2.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(focusMusic==true)
				{
				focusMusic=false;
				}
			}
		});
			music3.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(focusMusic==true)
				{
				focusMusic=false;
				}
			}
		});
			music4.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(focusMusic==true)
				{
				focusMusic=false;
				}
			}
		});
			
			imagePic.setOnFocusChangeListener(new OnFocusChangeListener() {
				
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				
				if(focusPicture==true)
				{
				focusPicture=false;
				}
			}
		});
		
		
			pic1.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(focusPicture==true)
					{
					focusPicture=false;
					}
				}
			});
			
			pic2.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(focusPicture==true)
					{
					focusPicture=false;
					}
				}
			});
			
			pic3.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(focusPicture==true)
					{
					focusPicture=false;
					}
				}
			});
			
			pic4.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					
					if(focusPicture==true)
					{
					focusPicture=false;
					}
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
