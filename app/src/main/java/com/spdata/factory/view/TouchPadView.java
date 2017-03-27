package com.spdata.factory.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchPadView extends View {

	private final String TAG = "TouchPadView";

	private Activity mActivity;

	/**
	 * screen's height
	 */
	private int mScreenHeight;

	/**
	 * screen's width
	 * 屏幕
	 */
	private int mScreenWidth;

	/**
	 * lattice's height
	 * 网格高
	 */
	private final int mGridHeight = 40;

	/**
	 * lattice's width
	 * 网格宽
	 */
	private final int mGridWidth = 40;

	/**
	 * Each Row can has the number of lattice
	 *每一行可以有格子的数量
	 */
	private int mRowCount;

	/**
	 * Each column can has the number of lattice
	 * 每一列可以有格子的数量
	 */
	private int mColCount;

	/**
	 * A  total number of the lattice
	 * 格的总数
	 */
	private int mRectCount;

	/**
	 * rect's and isTouch's index
	 */
	private int mIndex = 0;

	private Rect[] rect;

	/**
	 * Record whether the lattice is touched
	 */
	private boolean[] isTouch;

	private int[] coordinateX;
	private int[] coordinateY;


	public TouchPadView(Activity activity) {
		super(activity);
		this.mActivity = activity;
		init();
	}

	private void init() {

		getScreenHeightAndWidth();//获取屏幕宽高

		mRowCount = mScreenWidth / mGridWidth + 1;
		mColCount = mScreenHeight / mGridHeight + 1;

		Log.v(TAG, "mRowCount = " + mRowCount);
		Log.v(TAG, "mColCount = " + mColCount);

		mRectCount = (mRowCount - 1) * (mColCount - 1);
		Log.v(TAG, "mRectCount = " + mRectCount);

		coordinateX = new int[mRowCount];
		coordinateY = new int[mColCount];

		rect = new Rect[mRectCount];
		isTouch = new boolean[mRectCount];

		/**
		 * Initialize All lattice have not been touched
		 */
		for(int i = 0; i < mRectCount; i++){
			isTouch[i] = false;
		}

		/**
		 * Initialize All lattice's upper left corner X-coordinate
		 */
		for (int i = 0; i < mRowCount; i++) {
			coordinateX[i] = i * mGridWidth;
			Log.v(TAG, "coordinateX[" + i + "] = " + coordinateX[i]);
		}

		/**
		 * Initialize All lattice's upper left corner Y-coordinate
		 */
		for (int i = 0; i < mColCount; i++) {
			coordinateY[i] = i * mGridHeight;
			Log.v(TAG, "coordinateY[" + i + "] = " + coordinateY[i]);
		}


		for (int i = 0; i < mRowCount - 1; i++) {
			for (int j = 0; j < mColCount - 1; j++) {
				rect[mIndex++] = new Rect(coordinateX[i], coordinateY[j],
						coordinateX[i + 1], coordinateY[j + 1]);
				Log.v(TAG, "coordinateX[" + i + "] = " + coordinateX[i]);
				Log.v(TAG, "coordinateY[" + j + "] = " + coordinateY[j]);
				Log.v(TAG, "coordinateX[" + (i+1) + "] = " + coordinateX[i+1]);
				Log.v(TAG, "coordinateY[" + (j+1) + "] = " + coordinateY[j+1]);
			}
		}

	}

	/**
	 * Get the screen's height and Width
	 */
	private void getScreenHeightAndWidth() {

		DisplayMetrics displayMestrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(
				displayMestrics);
		mScreenHeight = displayMestrics.heightPixels;
		mScreenWidth = displayMestrics.widthPixels;
		Log.v(TAG, "mScreenHeight = " + mScreenHeight);
		Log.v(TAG, "mScreenWidth = " + mScreenWidth);

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.v(TAG, "onDraw");
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);


		for (int i = 0; i < mRectCount; i++){
			//Log.v(TAG, "index="+i);
			canvas.drawRect(rect[i], paint);
		}

		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);

		for (int i = 0; i < mRectCount; i++){
			if(isTouch[i] == true){
				canvas.drawRect(rect[i], paint);
			}

		}
	}

	/**
	 * Judge all the grid Whether have been touched
	 *
	 */
	private boolean allRectChange(){
		boolean isAllRectChange = false;
		int index = 0;
		for(int i = 0; i < mRectCount; i++){
			index = i;
			if(isTouch[i]){
				continue;
			}
			else{
				break;
			}
		}
		Log.v(TAG,"index = "+index);
		if(index + 1 == mRectCount){
			isAllRectChange = true;
			Log.v(TAG,"isAllRectChange = true");
		}

		return isAllRectChange;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(allRectChange()){
			mActivity.finish();
		}
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		if(touchX == mScreenWidth){
			touchX = touchX -1;
		}
		if(touchY == mScreenHeight){
			touchY = touchY -1;
		}
		Log.v(TAG, "touchX = "+touchX);
		Log.v(TAG, "touchY = "+touchY);
		touchWhere(touchX, touchY);
		invalidate();
		return true;
	}


	private void touchWhere(int touchX, int touchY){
		int indexX = touchX / mGridWidth;
		int indexY = touchY / mGridHeight;
		Log.v(TAG, "indexX = "+indexX);
		Log.v(TAG, "indexY = "+indexY);
		isTouch[indexX * (mColCount - 1) + indexY] = true;
		Log.v(TAG, indexX * (mColCount - 1) + indexY+"");
	}

}
