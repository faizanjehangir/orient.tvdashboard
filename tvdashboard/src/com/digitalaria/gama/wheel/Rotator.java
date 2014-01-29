/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This source code is also licensed under the terms of the 
 * Digital Aria GAMA Open Source Software License (the "GAMA license");
 * you may use this component after to agree the GAMA license.
 * 
 * GAMA - Open Source Mobile UI Component Library
 * 
 *  Copyright (c) 2012 Digital Aria Inc.
 *  licensing@digitalaria.com
 *  For more details, please visit: http://developer.digitalaria.com/gama/license
 */

package com.digitalaria.gama.wheel;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

class Rotator {
	private int mMode;

	private int _startAngle;
	private float _finalAngle;
	private int _minAngle;
	private int _maxAngle;
	private float _currAngle;
	private float _deltaAngle;
	private float _coeffAngle = 0.0f;
	private float _coeffVelocity = 0.7f;

	private long mStartTime;
	private int mDuration;
	private float mDurationReciprocal;
	private boolean mFinished;

	private float mVelocity;

	private static final int DEFAULT_DURATION = 250;
	private static final int SCROLL_MODE = 0;
	private static final int FLING_MODE = 1;

	private final float mDeceleration;

	private static float sViscousFluidScale;
	private static float sViscousFluidNormalize;

	static {
		// This controls the viscous fluid effect (how much of it)
		sViscousFluidScale = 8.0f;
		// must be set to 1.0 (used in viscousFluid())
		sViscousFluidNormalize = 1.0f;
		sViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
	}

	public Rotator(Context context) {
		mFinished = true;
		float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
		mDeceleration = SensorManager.GRAVITY_EARTH // g (m/s^2)
				* 39.37f // inch/meter
				* ppi // pixels per inch
				* ViewConfiguration.getScrollFriction();
	}

	public final boolean isFinished() {
		return mFinished;
	}

	public final void forceFinished(boolean finished) {
		mFinished = finished;
	}

	public final int getDuration() {
		return mDuration;
	}

	public final float getCurrentAngle() {
		return _currAngle;
	}

	public final float getStartAngle() {
		return _startAngle;
	}

	public final float getFinalAngle() {
		return _finalAngle;
	}

	public float getCurrentVelocity() {
		return mVelocity - mDeceleration * timePassed() / 2000.0f;
	}

	public void scroll(int startAngle, float dAngle) {
		scroll(startAngle, dAngle, DEFAULT_DURATION);
	}

	public void scroll(int startAngle, float deltaAngle, int duration) {
		mMode = SCROLL_MODE;
		mFinished = false;
		mDuration = duration;
		mStartTime = AnimationUtils.currentAnimationTimeMillis();
		_startAngle = startAngle;
		_finalAngle = startAngle + deltaAngle;
		_deltaAngle = deltaAngle;
		mDurationReciprocal = 1.0f / mDuration;
	}

	public void fling(int startAngle, int velocityX, int velocityY) {
		mMode = FLING_MODE;
		mFinished = false;
		float velocity = (float) Math.sqrt(velocityX * velocityX + velocityY
				* velocityY)
				* _coeffVelocity;
		mVelocity = velocity;
		mDuration = (int) (1000 * velocity / mDeceleration);
		mStartTime = AnimationUtils.currentAnimationTimeMillis();
		_startAngle = startAngle;

		_coeffAngle = 0.5f;
		_minAngle = 0;
		_maxAngle = Integer.MAX_VALUE;

		int totalDistance = (int) ((velocity * velocity) / (2 * mDeceleration));

		_finalAngle = startAngle + Math.round(totalDistance * _coeffAngle);
		_finalAngle = Math.min(_finalAngle, _maxAngle);
		_finalAngle = Math.max(_finalAngle, _minAngle);
	}

	public boolean computeAngleOffset() {
		if (mFinished) {
			return false;
		}

		int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);
		if (timePassed < mDuration) {
			switch (mMode) {
			case SCROLL_MODE:

				float a = (float) timePassed * mDurationReciprocal;
				a = viscousFluid(a);
				_currAngle = _startAngle + (a * _deltaAngle);
				break;

			case FLING_MODE:

				float timePassedSeconds = timePassed / 1000.0f;
				float distance = (mVelocity * timePassedSeconds)
						- (mDeceleration * timePassedSeconds
								* timePassedSeconds / 2.0f);

				_currAngle = _startAngle + (distance * _coeffAngle);
				_currAngle = Math.min(_currAngle, _maxAngle);
				_currAngle = Math.max(_currAngle, _minAngle);
				break;
			}

			if (_currAngle == _finalAngle) {
				mFinished = true;
				return false;
			}
		} else {
			_currAngle = _finalAngle;
			mFinished = true;
			return false;
		}
		return true;
	}

	static float viscousFluid(float x) {
		x *= sViscousFluidScale;
		if (x < 1.0f) {
			x -= (1.0f - (float) Math.exp(-x));
		} else {
			float start = 0.36787944117f; // 1/e == exp(-1)
			x = 1.0f - (float) Math.exp(1.0f - x);
			x = start + x * (1.0f - start);
		}
		x *= sViscousFluidNormalize;
		return x;
	}

	public void abortAnimation() {
		_currAngle = _finalAngle;
		mFinished = true;
	}

	public void extendDuration(int extend) {
		int passed = timePassed();
		mDuration = passed + extend;
		mDurationReciprocal = 1.0f / (float) mDuration;
		mFinished = false;
	}

	public int timePassed() {
		return (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);
	}

	public void setFinalX(int newAngle) {
		_finalAngle = newAngle;
		_deltaAngle = _finalAngle - newAngle;
		mFinished = false;
	}
}
