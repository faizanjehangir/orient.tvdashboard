package com.tvdashboard.pictures;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvdashboard.pictures.PictureGallery;
import com.tvdashboard.bll.Album;
import com.tvdashboard.database.R;

public class FragmentAlbumsMain extends Fragment{
	
	public static LinearLayout layoutContent01;
	public static LinearLayout layoutOptions;
	
	
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
	public static FragmentAlbumsMain newInstance(int index, List<Album> albumslist) {
		FragmentAlbumsMain fragment = new FragmentAlbumsMain();
		/*albums = new ArrayList<Album>();*/
		albums = new ArrayList<Album>();
		albums = albumslist;
		Bundle b = new Bundle();
        b.putInt("fragment#", index);
        fragment.setArguments(b);
		
        return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		/*pageno = getArguments().getInt("fragment#");*/
		/*if (num.equals("0"))
		{
			layoutContent01.setVisibility(View.GONE);
		}
		else
		{
			layoutOptions.setVisibility(View.GONE);
		}*/
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
		pageno = getArguments().getInt("fragment#");
		View view = inflater.inflate(R.layout.fragment_albums_main_layout, container, false);
		layoutContent01 = (LinearLayout)view.findViewById(R.id.LayoutContent01);
		
		
		DisplayMetrics metrics = getActivity().getApplicationContext().getResources()
				.getDisplayMetrics();
		
		//get buttons
		btnPlay = (Button) getActivity().findViewById(R.id.btnplay);
		btnPrev = (Button) getActivity().findViewById(R.id.btnprev);
		btnNext = (Button) getActivity().findViewById(R.id.btnnext);
		
//		EnableGalleryButtons(false);
			
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		marginSpacesWidth = 150;
		screenWidth -= marginSpacesWidth;
		albumRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.albumRelativeLayout);
		gl = (GridLayout) view.findViewById(R.id.albumMainGrid);
		
		imageWrapper = (RelativeLayout) view.findViewById(R.id.expandImagebackground);
		
		btnPanelLayout = (RelativeLayout) view.findViewById(R.id.buttonsPanel);

		thumbnailPreviewRL = (RelativeLayout) view.findViewById(R.id.thumbnailRelMainLayout);
		thumbnailInnerLayout = (RelativeLayout) view.findViewById(R.id.thumbnailRelInnerLayout);
		
		albumName = (TextView) view.findViewById(R.id.albumName);
		thumbnailGrid = (GridLayout) view.findViewById(R.id.thumbnailGrid);
		
		enlargeImageView = (ImageView) view.findViewById(R.id.expanded_image);
		
		pictureGallery = new PictureGallery(view.getContext(), enlargeImageView,
				albumName, thumbnailPreviewRL, thumbnailGrid);
		
		SetGridLayoutDim(gl, pictureGallery.getAlbumLayoutWidth(),
				pictureGallery.getAlbumLayoutHeight());
		
		SetGridLayoutDim(thumbnailGrid, pictureGallery.getThumbnailWidth(),
				pictureGallery.getThumbnailHeight());
		
		
		if(((pageno+1)*10)<=albums.size()){
			pictureGallery.BuildAlbums(albums.subList(pageno*10, (pageno+1)*10), pageno);
		}
		else
			pictureGallery.BuildAlbums(albums.subList(pageno*10, (pageno*10)+albums.size()%10), pageno);
		gl.getChildAt(0).requestFocus();
		/*layoutOptions = (LinearLayout)view.findViewById(R.id.PhotosOptions);*/
		return view;
	}	
	
	

	public void SetGridLayoutDim(GridLayout gridLayout, int width, int height) {
		int colCount = screenWidth / width; // subtraction from colspan
													// from right/left
		gridLayout.setColumnCount(colCount);
	}
	
	public static void hideOrShowPanelButtons(int visibility){
		btnPlay.setVisibility(visibility);
		btnPrev.setVisibility(visibility);
		btnNext.setVisibility(visibility);
		btnPanelLayout.setVisibility(visibility);
	}

	private int dpToPx(int dp) {
		float density = getActivity().getResources()
				.getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}
	
	public int pxToDp(int px) {
	    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}
	
	

}
