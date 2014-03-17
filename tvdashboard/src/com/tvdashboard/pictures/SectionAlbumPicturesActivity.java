package com.tvdashboard.pictures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




import com.tvdashboard.bll.Album;
import com.tvdashboard.bll.Picture;
import com.tvdashboard.database.R;
import com.tvdashboard.database.R.id;
import com.tvdashboard.database.R.layout;
import com.tvdashboard.database.R.menu;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SectionAlbumPicturesActivity extends Activity {

	public PictureGallery pictureGallery;
	public RelativeLayout albumRelativeLayout;
	public RelativeLayout thumbnailPreviewRL;
	public RelativeLayout thumbnailInnerLayout;
	public ImageView enlargeImageView;
	public TextView albumName;
	public GridLayout thumbnailGrid;
	public int screenWidth;
	public int screenHeight;
	public int marginSpacesWidth;
	public static GridLayout gl;
	public static RelativeLayout imageWrapper;
	public static RelativeLayout btnPanelLayout;
	public static Button btnPlay;
	public static Button btnPrev;
	public static Button btnNext;
	public static List<Album> albums;
	public int pageno;
	
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section_album_pictures);
		Bundle b = this.getIntent().getExtras();
		int albumIndex = b.getInt("albumIndex");
		List<Album> albums = makealbums();
				
		

		DisplayMetrics metrics = getApplicationContext().getResources()
				.getDisplayMetrics();
		
		//get buttons
		btnPlay = (Button) findViewById(R.id.btnplay);
		btnPrev = (Button) findViewById(R.id.btnprev);
		btnNext = (Button) findViewById(R.id.btnnext);
		
//		EnableGalleryButtons(false);
			
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		marginSpacesWidth = 150;
		screenWidth -= marginSpacesWidth;
		albumRelativeLayout = (RelativeLayout) findViewById(R.id.albumRelativeLayout);
		gl = (GridLayout) findViewById(R.id.albumMainGrid);
		
		imageWrapper = (RelativeLayout) findViewById(R.id.expandImagebackground);
		
		btnPanelLayout = (RelativeLayout) findViewById(R.id.buttonsPanel);

		thumbnailPreviewRL = (RelativeLayout) findViewById(R.id.thumbnailRelMainLayout);
		thumbnailInnerLayout = (RelativeLayout) findViewById(R.id.thumbnailRelInnerLayout);
		
		albumName = (TextView) findViewById(R.id.albumName);
		thumbnailGrid = (GridLayout) findViewById(R.id.thumbnailGrid);
		
		enlargeImageView = (ImageView) findViewById(R.id.expanded_image);
		
		pictureGallery = new PictureGallery(getApplicationContext(), enlargeImageView,
				albumName, thumbnailPreviewRL, thumbnailGrid);
		
		SetGridLayoutDim(gl, pictureGallery.getAlbumLayoutWidth(),
				pictureGallery.getAlbumLayoutHeight());
		
		SetGridLayoutDim(thumbnailGrid, pictureGallery.getThumbnailWidth(),
				pictureGallery.getThumbnailHeight());
		
		pictureGallery.BuildAlbums(makealbums(), albumIndex);
		
		gl.getChildAt(0).requestFocus();
		/*pictureGallery.BuildAlbums(makealbums());*/
		thumbnailGrid =  pictureGallery.ConstructAlbumImagesForPictures(albums.get(albumIndex));
		thumbnailPreviewRL.getChildAt(0).requestFocus();
		
		
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setPageMargin(-150);
		/*pageAdapter = new PicturesPageAdapter(getSupportFragmentManager(), fragments);*/
		
		/*thumbGrid.getChildAt(0).requestFocus();*/
		/*FragmentAlbumsMain.gl.setVisibility(View.GONE);*/
		
	}
	
	public void SetGridLayoutDim(GridLayout gridLayout, int width, int height) {
		int colCount = screenWidth / width; // subtraction from colspan
													// from right/left
		gridLayout.setColumnCount(colCount);
	}
	
	private List<Album> makealbums(){
		List<Album> albums = new ArrayList<Album>();
		
		String result="";
		String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATA};
		Cursor cur = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, "");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.section_album_pictures, menu);
		return true;
	}

}
