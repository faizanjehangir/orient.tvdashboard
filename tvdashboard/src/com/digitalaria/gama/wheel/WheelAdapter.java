/*
    GAMA - Open Source Mobile UI Component Library
    Copyright (c) 2012 Digital Aria Inc.
    licensing@digitalaria.com
    For more details, please visit: http://developer.digitalaria.com/gama/license
*/

package com.digitalaria.gama.wheel;


import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

public abstract class WheelAdapter<T extends Adapter> extends ViewGroup {

	/**
	 * If an item is invalid, the ID of item is set by {@link #INVALID_ID}.
	 */
	static final int INVALID_ID = -1;

	OnItemClickListener _onItemClickListener;
	OnRotateListener _onRotateListener;
	OnWheelRotationListener _onWheelRotationListener;
	OnItemSelectionUpdatedListener _onItemSelectionUpdatedListener;

	float sumOfDeltaAngle = 0;

	/**
	 * The number of items registered in the entire adapter.
	 */
	private int _itemCount;

	/**
	 * The ID of the currently selected item in the adapter
	 */
	int _selectedItemID = INVALID_ID;
	protected int _tempSelectedItemID = INVALID_ID;

	WheelAdapter(Context context) {
		super(context);
	}

	WheelAdapter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	WheelAdapter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * WheelAdapter has been clicked.
	 */
	public interface OnItemClickListener {
		/**
		 * Callback method to be invoked when an item in this WheelAdapter has
		 * been clicked.
		 * <p>
		 * Implementers can call getItemAtPosition() if they need to access the
		 * data associated with the selected item.
		 * 
		 * @param parent
		 *            The WheelAdapter where the click happened.
		 * @param view
		 *            The view within the WheelAdapter that was clicked.
		 * @param position
		 *            The position of the item view in the adapter.
		 * @param id
		 *            The ID of the item that was clicked.
		 */
		void onItemClick(WheelAdapter<?> parent, View view, int position,
				long id);
	}

	/**
	 * Register a callback to be invoked when an item has been clicked. Set the
	 * {@link OnItemClickListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		_onItemClickListener = listener;
	}

	/**
	 * @return The callback to be invoked with an item in this adapter has been
	 *         clicked, or null if no callback has been set.
	 */
	public final OnItemClickListener getOnItemClickListener() {
		return _onItemClickListener;
	}

	/**
	 * Interface definition for a callback to be invoked when an item has been
	 * selected. The interface is invoked whenever an item in this adapter is
	 * changed.
	 */
	public interface OnItemSelectionUpdatedListener {
		/**
		 * Callback method to be invoked when an item has been selected.
		 * 
		 * @param view
		 *            The view that was selected.
		 * @param index
		 *            The index of item that was selected.
		 */
		void onItemSelectionUpdated(View view, int index);
	}

	/**
	 * Register a callback to be invoked when an item has been selected. Sets
	 * the {@link OnItemSelectionUpdatedListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public void setOnItemSelectionUpdatedListener(
			OnItemSelectionUpdatedListener listener) {
		_onItemSelectionUpdatedListener = listener;
	}

	/**
	 * @return The callback to be invoked with an item has been selected, or
	 *         null if no callback has been set.
	 */
	public OnItemSelectionUpdatedListener getOnItemSelectionUpdatedListener() {
		return _onItemSelectionUpdatedListener;
	}

	/**
	 * Interface definition for a callback to be invoked when the wheel has been
	 * rotated.
	 */
	protected interface OnRotateListener {
		/**
		 * Callback method to be invoked when the wheel has been rotated.
		 * 
		 * @param baseAngle
		 *            The base angle of wheel.
		 * @param angle
		 *            A total amount of angle that has been rotated from the
		 *            baseAngle.
		 */
		void onWheelRotate(int baseAngle, float angle);
	}

	/**
	 * Register a callback to be invoked when the wheel has been rotated. Set
	 * the {@link OnRotateListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	protected void setOnRotateListener(OnRotateListener listener) {
		_onRotateListener = listener;
	}

	/**
	 * @return The callback to be invoked with the wheel has been rotated, or
	 *         null if no callback has been set.
	 */
	protected final OnRotateListener getOnRotateListener() {
		return _onRotateListener;
	}

	/**
	 * A wheel rotation listener receives notifications from a wheel.
	 * Notification indicate rotation related events, such as the start or end
	 * of the rotation.
	 */
	public interface OnWheelRotationListener {

		/**
		 * Notifies the start of the rotation of wheel.
		 */
		void onWheelRotationStart();

		/**
		 * Notifies the end of the rotation of wheel.
		 */
		void onWheelRotationEnd();
	}

	/**
	 * Register a callback to be invoked when the wheel rotation has been
	 * finished. Set the {@link OnWheelRotationListener}.
	 * 
	 * @param listener
	 *            The callback that will be invoked.
	 */
	public void setOnWheelRotationListener(OnWheelRotationListener listener) {
		_onWheelRotationListener = listener;
	}

	/**
	 * @return The callback to be invoked with the wheel rotation has been
	 *         finished, or null if no callback has been set.
	 */
	public OnWheelRotationListener getOnWheelRotationListener() {
		return _onWheelRotationListener;
	}

	/**
	 * Call the {@link OnItemClickListener}, if it is defined.
	 * 
	 * @param view
	 *            The view within the WheelAdapter that was clicked.
	 * @param position
	 *            The position of the view in the adapter.
	 * @param id
	 *            The ID of the item that was clicked.
	 * @return True, if there was an assigned {@link OnItemClickListener} that
	 *         was called, false otherwise is returned.
	 */
	public boolean dispatchItemClick(View view, int position, long id) {
		if (_onItemClickListener != null) {
			playSoundEffect(SoundEffectConstants.CLICK);
			_onItemClickListener.onItemClick(this, view, position, id);
			return true;
		}
		return false;
	}

	/**
	 * Returns the adapter currently associated with this component.
	 * 
	 * @return The adapter used to provide this Wheel's content.
	 */
	public abstract T getAdapter();

	/**
	 * Sets the adapter that provides the items to represent the wheel in this
	 * component.
	 * 
	 * @param adapter
	 *            The adapter to use to create this component's content.
	 */
	public abstract void setAdapter(T adapter);

	/**
	 * @return The number of items owned by the Adapter associated with this
	 *         WheelAdapter.
	 */
	public int getCount() {
		return _itemCount;
	}

	/**
	 * Sets the number of items owned by the Adapter associated with this
	 * WheelAdapter.
	 * 
	 * @param itemCount
	 *            The number of items.
	 */
	public void setCount(int itemCount) {
		_itemCount = itemCount;
	}

	/**
	 * Gets the object associated with the specified position in this
	 * WheelAdapter.
	 * 
	 * @param position
	 *            Which data to get.
	 * @return The object associated with the specified position in this
	 *         WheelAdapter.
	 */
	public Object getItemAtPosition(int position) {
		T adapter = getAdapter();
		return (adapter == null || position < 0) ? null : adapter
				.getItem(position);
	}

	/**
	 * Gets the data associated with the specified position in this
	 * WheelAdapter.
	 * 
	 * @param position
	 *            Which data to get.
	 * @return The row id associated with the specified position in this
	 *         WheelAdapter.
	 */
	public long getItemIdAtPosition(int position) {
		T adapter = getAdapter();
		return (adapter == null || position < 0) ? INVALID_ID : adapter
				.getItemId(position);
	}

	/**
	 * This method is not supported and throws an UnsupportedOperationException
	 * when called. You probably want
	 * {@link #setOnItemClickListener(OnItemClickListener)} instead.
	 * 
	 * @see #setOnItemClickListener(OnItemClickListener)
	 * @throws UnsupportedOperationException
	 *             Every time this method is invoked.
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		throw new RuntimeException(
				"Don't call setOnClickListener for a WheelAdapter. "
						+ "You probably want setOnItemClickListener instead.");
	}
}
