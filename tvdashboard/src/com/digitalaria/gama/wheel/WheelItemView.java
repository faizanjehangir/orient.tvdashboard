/*
    GAMA - Open Source Mobile UI Component Library
    Copyright (c) 2012 Digital Aria Inc.
    licensing@digitalaria.com
    For more details, please visit: http://developer.digitalaria.com/gama/license
*/

package com.digitalaria.gama.wheel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

class WheelItemView extends ImageButton {

	private int _index;
	private float _x;
	private float _y;
	private float _width;
	private float _height;
	private boolean _drawn;
	private float _currentAngle;

	private boolean _hasRotation = false;
	private float _dx=0;
	private float _dy=0;

	public WheelItemView(Context context) {
		this(context, null, 0);
	}	

	public WheelItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WheelItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageBitmap(Bitmap src)
	{	
		_width = src.getWidth();
		_height = src.getHeight();

		if(_hasRotation) {
			calculateDistance();
		}

		super.setImageBitmap(src);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if(_hasRotation) {
			Drawable d = getDrawable();
			if(d!=null && d instanceof BitmapDrawable && ((BitmapDrawable)d).getBitmap()!=null) {
				Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
				canvas.save();
				canvas.translate(_dx, _dy);
				canvas.rotate(_currentAngle+90, _width/2f, _height/2f);
				canvas.drawBitmap(((BitmapDrawable)d).getBitmap(),0, 0, p);
				canvas.restore();
				return;
			}
		}

		super.onDraw(canvas);
	}

	private void calculateDistance() 
	{
		_dx = (float) ((Math.sqrt(_width*_width + _height*_height)-_width)/2.0f);
		_dy = (float) ((Math.sqrt(_width*_width + _height*_height)-_height)/2.0f);
	}

	public void setIndex(int index) 
	{
		this._index = index;
	}

	public int getIndex() 
	{
		return _index;
	}

	public void setCurrentAngle(float currentAngle) 
	{
		this._currentAngle = currentAngle;
	}

	public float getCurrentAngle() 
	{
		return _currentAngle;
	}

	public void setAxisX(float x) 
	{
		this._x = x;
	}

	public float getAxisX() 
	{
		return _x;
	}

	public void setAxisY(float y) 
	{
		this._y = y;
	}

	public float getAxisY() 
	{
		return _y;
	}

	public void setDrawn(boolean drawn) 
	{
		this._drawn = drawn;
	}

	public boolean isDrawn() 
	{
		return _drawn;
	}

	public void setItemWidth(float _width) 
	{
		this._width = _width;
	}

	public float getItemWidth() 
	{
		return _width;
	}

	public void setItemHeight(float _height) 
	{
		this._height = _height;
	}

	public float getItemHeight() 
	{
		return _height;
	}

	public int getDistanceX() 
	{
		return (int)_dx;
	}

	public int getDistanceY() 
	{
		return (int)_dy;
	}

	public void setRotatedItem(Boolean flag) 
	{
		_hasRotation = flag;
		if(_hasRotation && (_dx==0 || _dy ==0)) {
			calculateDistance();
		}

		if(!_hasRotation) {
			_dx = _dy = 0;
		}
	}
}
