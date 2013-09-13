package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TITFLTownView  extends View 
{
	public static Activity m_activity;
	
	public TITFLTownView(Context context, AttributeSet attrs)
	{
		super(context);
		setFocusable(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{		
		return true;
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);

		Rect viewRect = new Rect();
		this.getWindowVisibleDisplayFrame(viewRect);

		Rect rectDst = new Rect();		
		Paint paint = new Paint();
		paint.setARGB(255, 192, 192, 255);
		rectDst.top = 0;
		rectDst.left = 0;
		rectDst.right = viewRect.width();
		rectDst.bottom = viewRect.height();
		canvas.drawRect(rectDst, paint);		
	}
}
