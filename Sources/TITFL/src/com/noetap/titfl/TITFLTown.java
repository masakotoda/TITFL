package com.noetap.titfl;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.MotionEvent;

public class TITFLTown 
{
	final static int m_maxSlot = 20;
	
	private SparseArray<Rect> m_slotRect;
	private ArrayList<Rect> m_nodeRect;
	private Activity m_activity;
	private int m_currentWeek;
	private ArrayList<TITFLTownElement> m_elements;
	private TITFLPlayer m_activePlayer;
	private ArrayList<TITFLTownMap> m_townmaps;
	
	private int m_mapId;
			
	TITFLTown(Activity activity)
	{
		m_mapId = 4; // map is 0,1,2,3,4 for now. see assets\default_townmap.xml
		
		m_activity = activity;
		m_elements = TITFLTownElement.loadTownElements(m_activity.getAssets(), this);
		m_townmaps = TITFLTownMap.loadTownMaps(m_activity.getAssets());
	}
	
	void setActivePlayer(TITFLPlayer player)
	{
		m_activePlayer = player;
	}
	
	AssetManager getAssets()
	{
		return m_activity.getAssets();
	}
	
	void draw(Canvas canvas, Rect rect)
	{
		initializeSlotRect(rect);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		int w = m_nodeRect.get(0).width();
		int h = m_nodeRect.get(0).height();
		for (int i = 0; i < TITFLTownMap.num_nodes; i++)
		{
			TITFLTownMapNode slot = m_townmaps.get(m_mapId).m_nodes.get(i);
			for (int j = 0; j < slot.m_link.size(); j++)
			{
				int startX = slot.m_x * w + w / 2;
				int stopX = slot.m_link.get(j).m_x * w + w / 2;
				int startY = slot.m_y * h + h / 2;
				int stopY = slot.m_link.get(j).m_y * h + h / 2;
				paint.setStrokeWidth(20);
				canvas.drawLine(startX, startY, stopX, stopY, paint);
			}
		}
		
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
			TITFLTownElement destination = getTownElement(slot);
			
			TITFLTownMapRouteFinder finder = new TITFLTownMapRouteFinder(m_townmaps.get(m_mapId).m_nodes);
			TITFLTownElement location = m_activePlayer.getLocation();
			if (location == null)
				location = m_elements.get(0);
			ArrayList<TITFLTownMapNode> route1 = finder.findRoute(location.getNode(), destination.getNode());
			ArrayList<TITFLTownMapNode> route2 = finder.findRoute(destination.getNode(), location.getNode());
			// route1 and route2 may be different. We can pick the shorter route if we want.
			m_activePlayer.goTo(m_activity, destination, route2);
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
	
	Rect nodeToPosition(int slot)
	{
		return m_nodeRect.get(slot);
	}

	private void initializeSlotRect(Rect rect)
	{		
		if (m_slotRect != null)
			return;
				
		m_slotRect = new SparseArray<Rect>();
		m_nodeRect = new ArrayList<Rect>();
		
		int w = rect.width();
		
		int eachW = w / 4;
		int eachH = (eachW * 2) / 3;

		m_slotRect.append(0, new Rect(eachW * 0, eachH * 0, eachW * 1, eachH * 1));
		m_slotRect.append(1, new Rect(eachW * 1, eachH * 0, eachW * 2, eachH * 1));
		m_slotRect.append(2, new Rect(eachW * 2, eachH * 0, eachW * 3, eachH * 1));
		m_slotRect.append(3, new Rect(eachW * 3, eachH * 0, eachW * 4, eachH * 1));

		m_slotRect.append(4, new Rect(eachW * 0, eachH * 1, eachW * 1, eachH * 2));
		m_slotRect.append(5, new Rect(eachW * 1, eachH * 1, eachW * 2, eachH * 2));
		m_slotRect.append(6, new Rect(eachW * 2, eachH * 1, eachW * 3, eachH * 2));
		m_slotRect.append(7, new Rect(eachW * 3, eachH * 1, eachW * 4, eachH * 2));

		m_slotRect.append(8, new Rect(eachW * 0, eachH * 2, eachW * 1, eachH * 3));
		//blank
		m_slotRect.append(9, new Rect(eachW * 2, eachH * 2, eachW * 3, eachH * 3));
		//blank

		//blank
		m_slotRect.append(10, new Rect(eachW * 1, eachH * 3, eachW * 2, eachH * 4));
		//blank
		m_slotRect.append(11, new Rect(eachW * 3, eachH * 3, eachW * 4, eachH * 4));
		
		m_slotRect.append(12, new Rect(eachW * 0, eachH * 4, eachW * 1, eachH * 5));
		m_slotRect.append(13, new Rect(eachW * 1, eachH * 4, eachW * 2, eachH * 5));
		m_slotRect.append(14, new Rect(eachW * 2, eachH * 4, eachW * 3, eachH * 5));
		m_slotRect.append(15, new Rect(eachW * 3, eachH * 4, eachW * 4, eachH * 5));
		
		m_slotRect.append(16, new Rect(eachW * 0, eachH * 5, eachW * 1, eachH * 6));
		m_slotRect.append(17, new Rect(eachW * 1, eachH * 5, eachW * 2, eachH * 6));
		m_slotRect.append(18, new Rect(eachW * 2, eachH * 5, eachW * 3, eachH * 6));
		m_slotRect.append(19, new Rect(eachW * 3, eachH * 5, eachW * 4, eachH * 6));

		for (int i = 0; i < TITFLTownMap.num_rows; i++)
		{
			for (int j = 0; j < TITFLTownMap.num_columns; j++)
			{
				m_nodeRect.add(new Rect(eachW * j, eachH * i, eachW * (1 + j), eachH * (1 + i)));
			}
		}

		for (int i = 0; i < m_elements.size(); i++)
		{
			m_elements.get(i).setNode(m_townmaps.get(m_mapId).m_nodes);
		}
	}
}
