package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.MotionEvent;

public class TITFLTown 
{
	final static int m_maxSlot = 20;
	
	private static SparseArray<Rect> m_slotRect;
	private Activity m_activity;
	private int m_currentWeek;
	private ArrayList<TITFLTownElement> m_elements;
	private TITFLPlayer m_activePlayer;
	
	TITFLTown(Activity activity)
	{
		m_activity = activity;
		m_elements = TITFLTownElement.loadTownElements(m_activity.getAssets(), this);
	}
	
	void setActivePlayer(TITFLPlayer player)
	{
		m_activePlayer = player;
	}
	
	void draw(Canvas canvas, Rect rect)
	{
		initializeSlotRect(rect);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(48);

		canvas.drawText("week #" + Integer.toString(getWeek()), 200, rect.height() - 200, paint);

		for (int i = 0; i < m_elements.size(); i++)
		{
			m_elements.get(i).draw(canvas);
		}
	}
	
	void addWeek()
	{
		m_currentWeek++;
	}
	
	int getWeek()
	{
		return m_currentWeek;
	}
	
	void handleActionDown(MotionEvent event)
	{
		if (m_activePlayer == null)
			return;
		
		int slot = positionToSlot((int)event.getX(), (int)event.getY());
		if (slot >= 0)
		{	
			TITFLTownElement element = getTownElement(slot);
			m_activePlayer.goTo(m_activity, element);
		}
	}
	
	int positionToSlot(int x, int y)
	{
		for (int i = 0; i < m_slotRect.size(); i++)
		{
			int key = m_slotRect.keyAt(i);
			if (m_slotRect.get(key).contains(x, y))
				return key;
		}			
		return -1;
	}
	
	TITFLTownElement getTownElement(int slot)
	{
		for (int i = 0; i < m_elements.size(); i++)
		{
			if (m_elements.get(i).slot() == slot)
				return m_elements.get(i);
		}
		return null;
	}
	
	Rect slotToPosition(int slot)
	{
		return m_slotRect.get(slot);
	}
	
	private void initializeSlotRect(Rect rect)
	{		
		if (m_slotRect != null)
			return;
				
		m_slotRect = new SparseArray<Rect>();
		
		int w = rect.width();
		
		int eachW = w / 4;
		int eachH = (eachW * 3) / 4;

		// I write them down just because it's easier to picture in my mind...
		
		m_slotRect.append(0, new Rect(eachW * 0, eachH * 0, eachW * 1, eachH * 1));
		m_slotRect.append(1, new Rect(eachW * 0, eachH * 1, eachW * 1, eachH * 2));
		m_slotRect.append(2, new Rect(eachW * 0, eachH * 2, eachW * 1, eachH * 3));
		m_slotRect.append(3, new Rect(eachW * 0, eachH * 3, eachW * 1, eachH * 4));
		m_slotRect.append(4, new Rect(eachW * 0, eachH * 4, eachW * 1, eachH * 5));
		m_slotRect.append(5, new Rect(eachW * 0, eachH * 5, eachW * 1, eachH * 6));
		m_slotRect.append(6, new Rect(eachW * 0, eachH * 6, eachW * 1, eachH * 7));

		m_slotRect.append(7, new Rect(eachW * 0, eachH * 7, eachW * 1, eachH * 8));
		m_slotRect.append(8, new Rect(eachW * 1, eachH * 7, eachW * 2, eachH * 8));
		m_slotRect.append(9, new Rect(eachW * 2, eachH * 7, eachW * 3, eachH * 8));

		m_slotRect.append(10, new Rect(eachW * 3, eachH * 7, eachW * 4, eachH * 8));
		m_slotRect.append(11, new Rect(eachW * 3, eachH * 6, eachW * 4, eachH * 7));
		m_slotRect.append(12, new Rect(eachW * 3, eachH * 5, eachW * 4, eachH * 6));
		m_slotRect.append(13, new Rect(eachW * 3, eachH * 4, eachW * 4, eachH * 5));
		m_slotRect.append(14, new Rect(eachW * 3, eachH * 3, eachW * 4, eachH * 4));
		m_slotRect.append(15, new Rect(eachW * 3, eachH * 2, eachW * 4, eachH * 3));
		m_slotRect.append(16, new Rect(eachW * 3, eachH * 1, eachW * 4, eachH * 2));

		m_slotRect.append(17, new Rect(eachW * 3, eachH * 0, eachW * 4, eachH * 1));
		m_slotRect.append(18, new Rect(eachW * 2, eachH * 0, eachW * 3, eachH * 1));
		m_slotRect.append(19, new Rect(eachW * 1, eachH * 0, eachW * 2, eachH * 1));
	}
}
