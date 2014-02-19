/*
    GAMA - Open Source Mobile UI Component Library
    Copyright (c) 2012 Digital Aria Inc.
    licensing@digitalaria.com
    For more details, please visit: http://developer.digitalaria.com/gama/license
*/

package com.digitalaria.gama.wheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tvdashboard.database.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;


final class WheelBehavior extends WheelAdapter<Adapter> implements
		GestureDetector.OnGestureListener {

	private Wheel _parent;
	private int _rotationDirection = Wheel.CW;
	private int _backgroundInitRotationAngle;
	
	public static int tmp;

	private int _rotationAnimationDuration = 450;
	private int _wheelDiameter = 250;

	private MotionEvent _lastMotionEvent;

	private boolean _hasRotatedItem = false;

	// set the touch area from _touchAreaFrom to _touchAreaTo.
	private int _touchAreaFrom = 0;
	private int _touchAreaTo = 0;
	private boolean _hasTouchArea = false;

	private boolean _enabled;

	/**
	 * Item position that touched by user. Default is INVALID_ID.
	 */
	private int _downTouchPosition = INVALID_ID;

	/**
	 * Item view that touched by {@link #_downTouchPosition}.
	 */
	private View _downTouchView;

	private RotationRunnable _rotationRunnable = new RotationRunnable();

	private GestureDetector _gestureHandler;

	private Adapter _adapter;

	/**
	 * A size of most big item. It gets a big size between width and height.
	 * TODO 
	 * 1) If each item has a different size.. 
	 * 2) If an item is a rectangle..
	 */
	private int _itemSize = 0;

	/**
	 * AngleOffset of the selection position. Touched item will move to this
	 * position.
	 */
	protected int selectionAngleOffset = 0; // Selected item angle
	private Rect _touchFrame;
	protected boolean isLayouted = false;
	private float _slotAngleUnit = 0;
	private int _selectionPosition = INVALID_ID;
	private boolean _isMoveItemIntoSlot;
	private boolean _doNotCallBack = false;
	private boolean _selectionPositionItemClickEvent = false;
	private long _lastScrollEvent;

	WheelBehavior(Context context) {
		super(context);
		throw new UnsupportedOperationException();
	}

	WheelBehavior(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	WheelBehavior(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		_gestureHandler = new GestureDetector(this); // FIXME deprecated..
		_gestureHandler.setIsLongpressEnabled(true);

		initWheelBehavior();

		TypedArray arr = getContext().obtainStyledAttributes(attrs,
				R.styleable.Wheel);
		{
			setAnimationDuration(arr.getInteger(
					R.styleable.Wheel_wheel_rotation_duration,
					Configuration.DEFAULT_WHEEL_ROTATION_DURATION));
			setWheelDiameter(arr.getInteger(R.styleable.Wheel_wheel_diameter,
					Configuration.DEFAULT_WHEEL_DIAMETER));

			int selectedItem = arr.getInteger(
					R.styleable.Wheel_item_selected_index, 0);
			_selectedItemID = selectedItem;

			int itemArrayID = arr.getResourceId(R.styleable.Wheel_items, -1);
			if (itemArrayID != -1) {
				TypedArray images = getResources()
						.obtainTypedArray(itemArrayID);
				setItems(images);
			}
		}
	}

	protected void setItems(TypedArray items) {
		if (items == null)
			return;

		ItemAdapter adapter = new ItemAdapter(getContext());
		adapter.setItems(items);
		verifySelectedItemID();
		setAdapter(adapter);
	}

	protected void setItems(Drawable[] drawables) {
		if (drawables == null)
			return;

		ItemAdapter adapter = new ItemAdapter(getContext());
		adapter.setItems(drawables);
		verifySelectedItemID();
		setAdapter(adapter);
	}

	private void verifySelectedItemID() {
		if (_selectedItemID < -1
				|| (getAdapter() != null && _selectedItemID > getAdapter()
						.getCount())) {
			if(_selectedItemID != INVALID_ID) _selectedItemID = 0;
		}
	}

	private void initWheelBehavior() {
		setFocusable(true);
		setStaticTransformationsEnabled(true);
		setWillNotDraw(true);
	}

	@Override
	public Adapter getAdapter() {
		return _adapter;
	}

	@Override
	public void setAdapter(Adapter adapter) {

		if (_adapter != null)
			resetList();

		_adapter = adapter;

		if (_adapter != null) {
			setCount(_adapter.getCount());
			_slotAngleUnit = 360.0f / getCount();
		} else {
			resetList();
		}

		requestLayout();
	}

	private void resetList() {
		removeAllViewsInLayout();
		invalidate();
	}

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int w = measureReconcile(widthMeasureSpec,
				Configuration.MEASURE_WIDTH_TYPE);
		int h = measureReconcile(heightMeasureSpec,
				Configuration.MEASURE_HEIGHT_TYPE);
		setMeasuredDimension(w, h);
	}

	private int measureReconcile(int measureSpec, int type) {
		int ret = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			ret = specSize;
		} else {
			if (type == Configuration.MEASURE_WIDTH_TYPE) {
				ret = getWheelDiameter() + (getItemSize() * 2)
						+ getPaddingRight() + getPaddingLeft();
				if (_parent.hasBackgroundImage()
						&& ret < _parent._backgroundView.getMeasuredWidth()) {
					ret = _parent._backgroundView.getMeasuredWidth();
				}
			} else {
				ret = getWheelDiameter() + (getItemSize() * 2)
						+ getPaddingBottom() + getPaddingTop();
				if (_parent.hasBackgroundImage()
						&& ret < _parent._backgroundView.getMeasuredHeight()) {
					ret = _parent._backgroundView.getMeasuredHeight();
				}
			}

			if (specMode == MeasureSpec.AT_MOST)
				ret = Math.min(ret, specSize);
		}
		return ret;
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	private int pointToPosition(int x, int y) {
		Rect frame = _touchFrame;
		if (frame == null) {
			_touchFrame = new Rect();
			frame = _touchFrame;
		}

		for (int i = getCount() - 1; i >= 0; i--) {
			WheelItemView child = (WheelItemView) getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				getHitRect(child, frame);
				if (frame.contains(x, y)) {
					return i;
				}
			}
		}

		return INVALID_ID;
	}

	private void getHitRect(WheelItemView view, Rect outRect) {

		int x = (int) (view.getAxisX() + _parent.getCenterX() - _itemSize / 2);
		int y = (int) (view.getAxisY() + _parent.getCenterY() - _itemSize / 2);

		int l = (int) (x - Configuration.ITEM_CLICK_AREA_PADDING);
		int t = (int) (y - Configuration.ITEM_CLICK_AREA_PADDING);
		int r = (int) (view.getItemWidth() + x + Configuration.ITEM_CLICK_AREA_PADDING);
		int b = (int) (view.getItemHeight() + y + Configuration.ITEM_CLICK_AREA_PADDING);

		outRect.set(l, t, r, b);
	}

	protected void setWheelDiameter(int _wheelDiameter) {
		this._wheelDiameter = _wheelDiameter;
	}

	protected int getWheelDiameter() {
		return _wheelDiameter;
	}

	protected void setItemSize(int _itemSize) {
		this._itemSize = _itemSize;
	}

	protected int getItemSize() {
		return _itemSize;
	}

	private class ItemAdapter extends BaseAdapter {

		private Context _context;
		private WheelItemView[] _itemImages;

		public ItemAdapter(Context c) {
			_context = c;
		}

		public void setItems(TypedArray array) {

			Drawable[] drawables = new Drawable[array.length()];
			int arLen = array.length();
			_itemImages = new WheelItemView[arLen];

			for (int i = 0; i < arLen; i++) {
				drawables[i] = array.getDrawable(i);
				Bitmap originalImage = ((BitmapDrawable) drawables[i])
						.getBitmap();
				setItemSize(Math.max(
						getItemSize(),
						Math.max(originalImage.getWidth(),
								originalImage.getHeight())));
				WheelItemView imgView = new WheelItemView(_context);
				imgView.setImageBitmap(originalImage);
				imgView.setIndex(i);
				imgView.setScaleType(ScaleType.MATRIX);

				_itemImages[i] = imgView;
			}
		}

		public void setItems(Drawable[] drawables) {
			int len = drawables.length;
			_itemImages = new WheelItemView[len];

			for (int i = 0; i < len; i++) {
				Bitmap originalImage = ((BitmapDrawable) drawables[i]).getBitmap();
				setItemSize(Math.max(
						getItemSize(),
						Math.max(originalImage.getWidth(), originalImage.getHeight())));
				WheelItemView imgView = new WheelItemView(_context);
				imgView.setImageBitmap(originalImage);
				imgView.setIndex(i);
				imgView.setScaleType(ScaleType.MATRIX);

				_itemImages[i] = imgView;
			}
		}

		@Override
		public int getCount() {
			if (_itemImages == null)
				return 0;
			else
				return _itemImages.length;
		}

		@Override
		public WheelItemView getItem(int position) {
			return _itemImages[position];
		}

		@Override
		public long getItemId(int position) {
			return _itemImages[position].getIndex();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return _itemImages[position];
		}
	}

	private class RotationRunnable implements Runnable {

		private Rotator _rotator;
		private float _lastFlingAngle;
		private boolean _requestScrollIntoSlots;

		public RotationRunnable() {
			_rotator = new Rotator(getContext());
			_requestScrollIntoSlots = false;
			_enabled = true;
		}

		private void startCommon() {
			removeCallbacks(this);
			if (_onWheelRotationListener != null)
				_onWheelRotationListener.onWheelRotationStart();
		}

		public void setEnabled(boolean enabled) {
			if (!enabled)
				stop(true);
			_enabled = enabled;
		}

		public void startUsingVelocity(int initialVelocity, int vx, int vy,
				boolean requestScrollIntoSlots) {
			if (!_enabled)
				return;
			_requestScrollIntoSlots = requestScrollIntoSlots;
			startCommon();

			int initialAngle = initialVelocity < 0 ? Integer.MAX_VALUE : 0;
			_lastFlingAngle = initialAngle;
			_rotator.fling(initialVelocity, vx, vy);
			post(this);
		}

		public void startUsingAngle(float deltaAngle,
				boolean requestScrollIntoSlots, int duration) {
			if (!_enabled)
				return;
			if (deltaAngle == 0)
				return;
			_requestScrollIntoSlots = requestScrollIntoSlots;
			startCommon();

			_lastFlingAngle = 0f;
			_rotator.scroll(0, (int) deltaAngle, duration);
			post(this);
		}

		public void startUsingAngle(float deltaAngle,
				boolean requestScrollIntoSlots) {
			if (!_enabled)
				return;
			startUsingAngle(deltaAngle, requestScrollIntoSlots,
					getAnimationDuration());
		}

		public void stop(boolean scrollIntoSlots) {
			removeCallbacks(this);
			endFling(scrollIntoSlots);
		}

		private void endFling(boolean scrollIntoSlots) {
			_rotator.forceFinished(true);
			_doNotCallBack = _isMoveItemIntoSlot = false;

			if (_onWheelRotationListener != null)
				_onWheelRotationListener.onWheelRotationEnd();

			if (scrollIntoSlots) {
				scrollIntoSlots();
			}
		}

		public Boolean isFinished() {
			return _rotator.isFinished();
		}

		public void run() {
			if (getChildCount() == 0) {
				stop(false);
				return;
			}

			final Rotator rotator = _rotator;
			boolean more = rotator.computeAngleOffset();
			final float curAngle = rotator.getCurrentAngle();

			float delta = _lastFlingAngle - curAngle;

			if (_isMoveItemIntoSlot) {
				trackMotionRotate(-1 * delta);
			} else {
				trackMotionRotate(-1 * _rotationDirection * delta);
			}

			if (more) {
				_lastFlingAngle = curAngle;
				post(this);
			} else {
				endFling(_requestScrollIntoSlots);
			}
		}
	}

	/**
	 * The position of child view is calculated.
	 * 
	 * @param child
	 * @param diameter
	 * @param angleOffset
	 */
	private void calculatePosition(WheelItemView child, int diameter,
			float angleOffset) {

		angleOffset = (float) (Math.toRadians(angleOffset));
		float x = FloatMath.cos(angleOffset) * diameter / 2f;
		float y = FloatMath.sin(angleOffset) * diameter / 2f;

		child.setAxisX(x);
		child.setAxisY(y);

		if (child.isHardwareAccelerated())
			child.invalidate();
	}

	private boolean _isInitBG = false;
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		long now = SystemClock.uptimeMillis();
		if (Math.abs(now - _lastScrollEvent) < Configuration.LAYOUT_DELAY)
			return;

		detachAllViewsFromParent();

		float angleOffset = 0;
		if (sumOfDeltaAngle == 0) {
			// At first, a start position of item is set.
			angleOffset = (_selectedItemID == INVALID_ID) 
							? selectionAngleOffset	
							: _selectedItemID * _slotAngleUnit + selectionAngleOffset;
			
			if (_parent._hasBackgroundImage &&  _selectedItemID != INVALID_ID && !_isInitBG) {
				float pr = _parent._backgroundView.getRotation() - (_selectedItemID * _slotAngleUnit);
				_parent._backgroundView.setRotation(pr);
				setBackgroundInitRotationAngle((int) pr);
				_isInitBG = true;
			}
		} else {
			angleOffset = selectionAngleOffset - sumOfDeltaAngle;
		}
		
		for (int i = 0; i < getCount(); i++) {
			float angle = _slotAngleUnit * i - angleOffset;
			if (angle < 0.0f)
				angle = 360.0f + angle;
			setUpChild(i, angle);
		}

		isLayouted = true;
		if(_tempSelectedItemID != INVALID_ID) {
			_selectedItemID = INVALID_ID;
			setSelectedItem(_tempSelectedItemID);
			_tempSelectedItemID = INVALID_ID;
		}
	}

	/**
	 * Set the position of a item and fill out its layout parameters.
	 * 
	 * @param position
	 *            Item's position.
	 * @param angleOffset
	 *            Offset from the selected position.
	 */
	private void setUpChild(int position, float angleOffset) {

		WheelItemView child = (WheelItemView) getAdapter().getView(position,
				null, this);
		int index = child.getIndex();

		addViewInLayout(child, index, generateDefaultLayoutParams());
		child.setSelected(index == _selectedItemID);
		child.setCurrentAngle(angleOffset);
		child.setRotatedItem(_hasRotatedItem);

		int h = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
		int w = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

		calculatePosition(child, getWheelDiameter(), angleOffset);
		child.layout(-child.getDistanceX(), -child.getDistanceY(),
				w - child.getDistanceX(), h - child.getDistanceY());
	}

	/**
	 * Moves an item to nearest selectionAngleOffset and sets it selected.
	 */
	private void scrollIntoSlots() {
		if (getChildCount() == 0)
			return;

		// sorting...
		ArrayList<WheelItemView> arr = new ArrayList<WheelItemView>();

		for (int i = 0; i < getCount(); i++)
			arr.add(((WheelItemView) getAdapter().getView(i, null, null)));

		Collections.sort(arr, new Comparator<WheelItemView>() {
			@Override
			public int compare(WheelItemView c1, WheelItemView c2) {
				int a1 = (int) c1.getCurrentAngle();
				int a2 = (int) c2.getCurrentAngle();
				return (a1 - a2);
			}
		});

		// binary searching... choose the nearest item with
		// selectionAngleOffset.
		int low = 0;
		int mid = 0;
		int high = arr.size() - 1;
		float target = 360 - selectionAngleOffset;
		WheelItemView targetView = null;
		float distance = 360;

		while (low <= high) {
			mid = (low + high) / 2;
			WheelItemView tmpView = (WheelItemView) arr.get(mid);
			if (tmpView.getCurrentAngle() < target) {
				low = mid + 1;
			} else if (tmpView.getCurrentAngle() > target) {
				high = mid - 1;
			}

			float temp = Math.abs(target - tmpView.getCurrentAngle());
			if (distance >= temp) {
				distance = temp;
				targetView = tmpView;
			}

			if (distance == 0)
				break;
		}

		if (targetView != null) {
			_downTouchView = targetView;

			_isMoveItemIntoSlot = true;
			moveItemIntoSlot(targetView.getIndex());

			if (_selectionPositionItemClickEvent) {
				dispatchItemClick(_downTouchView, _downTouchPosition,
						getAdapter().getItemId(_downTouchPosition));
			}
		}
	}

	/**
	 * Tracks a motion rotation. Position of current item is calculated. This is
	 * used to do just about any movement to items.
	 * 
	 * @param deltaAngle
	 *            Change in angle from the previous event.
	 */
	private void trackMotionRotate(float deltaAngle) {
		if (getChildCount() == 0 || deltaAngle == 0) {
			return;
		}

		if (_onRotateListener != null) {
			sumOfDeltaAngle += deltaAngle;
			if (sumOfDeltaAngle > 360.0f)
				sumOfDeltaAngle -= 360.0f;
			if (sumOfDeltaAngle < 0.0f)
				sumOfDeltaAngle += 360.0f;
			_onRotateListener.onWheelRotate(_backgroundInitRotationAngle,
					sumOfDeltaAngle);
		}

		float sAngle = 0, eAngle = 0;
		int tempSelectionPosition = INVALID_ID;

		if (_onItemSelectionUpdatedListener != null) {
			sAngle = 360 - selectionAngleOffset - (_slotAngleUnit / 2);
			eAngle = 360 - selectionAngleOffset + (_slotAngleUnit / 2);
		}

		for (int i = getCount() - 1; i >= 0; i--) {
			WheelItemView child = (WheelItemView) getAdapter().getView(i, null,
					null);
			float angle = child.getCurrentAngle();
			angle += deltaAngle;
			while (angle > 360.0f)
				angle -= 360.0f;
			while (angle < 0.0f)
				angle += 360.0f;
			child.setCurrentAngle(angle);

			if (_onItemSelectionUpdatedListener != null && sAngle <= angle
					&& angle <= eAngle) {
				tempSelectionPosition = child.getIndex();
			}

			calculatePosition(child, getWheelDiameter(), angle);
		}

		if (!_doNotCallBack && _onItemSelectionUpdatedListener != null
				&& _selectionPosition != tempSelectionPosition) {
			WheelItemView v = (WheelItemView) getAdapter().getView(
					tempSelectionPosition, null, null);
			_onItemSelectionUpdatedListener.onItemSelectionUpdated(v,
					v.getIndex());
			_selectionPosition = tempSelectionPosition;
		}

		invalidate();
	}

	/**
	 * Transform an item depending on its coordinates.
	 */
	@Override
	protected boolean getChildStaticTransformation(View child,
			Transformation transformation) {
		transformation.clear();
		transformation.setTransformationType(Transformation.TYPE_MATRIX);

		final Matrix m = transformation.getMatrix();
		float dx = _parent.getCenterX() - (_itemSize / 2)
				+ ((WheelItemView) child).getAxisX();
		float dy = _parent.getCenterY() - (_itemSize / 2)
				+ ((WheelItemView) child).getAxisY();
		m.postTranslate(dx, dy);

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		getParent().requestDisallowInterceptTouchEvent(true);

		synchronized (WheelBehavior.this) {
			float ay, bx, by, theta;
			float v;
			_lastScrollEvent = SystemClock.uptimeMillis();

			ay = -_parent.getCenterY();
			bx = e2.getX() - _parent.getCenterX();
			by = e2.getY() - _parent.getCenterY();

			v = FloatMath.sqrt(ay * ay);
			ay /= v;
			v = FloatMath.sqrt(bx * bx + by * by);
			by /= v;
			theta = (float) Math.acos(ay * by);
			float angle1 = (float) (Math.toDegrees(theta));

			bx = _lastMotionEvent.getX() - _parent.getCenterX();
			by = _lastMotionEvent.getY() - _parent.getCenterY();

			v = FloatMath.sqrt(bx * bx + by * by);
			by /= v;
			theta = (float) Math.acos(ay * by);
			float angle2 = (float) (Math.toDegrees(theta));

			float angle = angle1 - angle2;

			if (e2.getX() >= _parent.getCenterX()) {
				if (angle > 0) {
					_rotationDirection = Wheel.CW;
				} else {
					_rotationDirection = Wheel.CCW;
				}

				trackMotionRotate(angle);
			} else {
				if (angle < 0) {
					_rotationDirection = Wheel.CW;
				} else {
					_rotationDirection = Wheel.CCW;
				}

				trackMotionRotate(-angle);
			}

			_lastMotionEvent = MotionEvent.obtain(e2);
		}
		return true;
	}

	protected void setAnimationDuration(int _animationDuration) {
		_rotationAnimationDuration = _animationDuration;
	}

	protected int getAnimationDuration() {
		return _rotationAnimationDuration;
	}

	protected void flingStartUsingAngle(float deltaAngle) {
		_rotationRunnable.startUsingAngle(deltaAngle, true);
	}

	protected void flingStartUsingVelocity(int initVelocity, int velocityX,
			int velocityY, boolean scroolToSlot) {
		_rotationRunnable.startUsingVelocity(initVelocity, velocityX,
				velocityY, scroolToSlot);
	}

	protected void flingStartUsingVelocityWithDirection(int vx, int vy,
			int direction) {
		if (direction == 0 || 1 < direction || direction < -1)
			direction = Wheel.CW;
		_isMoveItemIntoSlot = false;
		_rotationDirection = direction;
		_rotationRunnable.startUsingVelocity(0, vx, vy, false);
	}

	protected void flingStop(boolean scrollIntoSlots) {
		_rotationRunnable.stop(scrollIntoSlots);
	}

	protected Boolean isRotationFinished() {
		return _rotationRunnable.isFinished();
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (!_enabled)
			return false;
		boolean ret = _gestureHandler.onTouchEvent(me);

		if (me.getAction() == MotionEvent.ACTION_UP
				&& _rotationRunnable.isFinished()) {
			scrollIntoSlots();
		}

		return ret;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		_lastMotionEvent = MotionEvent.obtain(e);

		// NOTE a range from the center of wheel to a coordinates of the
		// clicked.
		float dx = e.getX() - _parent.getCenterX();
		float dy = e.getY() - _parent.getCenterY();
		float r = FloatMath.sqrt(dx * dx + dy * dy);

		if (!_hasTouchArea) {
			if ((getWheelDiameter() / 2 - getItemSize() > r)) {
				return false;
			}
			runOnDown(e);
			return true;
		} else {
			if (_touchAreaFrom / 2 < r && r < _touchAreaTo / 2) {
				runOnDown(e);
				return true;
			}
			return false;
		}
	}

	private void runOnDown(MotionEvent motion) {
		_rotationRunnable.stop(false);

		int p = pointToPosition((int) motion.getX(), (int) motion.getY());
		if (p != INVALID_ID) {
			_selectedItemID = p;
		} else {
			_selectedItemID = INVALID_ID;
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (_selectedItemID != INVALID_ID) {
			_downTouchView = getChildAt(_selectedItemID);

			_isMoveItemIntoSlot = true;
			moveItemIntoSlot(_selectedItemID);
			dispatchItemClick(_downTouchView, _downTouchPosition, getAdapter()
					.getItemId(_downTouchPosition));

			return true;
		}

		return false;
	}

	private void moveItemIntoSlot(int itemIndex) {
		_selectedItemID = _downTouchPosition = itemIndex;
		if (!isLayouted) {
			if(_tempSelectedItemID == INVALID_ID)
				_tempSelectedItemID = itemIndex;
			return;	
		}
		

		WheelItemView targetView = ((WheelItemView) getItemAtPosition(itemIndex));
		float angle = targetView.getCurrentAngle();
		float targetAngle = 360f - selectionAngleOffset;

		// move to the nearest selection position.
		float half = (targetAngle + 180.0f >= 360f) ? targetAngle - 180.0f
				: targetAngle + 180.0f;
		float delta = (angle >= half) ? targetAngle - angle
				: -(360 - targetAngle + angle);

		if (Math.abs(delta) < 0.5f) {
			return;
		}

		_rotationRunnable.startUsingAngle(delta, false);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		_isMoveItemIntoSlot = false;
		flingStartUsingVelocity(0, (int) velocityX, (int) velocityY, true);
		return true;
	}

	protected void setSelectionAngleOffset(int angle) {
		selectionAngleOffset = 0 - angle;
		_isInitBG = false;
		requestLayout();
	}

	protected float getSelectionAngleOffset() {
		return selectionAngleOffset;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	protected void setRotatedItem(boolean flag) {
		_hasRotatedItem = flag;
		if (isLayouted)
			onLayout(true, getLeft(), getTop(), getRight(), getBottom());
	}

	protected void setBackgroundInitRotationAngle(int initRotationAngle) {
		_backgroundInitRotationAngle = initRotationAngle;
	}

	protected void setParentView(Wheel wheel) {
		_parent = wheel;
	}

	protected void setTouchArea(int from, int to) {
		_touchAreaFrom = from;
		_touchAreaTo = to;
		_hasTouchArea = true;
	}

	protected int nextItem() {
		_rotationDirection = Wheel.CW;
		_isMoveItemIntoSlot = false;
		_doNotCallBack = false;
		scrollIntoSlots();
//		int tmp = (_selectedItemID + 1) % getCount();
		tmp = (_selectedItemID + 1);
		if (tmp > (getCount() - 1) )
			tmp = 0;
		moveItemIntoSlot(tmp);
		return tmp;
	}

	protected int previousItem() {
		_rotationDirection = Wheel.CCW;
		_isMoveItemIntoSlot = false;
		_doNotCallBack = false;
		scrollIntoSlots();
		tmp = (_selectedItemID - 1);
		if (tmp < 0)
			tmp = getCount() - 1;
		moveItemIntoSlot(tmp);
		return tmp;
	}

	protected int getSelectedItem() {
		return _selectedItemID;
	}

	protected int getSelectedItem(boolean stopScroll) {
		int id = getSelectedItem();
		if (stopScroll)
			setSelectedItem(id);
		return id;
	}

	protected boolean setSelectedItem(int index) {
		if (index <= INVALID_ID || index >= getCount()
				|| _selectedItemID == index)
			return false;

		_isMoveItemIntoSlot = true;
		_doNotCallBack = true;
		moveItemIntoSlot(index);

		_downTouchView = getChildAt(index);
		return true;
	}

	protected void setSelectionPositionItemClickEvent(boolean enable) {
		_selectionPositionItemClickEvent = enable;
	}

	protected boolean getSelectionPositionItemClickEvent() {
		return _selectionPositionItemClickEvent;
	}

	@Override
	public void setEnabled(boolean enabled) {
		_rotationRunnable.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return _enabled;
	}
}
