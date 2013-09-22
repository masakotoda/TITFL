package com.noetap.titfl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TITFLPlayerView  extends View 
{
	private Rect m_rect;
	private TITFLActivity m_activity;
	private TITFLPlayer m_player;
	
	public TITFLPlayerView(Context context, AttributeSet attrs)
	{
		super(context, attrs); // Do not call super(context). Always pass attrs. Otherwise findViewById will NOT work!
		setFocusable(true);
	}
	
	public void initialize()
	{
		m_activity = (TITFLActivity)getContext();
		int w = NoEtapUtility.getScreenWidth(m_activity);
		int h = NoEtapUtility.getScreenHeight(m_activity);
		int playerInfoW = (int)(0.6 * w);
		int playerInfoH = w;
		int playerInfoLeft = h - playerInfoW;
		int avatarW = playerInfoW / 2;
		int avatarH = playerInfoH / 2;
		int avatarLeft = playerInfoLeft;

		m_rect = new Rect(0, 0, playerInfoW, w);

		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.width = playerInfoW;
		params.height = w;
		this.setLayoutParams(params);
	}	
	
	public void setPlayer(TITFLPlayer player)
	{
		m_player = player;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{	
		final TITFLActivity activity = (TITFLActivity)getContext();
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				//TODO
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

		if (m_activity == null)
			return;
		
		try
		{
			Paint paint = new Paint();
			paint.setColor(Color.LTGRAY);

			if (m_player != null)
				m_player.draw(canvas, paint);
			else if (m_activity.getTown().activePlayer() != null)
				m_activity.getTown().activePlayer().draw(canvas, paint);
			else
				canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
		}
		catch (Exception e)
		{			
		}
	}
}
