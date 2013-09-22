package com.noetap.titfl;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class TITFLTown 
{
	final static int m_maxSlot = 20;
	final int m_roadWidth = 60;
	
	private ArrayList<Rect> m_nodeRect;
	private Activity m_activity;
	private int m_currentWeek;
	private ArrayList<TITFLTownElement> m_elements;
	private ArrayList<TITFLTownElement> m_fixedElements;
	private ArrayList<TITFLTownElement> m_randomElements;
	private TITFLPlayer m_activePlayer;
	private TITFLTownMap m_townMap;
	private TITFL m_game;
	
	private Rect m_rectTown;
	private Rect m_rectAvatar;
	private Rect m_rectPlayerInfo;

	public TITFLTown(Activity activity, TITFL game, TITFLTownMap townMap)
	{		
		m_activity = activity;
		m_game = game;
		m_townMap = townMap;
		m_elements = TITFLTownElement.loadTownElements(m_activity.getAssets(), this);

		m_fixedElements = new ArrayList<TITFLTownElement>();
		m_randomElements = new ArrayList<TITFLTownElement>();
		for (int i = 0; i < m_elements.size(); i++)
		{
			TITFLTownElement element = m_elements.get(i);
			if (element.slot() < 0)
				m_randomElements.add(element);
			else
				m_fixedElements.add(element);
		}

		initialize();
	}
	
	public void setActivePlayer(TITFLPlayer player)
	{
		m_activePlayer = player;
	}
	
	public void setNextPlayer()
	{
		m_game.setNextPlayer(m_activePlayer);
		
		TITFLTownElement location = m_activePlayer.getLocation();
		if (location == null)
		{
			location = m_elements.get(0);
			m_activePlayer.setLocation(location);
		}		
		m_activePlayer.goTo(m_activity, location, false);
	}
	
	public AssetManager getAssets()
	{
		return m_activity.getAssets();
	}
	
	public Activity getActivity()
	{
		return m_activity;
	}

	public void setTownRect(Rect rect)
	{
		m_rectTown = rect;
	}

	public Rect townRect()
	{
		return m_rectTown;
	}

	public void setAvatarRect(Rect rect)
	{
		m_rectAvatar = rect;
	}

	public Rect avatarRect()
	{
		return m_rectAvatar;
	}

	public void setPlayerInfoRect(Rect rect)
	{
		m_rectPlayerInfo = rect;		
	}

	public Rect playerInfoRect()
	{
		return m_rectPlayerInfo;		
	}
	
	public TITFLTownMap townMap()
	{
		return m_townMap;
	}

	public void draw(Canvas canvas, Paint paint)
	{
		paint.setARGB(255, 128, 128, 128);

		int w = m_nodeRect.get(0).width();
		int h = m_nodeRect.get(0).height();
		
		// Draw roads
		for (int i = 0; i < TITFLTownMap.num_nodes; i++)
		{
			TITFLTownMapNode slot = m_townMap.nodes().get(i);
			for (int j = 0; j < slot.link().size(); j++)
			{
				int startX = slot.x() * w + w / 2;
				int stopX = slot.link().get(j).x() * w + w / 2;
				int startY = slot.y() * h + h / 2;
				int stopY = slot.link().get(j).y() * h + h / 2;
				
				paint.setStrokeWidth(m_roadWidth * NoEtapUtility.getFactor(getActivity()));
				canvas.drawLine(startX, startY, stopX, stopY, paint);

				int halfRoad = (m_roadWidth / 2) - 1;
				paint.setStrokeWidth(1);				
				canvas.drawCircle(startX, startY, halfRoad * NoEtapUtility.getFactor(getActivity()), paint);
				canvas.drawCircle(stopX, stopY, halfRoad * NoEtapUtility.getFactor(getActivity()), paint);
			}
		}
		
		// Draw elements
		for (int i = 0; i < m_elements.size(); i++)
		{
			m_elements.get(i).draw(canvas, paint);
		}
		
		// Draw player
		if (m_activePlayer != null)
		{
			m_activePlayer.draw(canvas, paint);
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
		
		if (m_activePlayer.isMoving())
			return;
		
		int slot = positionToSlot((int)event.getX(), (int)event.getY());
		if (slot < 0)
			return;

		TITFLTownElement destination = getTownElement(slot);
			
		TITFLTownElement location = m_activePlayer.getLocation();
		if (location == null)
		{
			location = m_elements.get(0);
			m_activePlayer.setLocation(location);
		}
		m_activePlayer.goTo(m_activity, destination, true);
	}
	
	int positionToSlot(int x, int y)
	{
		for (int i = 0; i < m_nodeRect.size(); i++)
		{
			if (m_nodeRect.get(i).contains(x, y))
			{
				for (int j = 0; j < m_elements.size(); j++)
				{
					int index = m_elements.get(j).node().index();
					if (i == index)
					{
						return j;
					}
				}
			}
		}
		return -1;
	}
	
	public TITFLTownElement getTownElement(int slot)
	{
		if (slot < m_elements.size())
			return m_elements.get(slot);
		return null;
	}
	
	public Rect slotToPosition(int slot)
	{
		int index = m_elements.get(slot).node().index();
		return m_nodeRect.get(index);
	}
	
	public Rect nodeToPosition(int slot)
	{
		return m_nodeRect.get(slot);
	}

	private TITFLTownElement getRandomElement()
	{
		Random random = new Random();		
		int remaining = m_randomElements.size();
		int next = (int)(remaining * random.nextFloat());
		if (next == remaining)
			next--;
		return m_randomElements.get(next);
	}
	
	public void changeRandomElement()
	{
		TITFLTownElement randomElement = getRandomElement();
		int maxIndex = m_maxSlot - 1;
		TITFLTownElement outOfBusiness = m_elements.get(maxIndex);
		randomElement.setSlot(outOfBusiness.slot());
		randomElement.setNode(outOfBusiness.node());
		m_elements.set(maxIndex, randomElement);
	}
	
	private void initialize()
	{		
		m_nodeRect = new ArrayList<Rect>();
		
		int w = NoEtapUtility.getScreenWidth(m_activity);		
		int eachW = w / 4;
		int eachH = (eachW * 2) / 3;
		
		for (int i = 0; i < TITFLTownMap.num_rows; i++)
		{
			for (int j = 0; j < TITFLTownMap.num_columns; j++)
			{
				m_nodeRect.add(new Rect(eachW * j, eachH * i, eachW * (1 + j), eachH * (1 + i)));
			}
		}

		m_elements.clear();

		int maxIndex = m_maxSlot - 1;
		Random random = new Random();		
		TITFLTownElement randomElement = getRandomElement();
		
		int remaining = m_fixedElements.size();
		for (int i = 0; i < m_townMap.nodes().size(); i++)
		{
			TITFLTownMapNode node = m_townMap.nodes().get(i);
			if (node.occupied())
			{
				if (m_elements.size() == maxIndex)
				{
					randomElement.setSlot(maxIndex);
					randomElement.setNode(node);
					m_elements.add(randomElement);
				}
				else
				{
					int next = (int)(remaining * random.nextFloat());
					if (next == remaining)
						next--;
	
					TITFLTownElement element = m_fixedElements.get(next);
					element.setSlot(m_elements.size());
					element.setNode(node);
					m_elements.add(element);
					m_fixedElements.remove(element);
					remaining--;
				}
			}
		}
	}
}
