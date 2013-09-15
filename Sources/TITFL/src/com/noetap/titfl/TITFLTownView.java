package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TITFLTownView  extends View 
{
	public TITFLTownView(Context context, AttributeSet attrs)
	{
		super(context);
		setFocusable(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{	
		final TITFLActivity activity = (TITFLActivity)getContext();
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				activity.getTown().handleActionDown(event);
				invalidate();
				break;
			default:
				break;
		}
		
		return true;
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);

		// Fill background
		Rect viewRect = new Rect();
		this.getWindowVisibleDisplayFrame(viewRect);

		Rect rectDst = new Rect(0, 0, viewRect.width(), viewRect.height());
		Paint paint = new Paint();
		paint.setARGB(255, 192, 192, 255);
		canvas.drawRect(rectDst, paint);		

		// Draw town
		try
		{
			final TITFLActivity activity = (TITFLActivity)getContext();
			if (activity == null)
				return;
	
			if (activity.getTown() != null)
				activity.getTown().draw(canvas, rectDst);
		}
		catch (Exception e)
		{			
		}
	}
}
