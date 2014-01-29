package com.orient.menu.animations;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class CollapseAnimationRTL extends Animation implements Animation.AnimationListener {

	private View view;
	private static int ANIMATION_DURATION;
	private int LastWidth;
	private int FromWidth;
	private int ToWidth;
	private static int duration;
	private static int STEP_SIZE=30;
	private int screenWidth;
	public CollapseAnimationRTL(View v, int FromWidth, int ToWidth, int Duration, int screenWidth) {
		
		this.view = v;
		LayoutParams lyp =  view.getLayoutParams();
		ANIMATION_DURATION = 1;
		this.FromWidth = lyp.width;
		this.ToWidth = lyp.width;
		this.screenWidth = screenWidth;
		this.duration = Duration;
		setDuration(ANIMATION_DURATION);
		setRepeatCount(duration);
		setFillAfter(false);
		setInterpolator(new AccelerateInterpolator());
		setAnimationListener(this);
	}

	public void onAnimationEnd(Animation anim) {
		
	}

	public void onAnimationRepeat(Animation anim) {
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = lyp.width - ToWidth/duration;
		view.setLayoutParams(lyp);
	}

	public void onAnimationStart(Animation anim) {
		LayoutParams lyp =  view.getLayoutParams();
		LastWidth = lyp.width;
	}

}
