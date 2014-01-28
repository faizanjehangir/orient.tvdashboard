package com.orient.menu.animations;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Toast;

public class ExpandAnimationRTL extends Animation implements Animation.AnimationListener {
	private View view;
	private static int ANIMATION_DURATION;
	private int LastWidth;
	private int FromWidth;
	private int ToWidth;
	private static int STEP_SIZE=30;
	private static int duration;
	private int screenWidth;
	public ExpandAnimationRTL(View v, int FromWidth, int ToWidth, int Duration, int ScreenWidth) {
		
		this.view = v;
		ANIMATION_DURATION = 6;
		this.FromWidth = FromWidth;
		this.ToWidth = ToWidth;
		this.duration = Duration;
		this.screenWidth = screenWidth;
		setDuration(ANIMATION_DURATION);
		setRepeatCount(Duration);
		setFillAfter(false);
		setInterpolator(new AccelerateInterpolator());
		setAnimationListener(this);
	}

	public void onAnimationEnd(Animation anim) {
		
	}

	public void onAnimationRepeat(Animation anim) {
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = LastWidth +=ToWidth/duration;
		view.setLayoutParams(lyp);
	}

	public void onAnimationStart(Animation anim) {
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = screenWidth;
		view.setLayoutParams(lyp);
		LastWidth = screenWidth;;
	}

}
