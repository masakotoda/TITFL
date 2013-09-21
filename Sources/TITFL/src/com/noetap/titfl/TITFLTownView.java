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
	private Rect m_rect;
	private TITFLActivity m_activity;
	
	public TITFLTownView(Context context, AttributeSet attrs)
	{
		super(context, attrs); // Do not call super(context). Always pass attrs. Otherwise findViewById will NOT work!
		setFocusable(true);
	}
	
	public void initialize()
	{
		m_activity = (TITFLActivity)getContext();
		int w = NoEtapUtility.getScreenWidth(m_activity);
		int h = NoEtapUtility.getScreenHeight(m_activity);

		m_rect = new Rect(0, 0, h, w);

		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.width = h;
		params.height = w;
		this.setLayoutParams(params);

		Rect rectTown;
		Rect rectAvatar;
		Rect rectPlayerInfo;

		int playerInfoW = (int)(0.6 * w);
		int playerInfoH = w;
		int playerInfoLeft = h - playerInfoW;
		rectPlayerInfo = new Rect(playerInfoLeft, 0, h, playerInfoH);
		m_activity.getTown().setPlayerInfoRect(rectPlayerInfo);

		int avatarW = playerInfoW / 2;
		int avatarH = playerInfoH / 2;
		int avatarLeft = playerInfoLeft;
		rectAvatar = new Rect(avatarLeft, 0, avatarLeft + avatarW, avatarH);
		m_activity.getTown().setAvatarRect(rectAvatar);

		rectTown = new Rect(0, 0, h - playerInfoW, w);
		m_activity.getTown().setTownRect(rectTown);
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

		if (m_activity == null)
			return;
		
		try
		{
			Paint paint = new Paint();
			paint.setARGB(255, 192, 192, 255);
			canvas.drawRect(m_rect, paint);		
	
			// Draw town	
			if (m_activity.getTown() != null)
				m_activity.getTown().draw(canvas, paint);		
		}
		catch (Exception e)
		{			
		}
	}
}
