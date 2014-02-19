/*
    GAMA - Open Source Mobile UI Component Library
    Copyright (c) 2012 Digital Aria Inc.
    licensing@digitalaria.com
    For more details, please visit: http://developer.digitalaria.com/gama/license
*/

package com.digitalaria.gama.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.digitalaria.gama.wheel.WheelAdapter.OnItemClickListener;
import com.digitalaria.gama.wheel.WheelAdapter.OnItemSelectionUpdatedListener;
import com.digitalaria.gama.wheel.WheelAdapter.OnWheelRotationListener;
import com.digitalaria.gama.wheel.WheelAdapter.OnRotateListener;

/**
 * GAMA Wheel component class. This class is a FrameLayout itself. The Wheel
 * component needs item list. Implementers can set its items.
 */
public final class Wheel extends FrameLayout {

	/**
	 * Counter-clockwise.
	 * 
	 * @see #flingStartUsingVelocityWithDirection(int, int, int)
	 */
	public static final int CCW = -1;

	/**
	 * Clockwise.
	 * 
	 * @see #flingStartUsingVelocityWithDirection(int, int, int)
	 */
	public static final int CW = 1;

	private WheelBehavior _wheelBehavior;

	private ViewStub _backgroundViewDummy;
	protected View _backgroundView;

	protected boolean _hasBackgroundImage = false;

	private FrameLayout.LayoutParams params;
	private int _reservedPositionLeft = 0;
	private int _reservedPositionTop = 0;
	private boolean isLayout = false;
	private int _storedLeft;
	private int _storedTop;
	private int _storedRight;
	private int _storedBottom;

	// Coordinates of the center wheel.
	private int _centerX;
	private int _centerY;

	public Wheel(Context context) {
		this(context, null, 0);
	}

	public Wheel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Wheel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setWillNotDraw(true);

		params = new FrameLayout.LayoutParams(context, attrs);

		int layoutIdx = 0;

		_backgroundViewDummy = new ViewStub(context, attrs);
		super.addView(_backgroundViewDummy, layoutIdx++, params);

		_wheelBehavior = new WheelBehavior(context, attrs, defStyle);
		_wheelBehavior.setParentView(this);
		super.addView(_wheelBehavior, layoutIdx++, params);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			if (!isLayout && !_wheelBehavior.isLayouted) {
				isLayout = true;
				_storedLeft = getLeft() + _reservedPositionLeft;
				_storedTop = getTop() + _reservedPositionTop;
				_storedRight = getRight() + _reservedPositionLeft;
				_storedBottom = getBottom() + _reservedPositionTop;
				layout(_storedLeft, _storedTop, _storedRight, _storedBottom);
				return;
			}

			if (isLayout
					&& (_storedLeft == getLeft() + _reservedPositionLeft || _storedTop == getTop()
							+ _reservedPositionTop)) {
				layout(_storedLeft, _storedTop, _storedRight, _storedBottom);
				return;
			}
		}

		super.onLayout(changed, l, t, r, b);
	}

	/**
	 * This method is not supported and throws an UnsupportedOperationException
	 * when called.
	 * 
	 * @param child
	 *            Ignored.
	 * @throws UnsupportedOperationException
	 *             Every time this method is invoked.
	 */
	@Override
	public void addView(View child) {
		throw new UnsupportedOperationException(
				"addView(View) is not supported in Wheel");
	}

	/**
	 * This method is not supported and throws an UnsupportedOperationException
	 * when called.
	 * 
	 * @param child
	 *            Ignored.
	 * @param index
	 *            Ignored.
	 * @throws UnsupportedOperationException
	 *             Every time this method is invoked.
	 */
	@Override
	public void addView(View child, int index) {
		throw new UnsupportedOperationException(
				"addView(View, int) is not supported in Wheel");
	}

	/**
	 * This method is not supported and throws an UnsupportedOperationException
	 * when called.
	 * 
	 * @param child
	 *            Ignored.
	 * @throws UnsupportedOperationException
	 *             Every time this method is invoked.
	 */
	@Override
	public void removeView(View child) {
		throw new UnsupportedOperationException(
				"removeView(View) is not supported in Wheel");
	}

	/**
	 * This method is not supported and throws an UnsupportedOperationException
	 * when called.
	 * 
	 * @param index
	 *            Ignored.
	 * @throws UnsupportedOperationException
	 *             Every time this method is invoked.
	 */
	@Override
	public void removeViewAt(int index) {
		throw new UnsupportedOperationException(
				"removeViewAt(int) is not supported in Wheel");
	}

	/**
	 * Gets the X coordinate of the center in wheel. Its value is calibrated at
	 * the same time.
	 * 
	 * @return The X coordinate of the center in wheel.
	 */
	public float getCenterX() {
		_centerX = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
		return _centerX;
	}

	/**
	 * Gets the Y coordinate of the center in wheel. Its value is calibrated at
	 * the same time.
	 * 
	 * @return The Y coordinate of the center in wheel.
	 */
	public float getCenterY() {
		_centerY = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2
				+ getPaddingTop();
		return _centerY;
	}

	/**
	 * Register a callback to be invoked when an item has been clicked. Set the
	 * {@link OnItemClickListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public final void setOnItemClickListener(
			WheelBehavior.OnItemClickListener listener) {
		_wheelBehavior.setOnItemClickListener(listener);
	}

	/**
	 * @return The callback to be invoked when an item in this adapter has been
	 *         clicked, or null if no callback has been set.
	 */
	public final WheelBehavior.OnItemClickListener getOnItemClickListener() {
		return _wheelBehavior.getOnItemClickListener();
	}

	/**
	 * Register a callback to be invoked when the rotation of wheel has been
	 * finished. Set the {@link OnWheelRotationListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public final void setOnWheelRotationListener(
			WheelBehavior.OnWheelRotationListener listener) {
		_wheelBehavior.setOnWheelRotationListener(listener);
	}

	/**
	 * @return The callback to be invoked when the rotation of wheel has been
	 *         finished, or null if no callback haas been set.
	 */
	public final WheelBehavior.OnWheelRotationListener getOnWheelRotationListener() {
		return _wheelBehavior.getOnWheelRotationListener();
	}

	/**
	 * Register a callback to be invoked when an item has been selected. Sets
	 * the {@link OnItemSelectionUpdatedListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public void setOnItemSelectionUpdatedListener(
			WheelAdapter.OnItemSelectionUpdatedListener listener) {
		_wheelBehavior.setOnItemSelectionUpdatedListener(listener);
	}

	/**
	 * Sets the position of wheel.
	 * 
	 * @param left
	 *            Left position, relative to parent.
	 * @param top
	 *            Top position, relative to parent.
	 */
	public void setPosition(int left, int top) {
		if (!_wheelBehavior.isLayouted) {
			_reservedPositionLeft = left;
			_reservedPositionTop = top;
		}
		invalidate();
	}

	/**
	 * Sets the selection position of the wheel component by angle. The item is
	 * selected when the item is in the selection position. (12 o'clock is 0
	 * degrees)
	 * 
	 * @param angle
	 *            The position of selection which is center angle of Selection
	 *            Area. (0<= angle < 360)
	 */
	public void setSelectionAngle(int angle) {
		_wheelBehavior.setSelectionAngleOffset(angle);
	}

	/**
	 * Sets the items for the wheel as a TypedArray type. Items will be
	 * displayed in order of entering in array.
	 * 
	 * @param items
	 *            The item list for the wheel as a TypedArray type. It will be
	 *            consisted of a Drawable type.
	 */
	public void setItems(TypedArray items) {
		if (_wheelBehavior == null)
			return;
		_wheelBehavior.setItems(items);
	}

	/**
	 * Sets the items for the wheel as a Drawable[] type. Items will be
	 * displayed in order of entering in array.
	 * 
	 * @param drawables
	 *            The item list for the wheel.
	 */
	public void setItems(Drawable[] drawables) {
		if (_wheelBehavior == null)
			return;
		_wheelBehavior.setItems(drawables);
	}

	/**
	 * Sets the diameter of the wheel. Depending on the location of the item is
	 * placed in a circle.
	 * 
	 * @param diameter
	 *            The diameter of the wheel.
	 */
	public void setWheelDiameter(int diameter) {
		if (_wheelBehavior == null)
			return;
		_wheelBehavior.setWheelDiameter(diameter);
	}

	/**
	 * Sets the background image for the wheel. Defines the id taken by the
	 * inflated view and specifies the layout resource to inflate.
	 * 
	 * @param inflatedId
	 *            A positive integer used to identify the inflated view or NO_ID
	 *            if the inflated view should keep its id.
	 * @param layoutResource
	 *            A valid layout resource identifier (different from 0.)
	 * @see ViewStub#setInflatedId(int)
	 * @see ViewStub#setLayoutResource(int)
	 */
	public void setWheelBackground(int inflatedId, int layoutResource) {
		if (_backgroundViewDummy == null)
			return;

		_backgroundViewDummy.setInflatedId(inflatedId);
		_backgroundViewDummy.setLayoutResource(layoutResource);
		_backgroundView = _backgroundViewDummy.inflate();

		if (_backgroundView != null)
			_hasBackgroundImage = true;
	}

	/**
	 * Sets whether or not the each items of wheel should display as rotated
	 * item toward center of the wheel.
	 * 
	 * @param flag
	 *            If you want a rotated item, true. otherwise, false.
	 */
	public void setRotatedItem(boolean flag) {
		if (_wheelBehavior == null)
			return;
		_wheelBehavior.setRotatedItem(flag);
	}

	/**
	 * Configures the initial rotation angle of background image, and the
	 * background rotation.
	 * 
	 * @param initRotationAngle
	 *            The initial rotation angle of background image.
	 * @param rotatedItem
	 *            If true, this background will rotate according to the wheel
	 *            rotation.
	 * @see #configureWheelBackground(boolean)
	 */
	public void configureWheelBackground(int initRotationAngle,
			boolean rotatedItem) {
		if (!_hasBackgroundImage)
			return;

		_backgroundView.setRotation(initRotationAngle);

		_wheelBehavior.setBackgroundInitRotationAngle(initRotationAngle);
		if (rotatedItem) {
			_wheelBehavior.setOnRotateListener(new OnRotateListener() {

				@Override
				public void onWheelRotate(int baseAngle, float angle) {
					_backgroundView.setRotation(baseAngle + angle);
				}
			});
		}
	}

	/**
	 * Sets the initial rotation angle of background image with the selection
	 * position in {@link #setSelectionAngle(int)}. It works the same way as the
	 * {@link #configureWheelBackground(int, boolean)}.
	 * 
	 * @param rotatedItem
	 *            If true, this background will rotate according to the wheel
	 *            rotation.
	 * @see #configureWheelBackground(int, boolean)
	 */
	public void configureWheelBackground(boolean rotatedItem) {
		configureWheelBackground(90 - _wheelBehavior.selectionAngleOffset,
				rotatedItem);
	}

	protected boolean hasBackgroundImage() {
		return _hasBackgroundImage;
	}

	/**
	 * Sets the touch input area from _touchAreaFrom to _touchAreaTo. Touch
	 * event on the area will propagate to the wheel.
	 * 
	 * @param from
	 *            Starting point of the touch area. It is the distance from the
	 *            origin.
	 * @param to
	 *            End point of the touch area. It is the distance from the
	 *            origin.
	 * @exception IllegalArgumentException
	 *                If the value of the end point(to) is greater than the
	 *                value of the starting point(from).
	 */
	public void setTouchArea(int from, int to) {
		if (from < to) {
			_wheelBehavior.setTouchArea(from, to);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Moves to next item. The wheel will be rotated in the {@link #CW}
	 * direction.
	 * 
	 * @return The index of next item.
	 */
	public int nextItem() {
		return _wheelBehavior.nextItem();
	}

	/**
	 * Moves to previous item. The wheel will be rotated in the {@link #CCW}
	 * direction.
	 * 
	 * @return The index of previous item.
	 */
	public int previousItem() {
		return _wheelBehavior.previousItem();
	}

	/**
	 * Returns whether the Wheel has finished rotating.
	 * 
	 * @return True if the Wheel has finished rotating, false otherwise.
	 */
	public boolean isRotationFinished() {
		return _wheelBehavior.isRotationFinished();
	}

	/**
	 * Requests the start of a fling event with the angle that is target
	 * position from current position. And then the nearest item at selection
	 * position will move to selection position after a fling.
	 * 
	 * @param angle
	 *            The angle to move.
	 */
	public void flingStartUsingAngle(float angle) {
		_wheelBehavior.flingStartUsingAngle(angle);
	}

	/**
	 * Requests the start of a fling event with the initial speed. It depends on
	 * the UI thread of android.
	 * 
	 * @param vx
	 *            The velocity of this fling measured in pixels per second along
	 *            the x axis.
	 * @param vy
	 *            The velocity of this fling measured in pixels per second along
	 *            the y axis.
	 * @param scroolToSlot
	 *            True, the nearest item at selection position will move to
	 *            selection position after a fling, false otherwise.
	 */
	public void flingStartUsingVelocity(int vx, int vy, boolean scroolToSlot) {
		_wheelBehavior.flingStartUsingVelocity(0, vx, vy, scroolToSlot);
	}

	/**
	 * Requests the start of a fling event with the initial speed and direction.
	 * It depends on the UI thread of android.
	 * 
	 * @param vx
	 *            The velocity of this fling measured in pixels per second along
	 *            the x axis.
	 * @param vy
	 *            The velocity of this fling measured in pixels per second along
	 *            the y axis.
	 * 
	 * @param direction
	 *            A direction of a fling event. One of {@link #CCW} or
	 *            {@link #CW}. (The default is {@link #CCW}.)
	 */
	public void flingStartUsingVelocityWithDirection(int vx, int vy,
			int direction) {
		_wheelBehavior.flingStartUsingVelocityWithDirection(vx, vy, direction);
	}

	/**
	 * @return The index of item that is in the selection position currently. If
	 *         there is no selected item, it returns the -1.
	 */
	public int getSelectedItem() {
		return _wheelBehavior.getSelectedItem();
	}

	/**
	 * Gets the index of item that is in the selection position currently.
	 * 
	 * @param stopScroll
	 *            If true, it will be stop at once. If false, it works the same
	 *            way as the {@link #getSelectedItem()}.
	 * 
	 * @return The index of item that is in the selection position currently. If
	 *         there is no selected item, it returns the -1.
	 */
	public int getSelectedItem(boolean stopScroll) {
		return _wheelBehavior.getSelectedItem(stopScroll);
	}

	/**
	 * Sets the item to selecting status. The selected item of wheel will be
	 * moved to selection position.
	 * 
	 * @param index
	 *            The index of item that will be selected.
	 * @return True if succeeded, false otherwise.
	 */
	public boolean setSelectedItem(int index) {
		return _wheelBehavior.setSelectedItem(index);
	}

	/**
	 * If the item are located around the selection position when the wheel
	 * stops rotating, it will dispatch a item click event.
	 * 
	 * @param enable
	 *            Whether the item click event dispatched
	 * @see WheelAdapter.OnItemClickListener
	 * @see Wheel#getItemClickEventAtSelectionPosition()
	 */
	public void setItemClickEventAtSelectionPosition(boolean enable) {
		_wheelBehavior.setSelectionPositionItemClickEvent(enable);
	}

	/**
	 * @return True if the item click event at selection position is set, false
	 *         otherwise.
	 * @see Wheel#setItemClickEventAtSelectionPosition(boolean)
	 */
	public boolean getItemClickEventAtSelectionPosition() {
		return _wheelBehavior.getSelectionPositionItemClickEvent();
	}
	
	/**
	 * Set the enabled state of this wheel. If the disabled state is set, this
	 * wheel will not be rotated anymore.
	 * 
	 * @param enabled
	 *            True if this wheel is enabled, false otherwise.
	 */	
	@Override
	public void setEnabled(boolean enabled) {
		_wheelBehavior.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	/**
	 * Indicates whether or not this wheel's layout have only laid once.
	 * 
	 * @return True if the wheel is currently laid, false otherwise.
	 */
	public boolean isLayouted() {
		return _wheelBehavior.isLayouted;
	}
}