package com.tvdashboard.pictures;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar.LayoutParams;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.tvdashboard.bll.Album;
import com.tvdashboard.database.R;
import com.tvdashboard.database.R.id;
import com.tvdashboard.database.R.layout;

public class AlbumPicturesFragment extends Fragment implements AnimationListener{
	
	public static LinearLayout layoutContent01;
	public static LinearLayout layoutOptions;
	public GridLayout albummainGrid;
	
	public PictureGallery pictureGallery;
	public RelativeLayout albumRelativeLayout;
	public RelativeLayout thumbnailPreviewRL;
	public RelativeLayout thumbnailInnerLayout;
	public ImageView enlargeImageView;
	public TextView albumName;
	public GridLayout thumbnailGrid;
	public static int screenWidth;
	public static int screenHeight;
	public int marginSpacesWidth;
	public static GridLayout gl;
	public static RelativeLayout imageWrapper;
	public static RelativeLayout btnPanelLayout;
	public static Button btnPlay;
	public static Button btnPrev;
	public static Button btnNext;
	public static Album album = new Album();
	public int pageno;
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;
	ImageView enlargeImage;
	GridLayout glPhotos;
	String enlargedImageString;
	AlphaAnimation anim;
	RelativeLayout thumbnailRL;
	int enlargedImageId;
	public boolean isPlayPressed = false;
	public static boolean inSlideShowMode = false;
	int currentButtonState;
	Animation animFadeout;
	
	public static AlbumPicturesFragment newInstance(int index, Album albumselected, int screenwidth, int screenheight) {
		AlbumPicturesFragment fragment = new AlbumPicturesFragment();
		screenWidth = screenwidth;
		screenHeight = screenheight;
		album = albumselected;
		Bundle b = new Bundle();
        b.putInt("fragment#", index);
        fragment.setArguments(b);
		return fragment;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Toast.makeText(getActivity(), "OnCreate", Toast.LENGTH_LONG).show();
		
		/*ArrayAdapter<String> aa = new ArrayAdapter<String>(this.getActivity(),  )*/
		/*gvPhotos.addView(tv);*/
	}
	
	@Override
	public void onDestroy(){
		/*View view = inflater.inflate(R.layout.activity_album_pictures_fragment, container, false);*/
		
		int count = glPhotos.getChildCount();
		for(int i=0;i<count;i++){
			LinearLayout thumbnaillayout = (LinearLayout) glPhotos.getChildAt(i);
			ImageView mImage = (ImageView) thumbnaillayout.getChildAt(0);
			Drawable toRecycle = mImage.getDrawable();
	        if ( toRecycle != null && toRecycle instanceof BitmapDrawable ) {
	            if ( ( (BitmapDrawable) mImage.getDrawable() ).getBitmap() != null )
	                ( (BitmapDrawable) mImage.getDrawable() ).getBitmap().recycle();
	        }
		}
		super.onDestroy();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*return super.onCreateView(inflater, container, savedInstanceState);*/
		anim = new AlphaAnimation(1, 0.2f);
		anim.setDuration(5000);
		pageno = getArguments().getInt("fragment#");
		View view = inflater.inflate(R.layout.activity_album_pictures_fragment, container, false);
		glPhotos = (GridLayout) view.findViewById(R.id.glPhotos);
		thumbnailRL = (RelativeLayout) view.findViewById(R.id.thumbnailRelMainLayout);
		TextView tv = new TextView(this.getActivity());
		enlargeImage = (ImageView) view.findViewById(R.id.expanded_image);
		ImageView iv = new ImageView(this.getActivity());
		imageWrapper = (RelativeLayout) view.findViewById(R.id.expandImagebackground);
		
		btnPlay = (Button) view.findViewById(R.id.btnplay);
		btnPrev = (Button) view.findViewById(R.id.btnprev);
		btnNext = (Button) view.findViewById(R.id.btnnext);
		
		btnPanelLayout = (RelativeLayout) view.findViewById(R.id.buttonsPanel);
		inSlideShowMode = false;
		GridLayout.LayoutParams glParams = new GridLayout.LayoutParams();
		glParams.width =Integer.parseInt(this.getString(R.string.thumbWidth));
		glParams.height = Integer.parseInt(this.getString(R.string.thumbHeight));
		int totalThumbs = 0;
		SetGridLayoutDim(glPhotos, Integer.parseInt(this.getString(R.string.thumbWidth)), Integer.parseInt(this.getString(R.string.thumbHeight)));
		int cols = screenWidth/ Integer.parseInt(this.getString(R.string.thumbWidth));
		int rows = screenHeight/Integer.parseInt(this.getString(R.string.thumbHeight));
		cols--;
		rows--;
		totalThumbs = cols * rows;
		mShortAnimationDuration = getActivity().getResources().getInteger(android.R.integer.config_shortAnimTime);
		
		for(int i= pageno*totalThumbs;i<(pageno+1)*totalThumbs && i<album.pics.size();i++){
			final int key = i;
			LinearLayout thumbNailLayout = new LinearLayout(this.getActivity());
			thumbNailLayout.setLayoutParams(new GridLayout.LayoutParams(
					glParams));
			thumbNailLayout.setPadding(7, 7, 0, 0);
			iv = new ImageView(this.getActivity());
			iv.setAdjustViewBounds(true);
			iv.setScaleType(ScaleType.CENTER_CROP);
			Bitmap bmp = BitmapLoader.loadBitmap(album.pics.get(i).imgName, Integer.parseInt(this.getString(R.string.thumbquality)), Integer.parseInt(this.getString(R.string.thumbquality))) ;
			iv.setImageBitmap(bmp);
			iv.setLayoutParams(new GridLayout.LayoutParams(glParams));
			thumbNailLayout.setBackgroundResource(R.drawable.photo);
			thumbNailLayout
			.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View imageLayout,
						boolean hasFocus) {

					SetBackgroundThumbnail(album.pics.get(key).imgName);
					LinearLayout layout = (LinearLayout) imageLayout;
					if (hasFocus)
						layout.setBackgroundResource(R.drawable.thumb);
					else
						layout.setBackgroundResource(R.drawable.photo);
				}
			});
			thumbNailLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View imageLayout) {
					inSlideShowMode = true;
					glPhotos.setVisibility(View.GONE);
					enlargedImageString = album.pics.get(key).imgName;
					enlargedImageId = key;
					zoomImageFromThumb(imageLayout, album);
					imageWrapper.setVisibility(View.VISIBLE);
					btnPanelLayout.startAnimation(anim);
					anim.setFillAfter(true);
					InitGalleryButtons(album);
				}
			});
			
			thumbNailLayout.addView(iv);
			glPhotos.addView(thumbNailLayout);
		}
		
		
		
		pageno = getArguments().getInt("fragment#");
		tv.append(Integer.toString(pageno));
		
		return view;
	}	
	
	private void zoomImageFromThumb(final View thumbView, Album album) {
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}
		
		final ImageView expandedImageView = enlargeImage;
		Bitmap bmp = BitmapLoader.loadBitmap(enlargedImageString, 300, 300) ;
		enlargeImage.setImageBitmap(bmp);
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the
		// container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		thumbnailRL.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
		
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
						.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
								startBounds.top, finalBounds.top))
								.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
										startScale, 1f))
										.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
												startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		/*final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(expandedImageView, View.X,
								startBounds.left))
								.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
										startBounds.top))
										.with(ObjectAnimator.ofFloat(expandedImageView,
												View.SCALE_X, startScaleFinal))
												.with(ObjectAnimator.ofFloat(expandedImageView,
														View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});*/
	}
	
	public void ChangeToNextImage(Album album) {
		if((enlargedImageId+1)<album.pics.size()){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(album.pics.get(enlargedImageId+1).imgName, options);
			Bitmap bmp = BitmapLoader.loadBitmap(album.pics.get(enlargedImageId+1).imgName, options.outWidth, options.outHeight) ;
			enlargedImageId++;
			enlargeImage.setImageBitmap(bmp);
		}
	}
	
	public void ChangeToPreviousImage(Album album) {
		if((enlargedImageId-1)>=0){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(album.pics.get(enlargedImageId-1).imgName, options);
			Bitmap bmp = BitmapLoader.loadBitmap(album.pics.get(enlargedImageId-1).imgName, options.outWidth, options.outHeight) ;
			enlargedImageId--;
			enlargeImage.setImageBitmap(bmp);
		}
	}
	
	public void InitGalleryButtons(final Album album) {

		// get the 3 buttons
		 btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Next CLICKED!", Toast.LENGTH_LONG).show();
				ChangeToNextImage(album);
			}
		});

		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (!isPlayPressed) {
					isPlayPressed = true;
					btnPlay.setBackgroundResource(R.drawable.sspause);
					StartSlideShow(album);
				} else {
					isPlayPressed = false;
					btnPlay.setBackgroundResource(R.drawable.ssplay);
				}
			}
		});

		btnPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get next sibling of the current layout
				/*HashMap<Integer, String> selectedAlbum = albumInfo
						.get(album.albumName);*/
				ChangeToPreviousImage(album);

			}
		});
		btnPanelLayout.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && currentButtonState == 1) {
					btnPanelLayout.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		/*btnPanelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChangeToNextImage(album);
			}
		});*/
	}
	
	public void StartSlideShow(Album album){

		AlphaAnimation animFadeOut = new AlphaAnimation(1, 0.0f);
		animFadeOut.setDuration(2000);
		FragmentAlbumsMain.btnPanelLayout.startAnimation(animFadeOut);
		animFadeOut.setFillAfter(true);
		animate(enlargeImage, album, true);
		FragmentAlbumsMain.btnPanelLayout.startAnimation(anim);
		anim.setFillAfter(true);
	}
	
	private void animate(final ImageView imageView, final Album album, final boolean forever) {

		//imageView <-- The View which displays the images
		//images[] <-- Holds R references to the images to display
		//imageIndex <-- index of the first image to show in images[] 
		//forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

		int fadeInDuration = 500; // Configure time values here
		int timeBetween = 3000;
		int fadeOutDuration = 1000;

		imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(album.pics.get(enlargedImageId).imgName, options);
		Bitmap bmp = BitmapLoader.loadBitmap(album.pics.get(enlargedImageId).imgName, options.outWidth, options.outHeight) ;
		imageView.setImageBitmap(bmp);


		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		fadeIn.setDuration(fadeInDuration);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setStartOffset(fadeInDuration + timeBetween);
		fadeOut.setDuration(fadeOutDuration);

		AnimationSet animation = new AnimationSet(false); // change to false
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);
		animation.setRepeatCount(1);
		imageView.setAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				if (album.pics.size() - 1 > enlargedImageId) {
					enlargedImageId++;
					animate(imageView, album, forever); //Calls itself until it gets to the end of the array
				}
				else {
					if (forever == true){
						enlargedImageId = 0;
						animate(imageView, album, forever);  //Calls itself to start the animation all over again in a loop if forever = true
					}
					Bitmap bmp = BitmapLoader.loadBitmap(album.pics.get(0).imgName, 1000, 1000) ;
					enlargeImage.setImageBitmap(bmp);
				}
			}
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void SetBackgroundThumbnail(String imagePath) {
		Log.v("on focus", "called");
		Bitmap bmp = BitmapLoader.loadBitmap(imagePath, Integer.parseInt(this.getString(R.string.ImageQuality)), Integer.parseInt(this.getString(R.string.ImageQuality))) ;
		@SuppressWarnings("deprecation")
		BitmapDrawable background = new BitmapDrawable(bmp);
		thumbnailRL.setBackgroundDrawable(background);
	}
		
	public void SetGridLayoutDim(GridLayout gridLayout, int width, int height) {
		int colCount = screenWidth / width; // subtraction from colspan
													// from right/left
		gridLayout.setColumnCount(colCount-1);
		
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
	
	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub

		if (arg0 == animFadeout) {
			// hide buttons
			currentButtonState = 1;
		}
	}


	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

}
