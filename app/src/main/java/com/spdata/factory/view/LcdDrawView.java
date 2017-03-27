package com.spdata.factory.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class LcdDrawView extends View {

	public static int flag = 0;

	public LcdDrawView(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		switch (flag) {
		case 1:
			canvas.drawColor(Color.GREEN);
			return;
		case 2:
			canvas.drawColor(Color.BLUE);
			return;
		case 3:
			canvas.drawColor(Color.WHITE);
			return;
		case 4:
			canvas.drawColor(Color.BLACK);
			return;
		case 5:
			canvas.drawColor(Color.GRAY);
			return;
		}
		canvas.drawColor(Color.RED);
	}

}
