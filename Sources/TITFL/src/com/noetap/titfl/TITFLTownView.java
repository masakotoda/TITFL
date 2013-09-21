package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TITFLTownView  extends View 
{
	private static Rect m_rect;
	
	public TITFLTownView(Context context, AttributeSet attrs)
	{
		super(context);
		setFocusable(true);
	}
	
	static public Rect getPlayerRect()
	{
		Rect rect = new Rect();
		int w = m_rect.width() * 4 / 16;
		int h = m_rect.width() * 3 / 16;
		rect.left = w;
		rect.right = w * 2;
		rect.top = (int)(h * 2.5);
		rect.bottom = (int)(h * 6);
		return rect;
	}

	static private Rect getGoodsRect(int index)
	{
		Rect rect = new Rect();
		int anchorW = m_rect.width() * 4 / 16;
		int anchorH = m_rect.width() * 3 * 7 / 16;
		int w = m_rect.width() / 10;
		if (index < 5)
		{
			rect.left = anchorW + w * index;
			rect.right = rect.left + w;
			rect.bottom = anchorH - w;
			rect.top = rect.bottom - w;
		}
		else
		{
			index -= 5;
			rect.left = anchorW + w * index;
			rect.right = rect.left + w;
			rect.bottom = anchorH;
			rect.top = rect.bottom - w;
		}

		rect.top++;
		rect.bottom--;
		rect.left++;
		rect.right--;
		return rect;
	}
	
	static Rect getClockRect()
	{
		Rect rect = new Rect();
		int w = m_rect.width() * 4 / 16;
		int h = m_rect.width() * 3 / 16;
		rect.left = w;
		rect.right = rect.left + h;
		rect.top = h;
		rect.bottom = rect.top + h;

		rect.top += 10;
		rect.bottom -= 10;
		rect.left += 10;
		rect.right -= 10;
		return rect;
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

		try
		{
			TITFLActivity activity = (TITFLActivity)getContext();
			if (activity == null)
				return;
			
			// Fill background
			if (m_rect == null)
			{			
				int w = NoEtapUtility.getScreenWidth(activity);
				m_rect = new Rect(0, 0, w, w);
	
				ViewGroup.LayoutParams params = this.getLayoutParams();
				params.width = w;
				params.height = w;
				this.setLayoutParams(params);
			}
			
			Paint paint = new Paint();
			paint.setARGB(255, 192, 192, 255);
			canvas.drawRect(m_rect, paint);		
	
			// Draw town	
			if (activity.getTown() != null)
				activity.getTown().draw(canvas, m_rect);
		
			paint.setARGB(255, 255, 192, 255);

			// Draw items
			for (int i = 0; i < 10; i++)
			{
				Rect rect = getGoodsRect(i);
				//canvas.drawRect(rect, paint);
			}
			
			// Draw clock
			{
				Rect rect = getClockRect();
				//canvas.drawCircle((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, rect.width() / 2, paint);
			}
		}
		catch (Exception e)
		{			
		}
	}
}
