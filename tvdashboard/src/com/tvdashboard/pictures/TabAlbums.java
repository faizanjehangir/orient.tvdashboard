package com.tvdashboard.pictures;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.tvdashboard.bll.Album;
import com.tvdashboard.bll.Picture;
import com.tvdashboard.database.R;
import com.tvdashboard.main.FixedSpeedScroller;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabAlbums extends FragmentActivity implements OnPageChangeListener {

    PicturesPageAdapter pageAdapter;
    private ViewPager mViewPager;
    PageIndicator mIndicator;
    int fragmentCounter=0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pictures_layout);
        Bundle b = this.getIntent().getExtras();
        
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setPageMargin(-150);
        List<Fragment> fragments = getFragments();
        pageAdapter = new PicturesPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(TabAlbums.this);
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        
        try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Interpolator sInterpolator = null;
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mViewPager.getContext(), sInterpolator);
			mScroller.set(mViewPager, scroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
        
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer(){
			@Override
			public void transformPage(View page, float position) {				
				
				final float normalizedposition = Math.abs(Math.abs(position) - 1);
			    page.setScaleX(normalizedposition / 2 + 0.5f);
			    page.setScaleY(normalizedposition / 2 + 0.5f);
			    page.setAlpha(normalizedposition);				
//				page.setRotationY(position * -30);				
			}        	
        });
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.mViewPager.getCurrentItem();
    }

    @Override
        public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments(){
    	List<Album> albums = makealbums();
    	List<Fragment> fList = new ArrayList<Fragment>();
    	fragmentCounter = (albums.size()/15)+1;
    	for(int i=0;i<fragmentCounter;i++){
    		
    			FragmentAlbumsMain fragAlbum = FragmentAlbumsMain.newInstance(i, albums);
        		fList.add(fragAlbum);
    		
    			
    	}
        /*

        FragmentAlbumsMain f1 = FragmentAlbumsMain.newInstance(String.valueOf(fragmentCounter));
        
        fragmentCounter++;
        FragmentAlbumsMain f2 = FragmentAlbumsMain.newInstance(String.valueOf(fragmentCounter));
        fragmentCounter++;
        fList.add(f1);
        fList.add(f2);*/

        return fList;
    }
    
    private List<Album> makealbums(){
		List<Album> albums = new ArrayList<Album>();
		
		String result="";
		String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA};
		Cursor cur = this.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, "");
		String prevAlbum = "";
		Album album = new Album();
		Picture pic = new Picture();
		
		if (cur.moveToFirst()) {
			
			String bucket;
	        
	        int bucketColumn = cur.getColumnIndex(
	            MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
	        int bucketData = cur.getColumnIndex(
		            MediaStore.Images.Media.DATA);
	        do {
	            // Get the field values
	        	pic = new Picture();
	            bucket = cur.getString(bucketColumn);
	            if(!bucket.equals(prevAlbum)){
	            	prevAlbum = bucket;
	            	albums.add(album);
	            	album = new Album();
	            	album.albumName = bucket;
	            	album.pics = new ArrayList<Picture>();
            	}
	            pic.imgName = cur.getString(bucketData);
	            album.pics.add(pic);
	            // Do something with the values.
	            result += bucket+",";
	        } while (cur.moveToNext());

	    }
		
		return albums;
	}

}