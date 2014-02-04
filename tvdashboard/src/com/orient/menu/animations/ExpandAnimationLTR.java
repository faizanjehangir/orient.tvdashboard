package com.orient.menu.animations;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public class ExpandAnimationLTR extends Animation implements Animation.AnimationListener {
	private View view;
	private static int ANIMATION_DURATION;
	private int LastWidth;
	private int FromWidth;
	private int ToWidth;
	private static int STEP_SIZE=30;
	private static int duration;
	public ExpandAnimationLTR(View v, int FromWidth, int ToWidth, int Duration) {
		
		this.view = v;
		ANIMATION_DURATION = 6;
		this.FromWidth = FromWidth;
		this.ToWidth = ToWidth;
		this.duration = Duration;
		setDuration(ANIMATION_DURATION);
		setRepeatCount(Duration);
		setFillAfter(false);
		setInterpolator(new AccelerateInterpolator());
		setAnimationListener(this);
	}

	public void onAnimationEnd(Animation anim) {
		// TODO Auto-generated method stub
		
	}

	public void onAnimationRepeat(Animation anim) {
		// TODO Auto-generated method stub
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = LastWidth +=ToWidth/duration;
		view.setLayoutParams(lyp);
	}

	public void onAnimationStart(Animation anim) {
		// TODO Auto-generated method stub
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = 0;
		view.setLayoutParams(lyp);
		LastWidth = 0;
	}

}
